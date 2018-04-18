package de.rebleyama.lib.net.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SenderThread extends Thread{
    private BlockingQueue<InternalPackage> senderQueue;
    private Connector connector;
    private boolean stopThread;
    private ByteBuffer sendBuffer;

    SenderThread(Connector connector){
        this.connector = connector;
        this.senderQueue = new LinkedBlockingQueue<>();
        this.stopThread= true;
        this.sendBuffer = ByteBuffer.allocate(Frame.FRAME_MAX_SIZE);
    }

    @Override
    public void run() {
        while(!stopThread){
            try {
				InternalPackage currentPackage = getSenderQueue().take();
                
                List<InetSocketAddress> targets = connector.getTargets(currentPackage.getMessage());

                for(Frame frame: currentPackage.getFrameList()){
                    this.sendBuffer.put(Frame.serialize(frame));
                    this.sendBuffer.flip();
                    for (InetSocketAddress target : targets) {
                        try {
							connector.getConnectorChannel().send(this.sendBuffer, target);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    this.sendBuffer.clear();
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    BlockingQueue<InternalPackage> getSenderQueue() {
        return senderQueue;
    }

    void begin(){
        this.stopThread = false;
        this.start();
    }

    void end(){
        this.stopThread = true;
        this.interrupt();
    }
}