package space.kappes.Shardmanager.event.impl;

import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;

public class ShardMessageReceivedEvent extends ShardEvent {

    private final String message;

    public ShardMessageReceivedEvent(ShardManager shardmanager, Shard shard, String message) {
        super(shardmanager, shard);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
