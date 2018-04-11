package de.rebleyama.server.connection;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.net.InetSocketAddress;
import de.rebleyama.lib.connection.message.*;

/**
 * The message broker handling the communication with the client
 * by writing received data into the according queues and sending
 * out data from the input queue.
 */
public class MessageBroker {
  private int port;
  private String listenAddress;
  private boolean isRunning;
  DatagramChannel channel;
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
            this.isRunning = true;
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
     * Reads data from the datagram buffer and writes them into the according queue
     */
    private void readData() {
        // Read from channel
        try {
            if (this.channel.read(this.receiveBuffer) > 0) {
                // Set to read
                receiveBuffer.flip();

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
     * Continuously processes data to be received and sent.
     */
    public void worker() {
        // Continuously read and send
        while (this.isRunning){
            readData();
            sendData();
        }
    }

    /**
     * Function to toggle the worker
     * @param newStatus The new status of the worker (isRunning)
     */
    public void setRunning(boolean newStatus) {
    this.isRunning = newStatus;
  }
}
