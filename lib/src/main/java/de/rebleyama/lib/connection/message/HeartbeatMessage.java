package de.rebleyama.lib.connection.message;

/**
 * Defines a heartbeat message between client and server.
 */
public class HeartbeatMessage extends Message {
    private String gameStateHash;

    /**
     * Creates a heartbeat message
     * @param clientID The ID of the client to be identified by the server.
     * @param gameStateHash The hash of the current game state to ensure client and server are in sync
     */
    public HeartbeatMessage(byte clientID, String gameStateHash){
        this.msgType = MessageType.HEARTBEAT;
        this.clientID = clientID;
        this.gameStateHash = gameStateHash;
    }
}
