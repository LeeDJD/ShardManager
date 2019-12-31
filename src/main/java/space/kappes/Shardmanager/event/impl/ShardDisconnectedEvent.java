package space.kappes.Shardmanager.event.impl;

import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;

public class ShardDisconnectedEvent extends ShardEvent {

    private String reason;


    public ShardDisconnectedEvent(ShardManager shardmanager, Shard shard) {
        super(shardmanager, shard);
    }

    public ShardDisconnectedEvent(ShardManager shardmanager, Shard shard, String reason) {
        super(shardmanager, shard);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
