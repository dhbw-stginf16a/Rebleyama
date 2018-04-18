package de.rebleyama.lib.net.message;

/**
 * Operative instructions for the server which may be sent by privileged clients.
 */
public enum ServerInstruction {
    /**
     * Terminates the server
     */
    SHUTDOWN;

    private byte flag;

    ServerInstruction() {
        this.flag = (byte) this.ordinal();
    }
}
