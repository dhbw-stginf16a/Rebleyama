package de.rebleyama.client.connection;

import de.rebleyama.client.net.ClientConnector;
import de.rebleyama.lib.net.message.HandshakeMessage;
import de.rebleyama.lib.net.message.HeartbeatMessage;
import de.rebleyama.lib.net.message.Message;

public class ServerConnection extends Thread {

    private String serverAddress;
    private int serverPort;
    private ClientConnector connector;
    private boolean running;
    private byte clientID;
    private long lastHeartbeatTime;

    public ServerConnection(String serveraddress, int port){
        this.serverAddress = serveraddress;
        this.serverPort = port;
        this.connector = new ClientConnector(serverAddress, port);
        this.clientID = 0;
    }

    public void connect(){
        this.lastHeartbeatTime = System.nanoTime();
        this.connector.connect();
        this.running = true;
        this.start();
        this.connector.send(new HandshakeMessage("Arthur Legend"));
    }

    public void disconnect(){
        this.connector.disconnect();
        this.running = false;
        this.interrupt();
    }

    private void disconnected(){

    }

    @Override
    public void run() {
        while(this.running){
            // Recieve Messages
            Message message = connector.receive();
            if(message != null){
                System.out.println(message.getMessageType().name());
                switch(message.getMessageType()){
                    case HANDSHAKE:
                        this.clientID = message.getClientId();
                        System.out.println("Got Client ID: " + this.clientID);
                        break;
                    case HEARTBEAT:
                        HeartbeatMessage hMessage = (HeartbeatMessage) message;
                        System.out.println("Handshake Hash: " + hMessage.getGameStateHash());
                        break;
                    default:
                        break;
                }
            }
            message = null;

            //Heartbeat
            if((System.nanoTime() - this.lastHeartbeatTime) > ClientConnector.HEARTBEAT_INTERVAL_NANOSECONDS){
                System.out.println("Sending Heartbeat");
                connector.send(new HeartbeatMessage(this.clientID, "Some Hash in the Future"));
                this.lastHeartbeatTime = System.nanoTime();
            }

        }
    }
}