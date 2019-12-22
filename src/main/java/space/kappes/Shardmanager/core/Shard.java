package space.kappes.Shardmanager.core;

import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.kappes.Shardmanager.event.impl.ShardMessageReceivedEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Shard {

    private final Logger logger;
    private final ShardManager shardManager;
    private final Socket socket;
    private final PrintWriter outputWriter;
    private final Scanner inputScanner;
    private final Thread listenThread;
    private final String ip;

    public Shard(ShardManager shardManager, Socket socket) throws IOException {
        this.shardManager = shardManager;
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.outputWriter = new PrintWriter(socket.getOutputStream());
        this.inputScanner = new Scanner(socket.getInputStream());
        this.listenThread = new Thread(this::listen);
        this.listenThread.start();
    }

    public void listen() {
        while (socket.isConnected() && !socket.isClosed()) {
            if (inputScanner.hasNextLine()) {
                String msg = inputScanner.nextLine();
                logger.debug(String.format("[ShardMessage] %s(%s) -> Message %s", socket.getInetAddress().getHostName(), getIp(), msg));
                shardManager.getEventManager().call(new ShardMessageReceivedEvent(shardManager, this, msg));
            }
        }
    }

    public void sendCommand(String invoke, JSONObject jsonObject) {
        if (socket.isConnected()) {
            outputWriter.println(String.format("%s %s", invoke, jsonObject.toString()));
            outputWriter.flush();
        }
    }

    public void sendCommand(String invoke) {
        if (socket.isConnected()) {
            outputWriter.println(String.format("%s %s", invoke, new JSONObject().toString()));
            outputWriter.flush();
        }
    }

    public void close(String reason) throws IOException {
        sendCommand("connection_closed", new JSONObject().put("reason", reason));
        logger.debug(String.format("[ShardClose] %s(%s) -> Reason: %s",socket.getInetAddress().getHostName(), socket.getInetAddress().getHostAddress(), reason));
        inputScanner.close();
        outputWriter.close();
        socket.close();
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getIp() {
        return ip;
    }
}
