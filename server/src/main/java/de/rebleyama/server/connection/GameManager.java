package de.rebleyama.server.connection;

import de.rebleyama.lib.gamestate.GameState;
import de.rebleyama.lib.gamestate.GameStateUpdate;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameManager implements Runnable {
    private GameState GameState;
    private boolean running;
    private BlockingQueue<GameStateUpdate> gameStateUpdates = new LinkedBlockingQueue<>();


    /**
     * Creates a new game manager with an already initialized game state
     * e.g. from a savepoint.
     * @param gameState An already initialized game state object.
     */
    GameManager(GameState gameState) {
        this.GameState = gameState;
        this.running = true;
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
        while (this.running) {
            // Do something
        }

    }

    public void stop() {
        this.running = false;
    }
}
