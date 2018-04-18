package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameState;
import de.rebleyama.lib.gamestate.GameStateUpdate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class GameManager implements Runnable {
    private GameState gameState;
    private boolean running;
    private BlockingQueue<GameStateUpdate> gameStateUpdates = new LinkedBlockingQueue<>();
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());


    /**
     * Creates a new game manager with an already initialized game state
     * e.g. from a savepoint.
     * @param gameState An already initialized game state object.
     */
    GameManager(GameState gameState) {
        this.gameState = gameState;
        this.running = true;
        Log.setup();
        log.info("Created new game manager");
    }

    /**
     * Creates a game manager with a blank game state
     */
    GameManager() {
        this(new GameState());
    }

    /**
     * Allows the client to add something to the queue of the game manager
     * @param gameStateUpdate The update to be applied to the game state
     * @return Success of the add operation
     */
    public boolean addToGameStateUpdateQueue(GameStateUpdate gameStateUpdate) {
        return this.gameStateUpdates.add(gameStateUpdate);
    }


    @Override
    public void run() {
        log.info("Started game manager thread.");
        while (this.running) {
            // Do something
        }
        log.info("Shutting down game manager.");

    }

    public void stop() {
        log.info("Ordering game manager to terminate.");
        this.running = false;
    }
}
