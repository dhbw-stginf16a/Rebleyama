package de.rebleyama.server.net;

import de.rebleyama.lib.Log;
import de.rebleyama.lib.net.message.HeartbeatMessage;
import de.rebleyama.lib.net.message.Message;
import de.rebleyama.lib.net.message.ServerInstructionMessage;
import de.rebleyama.server.connection.ClientManager;
import de.rebleyama.server.connection.GameManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * The message broker handling the communication with the client
 * by writing received data into the according queues and sending
 * out data from the input queue.
 */
public class MessageManager extends Thread implements ClientIdCreator {
    private int port;
    private String listenAddress;
    private boolean running;
    private ServerConnector connector;
    private BlockingQueue<Message> sendQueue;
    private GameManager gameManager;
    private Map<Byte, ClientManager> clientMap;
    private static final Logger log = Logger.getLogger(ClientManager.class.getName());

    /**
     * Initializes a message broker.
     * @param port The Port to listen on
     * @param listenAddress The ip to bind to.
     */
    public MessageManager(int port, String listenAddress) {
        this.port = port;
        this.listenAddress = listenAddress;
        this.connector = new ServerConnector(listenAddress, port, this);
        this.clientMap = new HashMap<>();
        this.sendQueue = new LinkedBlockingQueue<>();
        this.gameManager = new GameManager();
        gameManager.begin();
        this.running = false;
        Log.setup();
    }

    /**
     * Initializes a message broker listening on localhost
     * @param port The Port to listen on
     */
    public MessageManager(int port) {
        this(port, "127.0.0.1");
    }

    /**
     * Reads data from the datagram buffer and writes them into the according queue
     */
    private void manage() {
        // Read from channel

        Message message = connector.receive();
        if(message != null){
            switch(message.getMessageType()) {
                case HANDSHAKE:
                        connector.send(message);
                    break;
                case HEARTBEAT:
                    connector.send(new HeartbeatMessage(message.getClientId(), gameManager.getGameStateHash()));
                    break;
                case GAMESTATEREQUEST:
                    // Typecast to game state request and let the client manager do the checks
                    clientMap.get(message.getClientId()).addToQueue(message);
                    break;
                case SERVERINSTRUCTION:
                    ServerInstructionMessage serverInstructionMessage = (ServerInstructionMessage) message;
                    // TODO: Include some kind of permission checking.
                    switch (serverInstructionMessage.getServerInstruction()) {
                        case SHUTDOWN:
                            log.info("Received shutdown message. Shutting down.");
                            this.end();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    // The received messages shouldn't be sent by a client.
                    // Ignore them
                    log.info("Received non-client message. Ignoring.");
            }
        }

        try {
            Message sendMessage = sendQueue.take();
            if ( sendMessage != null) {
                connector.send(sendMessage);
            }
        } catch (InterruptedException e) {
            log.warning("Send queue take failed");
            log.warning(e.getMessage());
        }

    }


    /**
     * Adds a message to the send queue.
     * @param message The message to be sent
     * @return Succes of the add operation
     */
    public boolean addToSendQueue(Message message) {
        return this.sendQueue.add(message);
    }

    /**
     * Bootstraps the message manager and starts the thread
     */
    public void begin(){
        this.running = true;
        this.connector.connect();
        this.start();
    }

    /**
     * Gracefully terminates all server actions
     */
    public void end() {
        this.gameManager.end();
        this.clientMap.forEach((clientId, clientManager) -> {
            log.info("Requesting termination of client manager " + clientId);
            clientManager.end();
        });
        this.running = false;
        this.interrupt();
        this.connector.disconnect();
    }

    /**
     * Continuously processes data to be received and sent.
     */
    @Override
    public void run() {
        while (this.running){
            manage();
        }
    }

    /**
     * Registers a new client by assigning a client id and creating a worker thread of the client.
     * @return The id of the new client
     */
	@Override
	public byte registerClient() {
		Random rand = new Random();
        int clientIntId = rand.nextInt(253) + 1;
        byte clientId = (byte) clientIntId;

        // create a new client worker thread
        this.clientMap.put(clientId, new ClientManager(clientId, this.gameManager));
        this.clientMap.get(clientId).begin();

        return clientId;
	}
}
