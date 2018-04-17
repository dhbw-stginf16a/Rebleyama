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
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Creates a client manager for the client with the given ID.
     * @param clientId The client ID this thread should take care of.
     */
    public ClientManager(byte clientId) {
        this.clientId = clientId;
        this.clientAlive = true;
        try {
            Log.setup();
        } catch (IOException e) {
            log.warning("Couldn't initialize file logging.");
        }
    }

    /**
     * Returns the clients worker queue to add new jobs to it.
     * @return the blocking queue for this client worker
     */
    public BlockingQueue<GameStateUpdate> getclientQueue() {
        return this.clientQueue;
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
                this.clientQueue.remove(update);
                // do something
                log.info("Got work package");

            } catch (InterruptedException e) {
                log.fine("Queue appears to be empty");
            }
         }
    }

    /**
     * Orders the client to stop processing the queue so it can be terminated safely.
     */
    public void stop() {
        this.clientAlive = false;
        log.info("Requested termination of queue processing<");
    }
}
