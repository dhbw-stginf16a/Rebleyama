package de.rebleyama.lib.connection;

public class HeartbeatMessage extends Message {
    private String gameStateHash;

    public HeartbeatMessage(byte clientID, String gameStateHash){
        this.msgType = MessageType.HEARTBEAT;
        this.clientID = clientID;
        this.gameStateHash = gameStateHash;
    }
}
