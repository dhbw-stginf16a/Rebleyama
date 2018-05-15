package de.rebleyama.lib.net.message;

/**
 * A request sent by the client to receive updates or a replacement of the game state.
 */
public class GamestateRequestMessage extends Message {
    private String gameStateHash;

    /**
     *
     * Creates a gamestate request message
     * @param clientId The id of the client for authentication on the server
     * @param gameStateHash The hash of the current game state for the server to determine an update or replacement
     */
    public  GamestateRequestMessage(byte clientId, String gameStateHash) {
        this.msgType = MessageType.GAMESTATEREQUEST;
        this.gameStateHash = gameStateHash;
        this.clientID = clientId;
    }

    /**
     * Returns the game state hash of the payload
     * @return The game state hash sent with the message
     */
    public String getGameStateHash() {
        return gameStateHash;
    }
}
