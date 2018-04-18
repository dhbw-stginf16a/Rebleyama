package de.rebleyama.server.net;

import java.util.Random;

import de.rebleyama.lib.net.message.Message;

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

    /**
     * Initializes a message broker.
     * @param port The Port to listen on
     * @param listenAddress The ip to bind to.
     */
    public MessageManager(int port, String listenAddress) {
        this.port = port;
        this.listenAddress = listenAddress;
        this.connector = new ServerConnector(listenAddress, port, this);
        this.running = false;
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
            switch(message.getMessageType()){
                case HANDSHAKE:
                        connector.send(message);
                    break;
                case HEARTBEAT:
                        //TODO implement this Thore
                    break;
                default:
                    // Placeholder
                    break;
            }
        }

        // TODO proactive sending
    }

    /**
     * 
     */
    public void begin(){
        this.running = true;
        this.connector.connect();
        this.start();
    }


    public void end(){
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

	@Override
	public byte registerClient() {
		Random rand = new Random();
        int clientId = rand.nextInt(253) + 1;

        // Spawn worker process here

        return (byte) clientId;
	}
}
