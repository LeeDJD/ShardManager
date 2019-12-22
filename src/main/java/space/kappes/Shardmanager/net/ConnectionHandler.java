package space.kappes.Shardmanager.net;

import space.kappes.Shardmanager.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.kappes.Shardmanager.event.EventManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {

    private final ShardManager shardmanager;
    private final Thread connectionThread;
    private final int port;
    private final ServerSocket serverSocket;
    private final Logger logger;
    private boolean running = false;

    public ConnectionHandler(ShardManager shardmanager) throws IOException {
        this.shardmanager = shardmanager;
        this.connectionThread = new Thread(this::listen);
        this.port = shardmanager.getConfigManager().getPort();
        this.serverSocket = new ServerSocket(this.port);
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    private void listen() {
        logger.info(String.format("Socket Sever is running on port `%s`", port));
        while (running) {
            try {
                Socket socket = serverSocket.accept();

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


}
