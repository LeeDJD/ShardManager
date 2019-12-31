package space.kappes.Shardmanager.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.event.ListenerAdapter;
import space.kappes.Shardmanager.event.impl.ShardMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {

    private final ShardManager shardManager;
    private final Map<String, Command> commandMap;
    private final Logger logger = LogManager.getLogger(getClass());

    public CommandManager(ShardManager shardManager) {
        this.shardManager = shardManager;
        this.commandMap = new HashMap<>();
    }

    public void registerCommand(Command command) {
        commandMap.put(command.getInvoke(), command);
    }

    public void registerCommands(Command... commands) {
        for (Command command : commands)
            registerCommand(command);
    }

    @Override
    public void onShardMessage(ShardMessageReceivedEvent shardMessageReceivedEvent) {
        String invoke = shardMessageReceivedEvent.getMessage().split("\\s")[0];
        if (!commandMap.containsKey(invoke))
            return;
        JSONObject jsonObject = new JSONObject(shardMessageReceivedEvent.getMessage().replace(invoke+" ",""));
        Command command = commandMap.get(invoke);
        if (!command.isWithoutLogin() && !shardManager.getShardMap().containsKey(shardMessageReceivedEvent.getShard().getIp())) {
            shardMessageReceivedEvent.getShard().sendCommand("error", new JSONObject().put("type", "Not Authorized").put("message", "The client first has to authorize itself."));
            return;
        }
        for (String required: command.getRequiredArgs()) {
            if (!jsonObject.has(required)) {
                logger.debug(jsonObject.toString());
                shardMessageReceivedEvent.getShard().sendCommand("error", new JSONObject().put("type", "Invalid Command").put("message", String.format("Missing argument: %s", required)));
                return;
            }
        }
        command.run(shardManager, shardMessageReceivedEvent.getShard(), jsonObject);
    }
}
