package space.kappes.Shardmanager.commands;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.command.Command;
import space.kappes.Shardmanager.core.Shard;
import space.kappes.Shardmanager.event.impl.ShardLoginEvent;
import space.kappes.Shardmanager.net.ConnectionHandler;

public class LoginCommand extends Command {

    private final Logger logger = LogManager.getLogger(getClass());

    public LoginCommand() {
        super("login", "Authorizes a Client/Shard", true, new String[]{"name", "secret"});
    }

    @Override
    public void run(ShardManager shardManager, Shard shard, JSONObject args) {
        String token = args.getString("secret");
        if (!token.equals(shardManager.getConfigManager().getAuthSecret())) {
            shard.sendCommand("error", new JSONObject().put("type", "invalid_login").put("message", "Invalid secret."));
            ConnectionHandler.getLoginQueue().remove(shard.getIp());
            return;
        }
        shard.setName(args.getString("name"));
        ConnectionHandler.getLoginQueue().remove(shard.getIp());
        shardManager.addShard(shard);
        shardManager.getShardCalculator().calculate();
        shard.sendCommand("logged_in");
        shardManager.getEventManager().call(new ShardLoginEvent(shardManager, shard));
    }
}
