package de.rebleyama.lib.net.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceiverThread extends Thread{
    private static final int PARTIAL_PACKAGE_MAP_SIZE = 32;

	private BlockingQueue<InternalPackage> receiverQueue;
    private Map<Short, Collector> partialPackageMap;
    private Connector connector;
    private boolean stopThread;
    private ByteBuffer receiveBuffer;

    ReceiverThread(Connector connector){
        this.connector = connector;
        this.receiverQueue = new LinkedBlockingQueue<>();
        this.stopThread= true;
        this.receiveBuffer = ByteBuffer.allocate(Frame.FRAME_MAX_SIZE);
        this.partialPackageMap = new HashMap<>(ReceiverThread.PARTIAL_PACKAGE_MAP_SIZE);
    }

    @Override
    public void run() {
        while(!stopThread){
            try {
                try {
                    InetSocketAddress socketAddress = (InetSocketAddress)this.connector.getConnectorChannel().receive(this.receiveBuffer);
                    this.receiveBuffer.flip();
                    byte[] data = new byte[this.receiveBuffer.limit()];
                    this.receiveBuffer.get(data);
                    this.receiveBuffer.clear();
                    Frame frame = Frame.deserialize(data);

                    Collector collector;
                    if(!this.partialPackageMap.containsKey(frame.getFrameInfo().getId())){
                        collector = new Collector(frame.getFrameInfo().getLastseq()+1);
                        this.partialPackageMap.put(frame.getFrameInfo().getId(), collector);
                    } else {
                        collector = this.partialPackageMap.get(frame.getFrameInfo().getId());
                    }
                    collector.frames[frame.getFrameInfo().getSeq()] = frame;

                    if(collector.isComplete()){
                        InternalPackage internalPackage = new InternalPackage(collector.frames, socketAddress);
                        this.receiverQueue.put(internalPackage);

                        this.partialPackageMap.remove(collector.frames[0].getFrameInfo().getId());
                    }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                //TODO Implement Error Correction

            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public BlockingQueue<InternalPackage> getReceiverQueue() {
        return this.receiverQueue;
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


class Collector {
    Frame[] frames;
    
    Collector(int size){
        frames = new Frame[size];
    }

    boolean isComplete(){
        for(Frame frame: frames){
            if(frame == null){
                return false;
            }
        }
        return true;
    }
}