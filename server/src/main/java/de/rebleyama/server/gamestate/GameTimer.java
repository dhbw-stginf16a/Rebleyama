package de.rebleyama.server.gamestate;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.game.Tile;
import de.rebleyama.lib.gamestate.GameState;
import de.rebleyama.lib.gamestate.GameStateUpdate;
import de.rebleyama.server.connection.ClientManager;

import java.util.TimerTask;
import java.util.logging.Logger;

public class GameTimer extends TimerTask {
    private GameManager gameManager;
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());


    GameTimer(GameManager gameManager) {
        this.gameManager = gameManager;
        Log.setup();
    }

    /**
     * Iterate over the current gamestate and calculate things
     * such as new resources.
     * @return The game state update to be applied.
     */
    private GameStateUpdate genUpdateOnTick() {
        GameStateUpdate tickUpdate = new GameStateUpdate();
        GameState gameState = this.gameManager.getGameState();

        return tickUpdate;
    }

    @Override
    public void run() {
        log.info("Calculating updates");
        GameStateUpdate newUpdate;
        newUpdate = genUpdateOnTick();
        this.gameManager.addToGameStateUpdateQueue(newUpdate);
    }
}
