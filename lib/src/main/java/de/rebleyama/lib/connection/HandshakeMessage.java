package de.rebleyama.lib.connection;

public class HandshakeMessage extends Message {
    private String hello = "HELLO";

    public HandshakeMessage(byte clientID) {
        this.msgType = MessageType.HANDSHAKE;
        this.clientID = clientID;
    }

    public HandshakeMessage() {
        this((byte) 0);
    }
}
