package de.rebleyama.lib.connection.message;

/**
 * A generic message building the basic frame for all other messages.
 */
public abstract class Message {
    MessageType msgType;
    byte clientID;
}
