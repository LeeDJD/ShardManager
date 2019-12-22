package space.kappes.Shardmanager.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.kappes.Shardmanager.event.ListenerAdapter;
import space.kappes.Shardmanager.event.impl.ShardConnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardDisconnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardLoginEvent;

import java.util.ArrayList;

public class ShardConnectionManager extends ListenerAdapter {

    private ArrayList<Integer> shardIds = new ArrayList<>();
    private boolean rebalancing = false;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onShardConnect(ShardConnectedEvent shardConnectedEvent) {
        logger.info(String.format("[ShardConnect] %s(%s) connected", shardConnectedEvent.getShard().getSocket().getInetAddress().getHostName(), shardConnectedEvent.getShard().getIp()));
    }

    @Override
    public void onShardDisconnect(ShardDisconnectedEvent shardDisconnectedEvent) {
        logger.info(String.format("[ShardDisconnect] %s(%s) disconnected. Reason: %s", shardDisconnectedEvent.getShard().getSocket().getInetAddress().getHostName(), shardDisconnectedEvent.getShard().getIp(), shardDisconnectedEvent.getReason()));
    }

    @Override
    public void onShardLogin(ShardLoginEvent shardLoginEvent) {
        logger.info(String.format("[ShardLogin] %s(%s) logged in", shardLoginEvent.getShard().getSocket().getInetAddress().getHostName(), shardLoginEvent.getShard().getIp()));
    }
}
