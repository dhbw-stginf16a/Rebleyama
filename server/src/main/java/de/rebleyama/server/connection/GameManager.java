package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameState;
import de.rebleyama.lib.gamestate.GameStateUpdate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class GameManager extends Thread {
    private GameState gameState;
    private boolean running;
    private BlockingQueue<GameStateUpdate> gameStateUpdates = new LinkedBlockingQueue<>();
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());


    /**
     * Creates a new game manager with an already initialized game state
     * e.g. from a savepoint.
     * @param gameState An already initialized game state object.
     */
    public GameManager(GameState gameState) {
        this.gameState = gameState;
        Log.setup();
        log.info("Created new game manager");
    }

    /**
     * Creates a game manager with a blank game state
     */
    public GameManager() {
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

    /**
     * Returns the hash of the current gamestate
     * @return Hash of the current game state
     */
    public String getGameStateHash() {
        return this.gameState.getHash();
    }

    /**
     * Returns the full current game state object.
     * @return The current game state object.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    public void begin() {
        this.running = true;
        log.info("Starting game manager");
        this.start();
    }


    public void end() {
        this.running = false;
        log.info("Ordering game manager to terminate.");
        this.interrupt();
    }


    @Override
    public void run() {
        log.info("Started game manager thread.");
        while (this.running) {
            // Do something
        }
        log.info("Shutting down game manager.");

    }
}
