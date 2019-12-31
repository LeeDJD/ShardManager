package space.kappes.Shardmanager.core;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.util.TimedMap;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HeartbeatManager {

    private final Logger logger = Logger.getLogger(getClass());
    private final static TimedMap<String, Shard> sendHeartBeats = new TimedMap<>();
    private final ShardManager shardManager;
    private final Timer timer;

    public HeartbeatManager(ShardManager shardManager) {
        this.shardManager = shardManager;
        this.timer = new Timer();
        
        start();
    }

    private void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<String, Shard> entry: shardManager.getShardMap().entrySet()) {
                    Shard shard = entry.getValue();
                    if (sendHeartBeats.containsKey(shard.getIp()))
                        continue;
                    shard.sendCommand("heartbeat", new JSONObject().put("timestamp", new Date().getTime()));
                    sendHeartBeats.put(shard.getIp(), shard, 15000, () -> {
                        try {
                            shard.close("Returned no heartbeat");
                        } catch (IOException e) {
                            logger.error("Error while closing Connection", e);
                        }
                    });
                }
            }
        }, 0, 10000);
    }

    private void close() {
        timer.cancel();
    }

    public static TimedMap<String, Shard> getSendHeartBeats() {
        return sendHeartBeats;
    }
}
