package de.rebleyama.server.connection;

import de.rebleyama.lib.gamestate.GameStateUpdate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameManagerTest {
    private GameManager gameManager;

    @Before
    public void startGameManager() {
        this.gameManager = new GameManager();
        this.gameManager.begin();
    }

    @Test
    public void addToGameStateUpdateQueue() {
        assertTrue("Add dummy update object to the game state", this.gameManager.addToGameStateUpdateQueue(new GameStateUpdate()));
    }

    @After
    public void stopGameManager() {
        this.gameManager.end();
    }
}