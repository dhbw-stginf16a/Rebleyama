package de.rebleyama.lib.net.message;

import de.rebleyama.lib.gamestate.GameStateUpdate;

import java.util.LinkedList;

/**
 * Represents a game state object message to be sent to the client by the server.
 */
public class GamestateUpdateMessage extends Message {
    private LinkedList<GameStateUpdate> stateUpdate;


    /**
     * Creates a gamestate update message for the client with the given id
     * @param clientId the ID of the client to receive the update, 0 means that it should be sent to all clients
     * @param updates The GameStateUpdate Object with a list of objects to be applied to the gamestate
     */
    public GamestateUpdateMessage(byte clientId, LinkedList<GameStateUpdate> updates) {
        this.msgType = MessageType.GAMESTATEUPDATE;
        this.clientID = clientId;
        this.stateUpdate = updates;
    }
}
