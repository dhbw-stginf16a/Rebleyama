package de.rebleyama.lib.net.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.List;

import de.rebleyama.lib.net.message.Message;

public abstract class Connector{


    public static long HEARTBEAT_INTERVAL_NANOSECONDS = (long)1E9;

    protected DatagramChannel udpChannel;

    protected SenderThread senderThread;
    protected ReceiverThread receiverThread;

    protected Connector(){
        this.senderThread = new SenderThread(this);
        this.receiverThread = new ReceiverThread(this);
        try {
			this.udpChannel = DatagramChannel.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void connect(){
        configureChannel();
        this.senderThread.begin();
        this.receiverThread.begin();
    }

    public void disconnect(){
        this.senderThread.end();
        this.receiverThread.end();
    }

    // Message Access
    public void send(Message message){
        this.senderThread.getSenderQueue().offer(new InternalPackage(message));
    }

    public Message receive(){
        InternalPackage internalPackage = this.receiverThread.getReceiverQueue().poll();
        if(internalPackage == null){
            return null;
        }
        return internalPackage.getMessage();
    }

    DatagramChannel getConnectorChannel(){
        return this.udpChannel;
    }

    //Abstract SenderMethodes
    protected abstract List<InetSocketAddress> getTargets(Message message);
    
    protected abstract void configureChannel();
}