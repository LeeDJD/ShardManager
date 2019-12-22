package space.kappes.Shardmanager.io.config;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Config extends JSONObject {

    private final File configFile;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Creates a {@link Config} object with a custom file
     *
     * @param file the file where the config should be located
     */
    public Config(File file) {
        this.configFile = file;

        if (!configFile.exists())
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                logger.error("Failed to create config File", e);
            }
        load();
    }


    public Config() {
        this(new File("config.json"));
    }

    /**
     * Add key-/value pairs to the config
     *
     * @param key   the key
     * @param value the value
     */
    public void set(String key, Object value) {
        put(key, value);
        save();
    }

    /**
     * Does the same as {@link Config#set(String, Object)} but only, if the key does not exist in the current objects
     *
     * @param key   the key
     * @param value the value
     */
    public void setDefault(String key, Object value) {
        if (!has(key))
            set(key, value);
    }

    /**
     * Saves the config to a file
     */
    public void save() {
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            fileWriter.write(toString(2));
        } catch (IOException e) {
            logger.error("Error while saving config to file", e);
        }
    }

    private void load() {
        try (Scanner scanner = new Scanner(configFile)) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine())
                stringBuilder.append(scanner.nextLine());
            String configContent = stringBuilder.toString();
            if (!configContent.equals("")) {
                JSONObject jsonObject = new JSONObject(configContent);
                for (String key : jsonObject.keySet()) {
                    put(key, jsonObject.get(key));
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Error while reading config file", e);
        }
    }
}

