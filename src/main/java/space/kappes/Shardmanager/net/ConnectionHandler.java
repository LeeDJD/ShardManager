package space.kappes.Shardmanager.net;

import org.apache.log4j.Logger;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.core.Shard;
import space.kappes.Shardmanager.event.impl.ShardConnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardDisconnectedEvent;
import space.kappes.Shardmanager.util.TimedMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {

    private final ShardManager shardmanager;
    private static final TimedMap<String, Shard> loginQueue = new TimedMap<>();
    private final Thread connectionThread;
    private final int port;
    private final ServerSocket serverSocket;
    private final Logger logger = Logger.getLogger(getClass());
    private boolean running = false;

    public ConnectionHandler(ShardManager shardmanager) throws IOException {
        this.shardmanager = shardmanager;
        this.connectionThread = new Thread(this::listen);
        this.port = shardmanager.getConfigManager().getPort();
        this.serverSocket = new ServerSocket(this.port);
    }

    private void listen() {
        logger.info(String.format("Socket Sever is running on port `%s`", port));
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                Shard shard = new Shard(shardmanager, socket);
                loginQueue.put(shard.getIp(), shard, 10000, () -> {
                    shard.setMissingAuth(true);
                    shardmanager.getEventManager().call(new ShardDisconnectedEvent(shardmanager, shard, "Took to long to authorize."));
                    try {
                        shard.close("Authorization took too long.");
                    } catch (IOException e) {
                        logger.error("Error while closing connection to Shard", e);
                    }
                });
                shardmanager.getEventManager().call(new ShardConnectedEvent(shardmanager, shard));
            }catch (Exception e) {
                logger.error("Error in Connection Handling", e);
            }
        }
    }

    public void run() {
        logger.info("Starting Socket Server...");
        running = true;
        connectionThread.start();
    }

    public void close() throws IOException {
        running = false;
        serverSocket.close();
    }

    public static TimedMap<String, Shard> getLoginQueue() {
        return loginQueue;
    }
}
