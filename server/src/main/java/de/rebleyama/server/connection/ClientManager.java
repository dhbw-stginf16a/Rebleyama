package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameStateUpdate;

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
    private BlockingQueue<GameStateUpdate> clientQueue = new LinkedBlockingQueue<>();
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());

    /**
     * Creates a client manager for the client with the given ID.
     * @param clientId The client ID this thread should take care of.
     */
    public ClientManager(byte clientId, GameManager gameManager) {
        this.gameManager = gameManager;
        this.clientId = clientId;
        Log.setup();
    }

    /**
     * Allows the connection manager to add work packages to the client managers queue
     * @param gameStateUpdate And update to be processed by the client manager
     * @return Success of the operation
     */
    public boolean addToQueue(GameStateUpdate gameStateUpdate) {
        return this.clientQueue.add(gameStateUpdate);
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
            GameStateUpdate update;
            try {
                update = this.clientQueue.take();
                if ( update != null ) {
                    // do something
                    log.info("Got work package");
                } else {
                    // The queue was empty and the timeout hit
                    log.fine("Queue appears to be empty, listening again");
                }

            } catch (InterruptedException e) {
                log.warning("An interrupt exception occurred while waiting for updates on the queue");
                log.warning(e.getMessage());
            }
         }
         log.info("Terminating client manager.");
    }
}
