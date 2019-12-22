package space.kappes.Shardmanager;


import space.kappes.Shardmanager.core.Shard;
import space.kappes.Shardmanager.core.ShardCalculator;
import space.kappes.Shardmanager.event.EventManager;
import space.kappes.Shardmanager.io.config.Config;
import space.kappes.Shardmanager.io.config.ConfigManager;
import space.kappes.Shardmanager.net.ConnectionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShardManager {

    private EventManager eventManager;
    private ConnectionHandler connectionHandler;
    private Map<String, Object> shardMap;
    private static ShardManager instance;
    private ConfigManager configManager;
    private ShardCalculator shardCalculator;

    public ShardManager() {
        instance = this;
        try {
            this.shardMap = new HashMap<>();
            this.configManager = new ConfigManager(new Config());
            this.eventManager = new EventManager();
            registerListeners();
            this.shardCalculator = new ShardCalculator(this);
            this.connectionHandler = new ConnectionHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
    }

    public void addShard(Shard shard) {
        shardMap.put(shard.getIp(), shard);
    }

    public void removeShard(Shard shard) {
        shardMap.remove(shard.getIp());
    }

    public void run() {
        connectionHandler.run();
    }

    public Map<String, Object> getShardMap() {
        return shardMap;
    }

    public static ShardManager getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public ShardCalculator getShardCalculator() {
        return shardCalculator;
    }
}
