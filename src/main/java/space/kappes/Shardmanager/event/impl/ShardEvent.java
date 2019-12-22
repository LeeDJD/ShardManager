package space.kappes.Shardmanager.event.impl;

import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;
import space.kappes.Shardmanager.event.Event;

public class ShardEvent extends Event {

    private final Shard shard;

    public ShardEvent(ShardManager shardmanager, Shard shard) {
        super(shardmanager);
        this.shard = shard;
    }

    public Shard getShard() {
        return this.shard;
    }
    
}
