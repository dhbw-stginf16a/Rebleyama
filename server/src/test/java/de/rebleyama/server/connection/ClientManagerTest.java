package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.gamestate.GameStateUpdate;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientManagerTest {

    @Test
    public void startAndStopClientManager() throws InterruptedException {
        byte clientId = (byte) 1;
        Log.setLogLevel(Level.FINEST);

        BlockingQueue<GameStateUpdate> emptyQueue = new LinkedBlockingQueue<>();
        GameStateUpdate gameStateUpdate = new GameStateUpdate();
        ClientManager cm = new ClientManager(clientId);
        new Thread(cm).start();
        assertEquals("Client Manager returns client queue", emptyQueue.getClass(), cm.getClientQueue().getClass());
        assertTrue("Client Manager queue accepts input", cm.getClientQueue().add(gameStateUpdate));

        cm.stop();
    }


}