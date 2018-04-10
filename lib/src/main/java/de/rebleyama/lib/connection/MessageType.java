package de.rebleyama.lib.connection;

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
     * Update of the game status continuously send by the server on each updates
     */
    GAMESTATEUPDATE,

    /**
     * The whole gamestate object to replace the previous one since it ran out of sync
     */
    GAMESTATEREPLACE;

    private byte flag;

    MessageType() {
        this.flag = (byte) this.ordinal();
    }
}
