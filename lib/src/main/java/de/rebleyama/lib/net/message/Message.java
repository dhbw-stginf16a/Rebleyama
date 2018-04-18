package de.rebleyama.lib.net.message;

import java.io.Serializable;

/**
 * A generic message building the basic frame for all other messages.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = -4605370810463332658L;
	MessageType msgType;
    byte clientID;

    /**
     * Returns the client ID
     * @return client ID
     */
    public byte getClientId() {
        return clientID;
    }

    /**
     * Returns the message type
     * @return message type
     */
    public MessageType getMessageType(){
        return msgType;
    }
}
