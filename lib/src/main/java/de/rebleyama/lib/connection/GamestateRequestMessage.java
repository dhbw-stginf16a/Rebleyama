package de.rebleyama.lib.connection;

public class GamestateRequestMessage extends Message {
    private String gameStateHash;

    public  GamestateRequestMessage(byte clientId, String gameStateHash) {
        this.msgType = MessageType.GAMESTATEREQUEST;
        this.gameStateHash = gameStateHash;
        this.clientID = clientId;
    }

}
