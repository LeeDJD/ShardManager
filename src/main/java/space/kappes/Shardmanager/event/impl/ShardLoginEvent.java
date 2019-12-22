package space.kappes.Shardmanager.event.impl;

import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;

public class ShardLoginEvent extends ShardEvent {

    public ShardLoginEvent(ShardManager shardmanager, Shard shard) {
        super(shardmanager, shard);
    }
}
