package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameStateUpdate;
import de.rebleyama.lib.net.message.*;
import de.rebleyama.server.gamestate.GameManager;
import de.rebleyama.server.net.MessageManager;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Implements the client manager handling connections for each client.
 */
public class ClientManager extends Thread {
    private byte clientId;
    private boolean running;
    private GameManager gameManager;
    private MessageManager messageManager;
    private BlockingQueue<Message> clientQueue;
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());

    /**
     * Creates a client manager for the client with the given ID.
     * @param clientId The client ID this thread should take care of.
     * @param gameManager The game manager to get access to its queue
     * @param messageManager The message manager to be able to send data
     */
    public ClientManager(byte clientId, GameManager gameManager, MessageManager messageManager) {
        this.gameManager = gameManager;
        this.messageManager = messageManager;
        this.clientId = clientId;
        this.clientQueue = new LinkedBlockingQueue<>();
        Log.setup();
    }

    /**
     * Allows the connection manager to add work packages to the client managers queue
     * @param message A message to be processed by the client manager
     * @return Success of the operation
     */
    public boolean addToQueue(Message message) {
        return this.clientQueue.add(message);
    }

    /**
     * Initializes the client manager as thread.
     */
    public void begin() {
        this.running = true;
        log.info("Starting game manager");
        this.start();
    }

    /**
     * Orders the client to stop processing the queue so it can be terminated safely.
     */
    public void end() {
        this.running = false;
        log.info("Requested termination of client manager with id " + clientId);
        this.interrupt();
    }

    /**
     * Starts the thread to watch and process the task queue.
     */
    @Override
    public void run() {
        // read from queue
        while (this.running) {
            Message message;
            message = this.clientQueue.poll();
            if ( message != null ) {
                log.info("Got work package");
                if (message.getMessageType()  == MessageType.GAMESTATEREQUEST) {
                    processGamestateRequest((GamestateRequestMessage) message);
                }
            } else {
                // The queue was empty and the timeout hit
                log.fine("Queue appears to be empty, listening again");
            }
         }
         log.info("Terminating client manager.");
    }

    /**
     * Generates either a game state update or a replace message depending on the offset.
     * @param gamestateRequest The message as received from the client.
     */
    private void processGamestateRequest(GamestateRequestMessage gamestateRequest) {
        log.info("Got game state request message");
        if (gameManager.getUpdateCache().get(gamestateRequest.getGameStateHash()) != null) {
            // Get all updates newer than the given game state and pass them to the message manager
            LinkedList<GameStateUpdate> updates = new LinkedList<>();
            gameManager.getUpdateCache().forEach((hash, updateObject) -> updates.add(updateObject));
            this.messageManager.addToSendQueue(new GamestateUpdateMessage(this.clientId, updates));
        } else {
           this.messageManager.addToSendQueue(new GamestateReplaceMessage(this.clientId, this.gameManager.getGameState()));
        }
    }
}
