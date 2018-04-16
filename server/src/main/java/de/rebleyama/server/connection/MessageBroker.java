package de.rebleyama.server.connection;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.net.InetSocketAddress;
import java.util.Random;

import de.rebleyama.lib.BytesUtil;
import de.rebleyama.lib.connection.message.*;

/**
 * The message broker handling the communication with the client
 * by writing received data into the according queues and sending
 * out data from the input queue.
 */
public class MessageBroker implements Runnable {
    private int port;
    private String listenAddress;
    private boolean isRunning;
    private DatagramChannel channel;
    private ByteBuffer receiveBuffer;
    private ByteBuffer sendBuffer;

    /**
     * Initializes a message broker.
     * @param port The Port to listen on
     * @param listenAddress The ip to bind to.
     */
    public MessageBroker(int port, String listenAddress) {
        this.port = port;
        this.listenAddress = listenAddress;

        // TODO: Intialize Datagram socket.
        try {
            this.channel = DatagramChannel.open();
            this.channel.socket().bind(new InetSocketAddress(this.port));
            this.channel.configureBlocking(false);
        } catch (Exception e) {
            // Dont care
            System.err.println(e);
        }

        // Create a byte buffer to receive data
        // 65535 is the max size of a udp package
        receiveBuffer = ByteBuffer.allocate(65535);
        sendBuffer = ByteBuffer.allocate(65535);
    }

    /**
     * Initializes a message broker listening on localhost
     * @param port The Port to listen on
     */
    public MessageBroker(int port) {
        this(port, "127.0.0.1");
    }

    /**
     * Generates a new client ID and spawns a client worker
     * @return new client ID
     */
    private byte initializeNewClient() {
        Random rand = new Random();
        int clientId = rand.nextInt(253) + 1;

        // Spawn worker process here

        return (byte) clientId;
    }

    /**
     * Reads data from the datagram buffer and writes them into the according queue
     */
    private void readData() {
        // Read from channel
        try {
            if (this.channel.read(this.receiveBuffer) > 0) {
                // Set to read
                this.receiveBuffer.flip();

                switch (MessageType.toMessageType((this.receiveBuffer.get(0)))) {
                    case HANDSHAKE:
                        HandshakeMessage clientHandshakeMessage = (HandshakeMessage) BytesUtil.toObject(this.receiveBuffer.array());
                        HandshakeMessage serverHandshakeMessage = new HandshakeMessage(initializeNewClient());
                        // TODO write response to send queue
                        break;

                    case HEARTBEAT:
                        HeartbeatMessage clientHeartbeatMessage = (HeartbeatMessage) BytesUtil.toObject(this.receiveBuffer.array());

                        // TODO: REPLACE with actual serialization of the game state object
                        HeartbeatMessage serverHeartbeatMeassage = new HeartbeatMessage(clientHeartbeatMessage.getClientId(), "this could be your commercial");
                        // TODO: write response to send queue
                        break;
                    default:
                        // Not yet implemented
                        // Do nothing
                }

                // Set to write again
                receiveBuffer.flip();
            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    /**
     * Processes the send queue and writes them into the datagram channel
     */
    private void sendData() {
        // send to client(s)
    }

    /**
     * Function to toggle the worker
     * @param newStatus The new status of the worker (isRunning)
     */
    public void setRunning(boolean newStatus) {
        this.isRunning = newStatus;
    }

    /**
     * Continuously processes data to be received and sent.
     */
    @Override
    public void run() {
        while (this.isRunning){
            readData();
            sendData();
        }
    }
}
