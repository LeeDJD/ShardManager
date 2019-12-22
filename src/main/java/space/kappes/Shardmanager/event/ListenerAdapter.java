package space.kappes.Shardmanager.event;

import space.kappes.Shardmanager.event.impl.ShardConnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardDisconnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardLoginEvent;
import space.kappes.Shardmanager.event.impl.ShardMessageReceivedEvent;

public class ListenerAdapter {

    public void onShardConnect(ShardConnectedEvent shardConnectedEvent)  {}
    public void onShardDisconnect(ShardDisconnectedEvent shardDisconnectedEvent)  {}
    public void onShardMessage(ShardMessageReceivedEvent shardMessageReceivedEvent)  {}
    public void onShardLogin(ShardLoginEvent shardLoginEvent)  {}

    public void onEvent(Event event) {
        if (event instanceof ShardMessageReceivedEvent)
            onShardMessage((ShardMessageReceivedEvent) event);
        else if (event instanceof ShardConnectedEvent)
            onShardConnect((ShardConnectedEvent) event);
        else if (event instanceof ShardDisconnectedEvent)
            onShardDisconnect((ShardDisconnectedEvent) event);
        else if (event instanceof ShardLoginEvent)
            onShardLogin((ShardLoginEvent) event);
    }

}
