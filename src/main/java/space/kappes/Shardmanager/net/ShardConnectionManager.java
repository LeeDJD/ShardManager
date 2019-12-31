package space.kappes.Shardmanager.net;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import space.kappes.Shardmanager.core.Shard;
import space.kappes.Shardmanager.event.ListenerAdapter;
import space.kappes.Shardmanager.event.impl.ShardConnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardDisconnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardEvent;
import space.kappes.Shardmanager.event.impl.ShardLoginEvent;

import java.io.IOException;
import java.util.*;

public class ShardConnectionManager extends ListenerAdapter {

    private ArrayList<Integer> shardIds = new ArrayList<>();
    private boolean rebalancing = false;
    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public void onShardConnect(ShardConnectedEvent shardConnectedEvent) {
        logger.info(String.format("[ShardConnect] %s(%s) connected", shardConnectedEvent.getShard().getSocket().getInetAddress().getHostName(), shardConnectedEvent.getShard().getIp()));

    }

    @Override
    public void onShardDisconnect(ShardDisconnectedEvent shardDisconnectedEvent) {
        logger.info(String.format("[ShardDisconnect] %s(%s) disconnected. Reason: %s", shardDisconnectedEvent.getShard().getSocket().getInetAddress().getHostName(), shardDisconnectedEvent.getShard().getIp(), shardDisconnectedEvent.getReason()));
        rebalance(shardDisconnectedEvent);
    }

    @Override
    public void onShardLogin(ShardLoginEvent shardLoginEvent) {
        logger.info(String.format("[ShardLogin] %s(%s) logged in", shardLoginEvent.getShard().getSocket().getInetAddress().getHostName(), shardLoginEvent.getShard().getIp()));
        if (shardLoginEvent.getShardManager().getShardMap().size() == 1 && shardLoginEvent.getShardManager().getShardCalculator().getOptimalShardCount() != 1) {
            balanceShards(shardLoginEvent);
        } else if (!rebalancing)
            rebalance(shardLoginEvent);
    }

    private void balanceShards(ShardEvent event) {
        new Thread(() -> {
            if (event.getShardManager().getShardMap().size() == event.getShardManager().getShardCalculator().getOptimalShardCount()) {
                event.getShard().sendCommand("error", new JSONObject().put("type", "not_needed").put("message", "No more shards needed"));
                return;
            }
            if (event.getShardManager().getShardMap().size() == 1) {
                rebalancing = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        rebalance(event);
                    }
                }, 10000);
            }

        }, "ShardBalancer").start();
    }

    private void rebalance(ShardEvent event) {
        if (event.getShardManager().getShardMap().isEmpty()) {
            logger.error("[ShardManager] No shards connected. Skipping");
            return;
        }
        resetShardIds(event);
        if (event.getShardManager().getShardMap().size() == 1) {
            Shard shard = new ArrayList<>(event.getShardManager().getShardMap().values()).get(0);
            logger.info(String.format("[ShardManager] Starting %s shards on %s ", event.getShardManager().getShardCalculator().getShardsPerInstance(), shard.getName()));
            shard.sendCommand("start", new JSONObject().put("totalShards", event.getShardManager().getShardCalculator().getOptimalShardCount()).put("shardIds", getShardIdsForInstance(event.getShardManager().getShardCalculator().getShardsPerInstance())));
        } else {
            event.getShardManager().getShardMap().values().forEach(shard -> {
                shard.sendCommand("stop", new JSONObject().put("message", "Rebalancing shards"));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        logger.info(String.format("[ShardManager] Starting %s shards on %s ", event.getShardManager().getShardCalculator().getShardsPerInstance(), shard.getName()));
                        shard.sendCommand("start", new JSONObject().put("totalShards", event.getShardManager().getShardCalculator().getShardsPerInstance() * event.getShardManager().getShardMap().size()).put("shardIds", getShardIdsForInstance(event.getShardManager().getShardCalculator().getShardsPerInstance())));
                    }
                }, 5000);
            });
        }
        rebalancing = false;
    }

    private Integer[] getShardIds(int stop) {
        List<Integer> out = new ArrayList<>();
        for (int count = 0; count < stop; count++) {
            out.add(count);
        }
        return out.toArray(new Integer[0]);
    }

    private Integer[] getShardIdsForInstance(int shardsPerInstance) {
        List<Integer> out = new ArrayList<>();
        for (int count = 0; count < shardsPerInstance; count++) {
            out.add(shardIds.get(0));
            shardIds.remove(0);
        }
        return out.toArray(new Integer[0]);
    }

    private void resetShardIds(ShardEvent event) {
        shardIds.clear();
        event.getShardManager().getShardCalculator().calculate();
        shardIds.addAll(Arrays.asList(getShardIds(event.getShardManager().getShardCalculator().getShardsPerInstance() * event.getShardManager().getShardMap().size())));
    }

}
