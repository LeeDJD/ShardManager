package space.kappes.Shardmanager.core;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import space.kappes.Shardmanager.ShardManager;
import space.kappes.Shardmanager.event.impl.ShardDisconnectedEvent;
import space.kappes.Shardmanager.event.impl.ShardMessageReceivedEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Shard {

    private final Logger logger = Logger.getLogger(getClass());
    private final ShardManager shardManager;
    private final Socket socket;
    private final PrintWriter outputWriter;
    private final Scanner inputScanner;
    private final Thread listenThread;
    private final String ip;
    private boolean connectionAuthorized = false;
    private boolean missingAuth = false;
    private String name;
    private long ping = 0L;

    public Shard(ShardManager shardManager, Socket socket) throws IOException {
        this.shardManager = shardManager;
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
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
        if (!missingAuth)
            shardManager.getEventManager().call(new ShardDisconnectedEvent(shardManager, this));
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
        shardManager.removeShard(this);
        sendCommand("connection_closed", new JSONObject().put("reason", reason));
        logger.debug(String.format("[ShardClose] %s(%s) -> Reason: %s",socket.getInetAddress().getHostName(), socket.getInetAddress().getHostAddress(), reason));
        inputScanner.close();
        outputWriter.close();
        socket.close();
    }

    public void close() throws IOException {
        close("Not specified.");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnectionAuthorized() {
        return connectionAuthorized;
    }

    public void setConnectionAuthorized(boolean connectionAuthorized) {
        this.connectionAuthorized = connectionAuthorized;
    }

    public boolean isMissingAuth() {
        return missingAuth;
    }

    public void setMissingAuth(boolean missingAuth) {
        this.missingAuth = missingAuth;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }
}
