package space.kappes.Shardmanager.commands;

import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.command.Command;
import space.kappes.Shardmanager.core.HeartbeatManager;
import space.kappes.Shardmanager.core.Shard;

public class HeartbeatResponseCommand extends Command {

    public HeartbeatResponseCommand() {
        super("heartbeat_response", "Handles heartbeat returned from client/shard", false, new String[]{"oldTimestamp","timestamp"});
    }

    @Override
    public void run(ShardManager shardManager, Shard shard, JSONObject args) {
        HeartbeatManager.getSendHeartBeats().remove(shard.getIp());
        long ping = args.getLong("timestamp") - args.getLong("oldTimestamp");
        shard.setPing(ping);
        shard.sendCommand("ping", new JSONObject().put("ping", ping));
    }
}
