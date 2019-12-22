package space.kappes.Shardmanager.event.impl;

import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;

public class ShardConnectedEvent extends ShardEvent {

    public ShardConnectedEvent(ShardManager shardmanager, Shard shard) {
        super(shardmanager, shard);
    }
}
