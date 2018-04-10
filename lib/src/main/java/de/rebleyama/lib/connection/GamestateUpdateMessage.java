package de.rebleyama.lib.connection;

public class GamestateUpdateMessage extends Message {
    private GameStateUpdate stateUpdate;

    public GamestateUpdateMessage(byte clientId, GameStateUpdate updates) {
        this.msgType = MessageType.GAMESTATEUPDATE;
        this.clientID = clientId;
        this.stateUpdate = updates;
    }
}
