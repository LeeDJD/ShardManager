package space.kappes.Shardmanager;


import org.apache.log4j.Logger;
import space.kappes.Shardmanager.command.CommandManager;
import space.kappes.Shardmanager.commands.HeartbeatResponseCommand;
import space.kappes.Shardmanager.commands.LoginCommand;
import space.kappes.Shardmanager.core.HeartbeatManager;
import space.kappes.Shardmanager.core.Shard;
import space.kappes.Shardmanager.core.ShardCalculator;
import space.kappes.Shardmanager.event.EventManager;
import space.kappes.Shardmanager.io.config.Config;
import space.kappes.Shardmanager.io.config.ConfigManager;
import space.kappes.Shardmanager.net.ConnectionHandler;
import space.kappes.Shardmanager.net.ShardConnectionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShardManager {

    private static final String VERSION = "0.1";
    private CommandManager commandManager;
    private EventManager eventManager;
    private ConnectionHandler connectionHandler;
    private Map<String, Shard> shardMap;
    private static ShardManager instance;
    private ConfigManager configManager;
    private HeartbeatManager heartbeatManager;
    private ShardCalculator shardCalculator;
    private final Logger logger = Logger.getLogger(getClass());

    public ShardManager() {
        instance = this;
        printHeader();
        init();
        run();
    }

    private void init() {
        try {
            this.shardMap = new HashMap<>();
            this.configManager = new ConfigManager(new Config());
            this.eventManager = new EventManager();
            this.commandManager = new CommandManager(this);
            this.heartbeatManager = new HeartbeatManager(this);
            registerListeners();
            registerCommands();
            this.shardCalculator = new ShardCalculator(this);
            this.connectionHandler = new ConnectionHandler(this);
        } catch (IOException e) {
            logger.error("Error initializing...exiting", e);
            System.exit(1);
        }
    }

    private void registerCommands() {
        commandManager.registerCommand(new LoginCommand());
        commandManager.registerCommand(new HeartbeatResponseCommand());
    }

    private void registerListeners() {
        eventManager.addListener(commandManager);
        eventManager.addListener(new ShardConnectionManager());
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

    private void printHeader() {
        System.out.println("   _____ _                   _                                             \n" +
                "  / ____| |                 | |                                            \n" +
                " | (___ | |__   __ _ _ __ __| |_ __ ___   __ _ _ __   __ _  __ _  ___ _ __ \n" +
                "  \\___ \\| '_ \\ / _` | '__/ _` | '_ ` _ \\ / _` | '_ \\ / _` |/ _` |/ _ \\ '__|\n" +
                "  ____) | | | | (_| | | | (_| | | | | | | (_| | | | | (_| | (_| |  __/ |   \n" +
                " |_____/|_| |_|\\__,_|_|  \\__,_|_| |_| |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_|   \n" +
                "                                                            __/ |          \n" +
                "                                                           |___/           ");
        System.out.println("Version: " + VERSION);
        System.out.println("Operating System: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
        System.out.println("Java Version: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
        System.out.println("Java VM Version: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
        System.out.println();
    }


    public Map<String, Shard> getShardMap() {
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

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HeartbeatManager getHeartbeatManager() {
        return heartbeatManager;
    }
}
