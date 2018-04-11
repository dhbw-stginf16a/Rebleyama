package de.rebleyama.lib.connection.message;

import de.rebleyama.lib.gamestate.GameState;

/**
 * Orders the client to replace its gamestate with the one send in this message.
 */
public class GamestateReplaceMessage extends Message {
    GameState gameState;

    /**
     * Creates a message with a new gamestate to be sent to the named client
     * @param clientId The client to be updated
     * @param gameState The game state the client should use to replace the current state with.
     */
    public GamestateReplaceMessage(byte clientId, GameState gameState) {
        this.msgType = MessageType.GAMESTATEREPLACE;
        this.clientID = clientId;
        this.gameState = gameState;
    }
}
