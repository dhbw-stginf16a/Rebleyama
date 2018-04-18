package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameStateUpdate;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Implements the client manager handling connections for each client.
 */
public class ClientManager implements Runnable {
    private byte clientId;
    private boolean clientAlive;
    private BlockingQueue<GameStateUpdate> clientQueue = new LinkedBlockingQueue<>();
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());

    /**
     * Creates a client manager for the client with the given ID.
     * @param clientId The client ID this thread should take care of.
     */
    ClientManager(byte clientId) {
        this.clientId = clientId;
        this.clientAlive = true;
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
     * Starts the thread to watch and process the task queue
     */
    @Override
    public void run() {
        // read from queue
        while (this.clientAlive) {
            GameStateUpdate update;
            try {
                update = this.clientQueue.poll(3, TimeUnit.SECONDS);
                if ( update != null) {
                    this.clientQueue.remove(update);
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

    /**
     * Orders the client to stop processing the queue so it can be terminated safely.
     */
    public void stop() {
        this.clientAlive = false;
        log.info("Requested termination of client manager with id " + clientId);
    }
}
