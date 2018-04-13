package de.rebleyama.lib.connection.message;

/**
 * Message types for communication between client and server
 * For reference please see the according wiki (https://github.com/dhbw-stginf16a/Rebleyama/wiki)
 */
public enum MessageType {
    /**
     * Client-Sever Handshake
     */
    HANDSHAKE,

    /**
     * Client-Server Heartbeat
     */
    HEARTBEAT,

    /**
     * Client request of the current game state object
     */
    GAMESTATEREQUEST,

    /**
     * Update of the game status continuously send by the server to the clients when an update occured
     */
    GAMESTATEUPDATE,

    /**
     * The whole game state object to replace the previous one since it ran out of sync
     */
    GAMESTATEREPLACE,

    /**
     * In case of a single player game this allows the client to shut down the server.
     */
    SERVERINSTRUCTION;

    private byte flag;

    MessageType() {
        this.flag = (byte) this.ordinal();
    }
}
