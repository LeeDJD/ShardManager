package space.kappes.Shardmanager.io.config;

import org.json.JSONObject;

public class ConfigManager {

    private final Config config;

    public ConfigManager(Config config) {
        this.config = config;
        setDefaults();
    }

    private void setDefaults() {
        JSONObject shardManagerProperty = new JSONObject();
        shardManagerProperty.put("port", System.getenv("SHARDMANAGER_PORT") == null ? 1024 : Integer.getInteger(System.getenv("SHARDMANAGER_PORT")));
        shardManagerProperty.put("secret", System.getenv("SHARDMANAGER_SECRET") == null ? "AuthSecret" : System.getenv("SHARDMANAGER_SECRET"));
        config.setDefault("shardmanager", shardManagerProperty);
        JSONObject botProperty = new JSONObject();
        botProperty.put("token", System.getenv("SHARDMANAGER_BOT_TOKEN") == null ? "BotToken" : System.getenv("SHARDMANAGER_BOT_TOKEN"));
        config.setDefault("bot",botProperty);
    }

    public String getAuthSecret() {
        return config.getJSONObject("shardmanager").getString("secret");
    }

    public int getPort() {
        return config.getJSONObject("shardmanager").getInt("port");
    }

    public String getBotToken() {
        return config.getJSONObject("bot").getString("token");
    }
}
