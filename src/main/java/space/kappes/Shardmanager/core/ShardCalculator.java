package space.kappes.Shardmanager.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.kappes.Shardmanager.ShardManager;

public class ShardCalculator {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ShardManager shardManager;
    private int optimalShardCount;
    private int shardsPerInstance;
    private int availableInstances;

    public ShardCalculator(ShardManager shardManager){
        this.shardManager = shardManager;
        calculate();
    }

    private void calculate() {
        //TODO: Web Request to Discord API
        optimalShardCount = 1;

        availableInstances = shardManager.getShardMap().size();
        if (availableInstances == 1) {
            shardsPerInstance = optimalShardCount;
        } else {
            shardsPerInstance = (int) Math.ceil(optimalShardCount / availableInstances);
        }
    }

    public int getOptimalShardCount() {
        return optimalShardCount;
    }

    public int getShardsPerInstance() {
        return shardsPerInstance;
    }

    public int getAvailableInstances() {
        return availableInstances;
    }
}
