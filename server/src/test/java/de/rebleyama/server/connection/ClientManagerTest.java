package de.rebleyama.server.connection;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.net.message.GamestateRequestMessage;
import de.rebleyama.lib.net.message.Message;
import de.rebleyama.server.gamestate.GameManager;
import de.rebleyama.server.net.MessageManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

import static org.junit.Assert.assertTrue;

public class ClientManagerTest {
    private ClientManager clientManager;
    private GameManager gameManager;
    private MessageManager messageManager;
    private byte clientId;

    @Before
    public void startClientManager() {
        Log.setLogLevel(Level.FINEST);
        this.clientId = (byte) 1;
        this.gameManager = new GameManager();
        this.messageManager = new MessageManager(21012);
        this.clientManager = new ClientManager(clientId, gameManager, messageManager);
        this.clientManager.begin();
    }

    @Test
    public void addToCMQueue() {
        Message message = new GamestateRequestMessage(this.clientId, "123456");
        assertTrue("Client Manager queue accepts input", clientManager.addToQueue(message));
    }

    @After
    public void stopClientManager() {
        clientManager.end();
    }


}