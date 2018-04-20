package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameStateUpdate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

import static org.junit.Assert.assertTrue;

public class ClientManagerTest {
    ClientManager clientManager;
    GameManager gameManager;

    @Before
    public void startClientManager() {
        Log.setLogLevel(Level.FINEST);
        byte clientId = (byte) 1;
        this.gameManager = gameManager;
        this.clientManager = new ClientManager(clientId, gameManager);
        this.clientManager.begin();
    }

    @Test
    public void startAndStopClientManager() {
        GameStateUpdate gameStateUpdate = new GameStateUpdate();
        assertTrue("Client Manager queue accepts input", clientManager.addToQueue(gameStateUpdate));
    }

    @After
    public void stopClientManager() {
        clientManager.end();
    }


}