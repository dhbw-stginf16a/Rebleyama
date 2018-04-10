package de.rebleyama.server.connection;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.net.InetSocketAddress;

public class MessageBroker {
  private int port;
  private String listenAddress;
  private boolean isRunning;
  DatagramChannel channel;
  private ByteBuffer receiveBuffer;
  private ByteBuffer sendBuffer;

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

  private void sendData() {
    // send to client(s)
  }

  public void worker() {
    // Continuously read and send
    while (this.isRunning){
      readData();
      sendData();
    }
  }

  public void setRunning(boolean newValue) {
    this.isRunning = newValue;
  }
}
