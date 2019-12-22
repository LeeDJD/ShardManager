package space.kappes.Shardmanager.event;

import space.kappes.Shardmanager.ShardManager;

import java.time.LocalDateTime;

public abstract class Event {

    private final ShardManager shardmanager;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    public Event(ShardManager shardmanager) {
        this.shardmanager = shardmanager;
    }

    public ShardManager getShardManager() {
        return shardmanager;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
