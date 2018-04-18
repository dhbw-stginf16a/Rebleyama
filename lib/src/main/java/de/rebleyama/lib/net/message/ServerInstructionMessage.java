package de.rebleyama.lib.net.message;

/**
 * Allows a client to send an instruction to the server
 */
public class ServerInstructionMessage extends Message {
    ServerInstruction serverInstruction;
    /**
     * Creates a server instruction message
     *  @param serverInstruction The instruction to be sent to the server
     *  @param clientId The ID of the client to be identified by the server
     */
    public ServerInstructionMessage(ServerInstruction serverInstruction, byte clientId) {
        this.msgType = MessageType.SERVERINSTRUCTION;
        this.clientID = clientId;
        this.serverInstruction = serverInstruction;
    }

}
