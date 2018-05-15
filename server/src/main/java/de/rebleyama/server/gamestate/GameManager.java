package de.rebleyama.server.gamestate;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameState;
import de.rebleyama.lib.gamestate.GameStateUpdate;
import de.rebleyama.server.connection.ClientManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class GameManager extends Thread {
    private GameState gameState;
    private Timer gameTimer;
    private boolean running;
    private BlockingQueue<GameStateUpdate> gameStateUpdates;
    private static final int CACHE_SIZE = 4;
    private Map<String, GameStateUpdate> updateCache;
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());


    /**
     * Creates a new game manager with an already initialized game state
     * e.g. from a savepoint.
     * @param gameState An already initialized game state object.
     */
    public GameManager(GameState gameState) {
        this.gameState = gameState;
        this.gameTimer = new Timer();

        // Make the updateCache a ringbuffer so it won't grow larger than updateCacheSize
        this.updateCache = new LinkedHashMap<String, GameStateUpdate>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                return size() > CACHE_SIZE;
            }
        };
        this.gameStateUpdates = new LinkedBlockingQueue<>();
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
     * Returns the hash of the current game state
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
        // Ensure the game manager is up before starting the timer
    }


    public void end() {
        this.gameTimer.cancel();
        this.running = false;
        log.info("Ordering game manager to terminate.");
        this.interrupt();
    }


    @Override
    public void run() {
        // Start the timer to produce regular gamestate updates with every tick
        this.gameTimer.schedule(new GameTimer(this), 0, 3000);
        log.info("Started game manager thread.");
        while (this.running) {
            GameStateUpdate update;
            update = this.gameStateUpdates.poll();
            if (update != null) {
                this.gameState.applyUpdate(update);
                this.updateCache.put(gameState.getHash(), update);
            }
        }
        log.info("Shutting down game manager.");
    }

    /**
     * Returns the cache of updates
     * @return The cached updates
     */
    public Map<String, GameStateUpdate> getUpdateCache() {
        return updateCache;
    }
}
