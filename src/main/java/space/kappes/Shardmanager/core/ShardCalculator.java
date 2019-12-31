package space.kappes.Shardmanager.core;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;

import java.io.IOException;

public class ShardCalculator {

    private final Logger logger = Logger.getLogger(getClass());
    private final ShardManager shardManager;
    private int optimalShardCount = 0;
    private int shardsPerInstance = 0;

    public ShardCalculator(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    public void calculate() {
        logger.debug("Fetching shardcount of discord api...");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://discordapp.com/api/gateway/bot")
                .header("Authorization", String.format("Bot %s", shardManager.getConfigManager().getBotToken()))
                .header("User-Agent", "ShardManager")
                .build();

        try (Response response = client.newCall(request).execute()) {
            optimalShardCount = new JSONObject(response.body().string()).getInt("shards");
        } catch (IOException e) {
            logger.error("Error fetching shardcount from discord api", e);
        }
        int availableInstances = shardManager.getShardMap().size();
        if (availableInstances <= 0)
            return;
        if (availableInstances >= optimalShardCount)
            shardsPerInstance = 1;
        else
            shardsPerInstance = (int) Math.ceil(optimalShardCount / availableInstances);
    }

    public int getOptimalShardCount() {
        return optimalShardCount;
    }

    public int getShardsPerInstance() {
        return shardsPerInstance;
    }

}
