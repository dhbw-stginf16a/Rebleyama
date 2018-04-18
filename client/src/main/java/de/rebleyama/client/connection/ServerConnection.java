package de.rebleyama.client.connection;

import java.net.URL;

import de.rebleyama.lib.connection.message.Message;

public class ServerConnection {

    private String serverAddress;
    private int serverPort;

    private NetworkThread networkThread;

    public ServerConnection(String serveraddress, int port){
        this.serverAddress = serveraddress;
        this.serverPort = port;
    }

    public void connect(){
        this.networkThread = new NetworkThread(this.serverAddress, this.serverPort);
        this.networkThread.start();
    }

    public void disconnect(){
        // Stop Connection
    }

    public boolean isConnected(){
        return this.networkThread.isConnected();
    }

    public Message getNextMessage(){
        return null;
    }
}