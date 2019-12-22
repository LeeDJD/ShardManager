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
        shardManagerProperty.put("port", 1024);
        shardManagerProperty.put("secret", "AuthSecret");
        config.setDefault("shardmanager", shardManagerProperty);
    }

    public String getAuthSecret() {
        return config.getJSONObject("shardmanager").getString("secret");
    }

    public int getPort() {
        return config.getJSONObject("shardmanager").getInt("port");
    }
}
