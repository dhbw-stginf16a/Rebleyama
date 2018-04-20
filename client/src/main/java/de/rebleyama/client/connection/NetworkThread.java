package de.rebleyama.client.connection;

import de.rebleyama.lib.net.message.HandshakeMessage;
import de.rebleyama.lib.net.message.Message;
import de.rebleyama.lib.net.util.ByteUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class NetworkThread extends Thread{

    private DatagramChannel udpChannel;
    private BlockingQueue<Message> outMessageQueue, inMessageQueue;

    private InetSocketAddress serverSocketAddress, clientSocketAddress;

    private boolean connected;
    
    private static int QUEUE_SIZE = 50;

    NetworkThread(String serveraddress, int port){
        outMessageQueue = new ArrayBlockingQueue<Message>(QUEUE_SIZE);
        inMessageQueue = new ArrayBlockingQueue<Message>(QUEUE_SIZE);

        this.serverSocketAddress = new InetSocketAddress(serveraddress, port);
        this.clientSocketAddress = new InetSocketAddress(0);
    }

    @Override
    public void run(){
        //Setup

        try {
            this.udpChannel = DatagramChannel.open();
            this.udpChannel.bind(this.clientSocketAddress);
            //this.udpChannel.connect(this.serverSocketAddress);
            this.udpChannel.configureBlocking(true);
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ByteBuffer Setup
        ByteBuffer buffer = ByteBuffer.allocate(65000);
        
        //Handshake Message
        try {
            buffer.put(ByteUtil.toByteArray(new HandshakeMessage()));
            buffer.flip();
            this.udpChannel.send(buffer, this.serverSocketAddress);

            buffer.clear();
            
            this.udpChannel.receive(buffer);
            buffer.flip();

            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            Message msg = (Message) ByteUtil.toObject(data);
            HandshakeMessage hmsg = (HandshakeMessage)msg;
            System.out.println(hmsg.getClientId());
		} catch (IOException | ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

        try {
			this.udpChannel.configureBlocking(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        while(true){
            try {
				this.udpChannel.receive(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}





        

            if(true){
                break;
            }
        }

        //Cleanup
        try{
            this.udpChannel.close();
        } catch(IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    boolean isConnected(){
        return this.connected;
    }

    BlockingQueue<Message> getInMessageQueue(){
        return this.inMessageQueue;
    }

    BlockingQueue<Message> getOutMessageQueue(){
        return this.outMessageQueue;
    }
}