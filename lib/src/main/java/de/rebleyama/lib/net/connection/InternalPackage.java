package de.rebleyama.lib.net.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.rebleyama.lib.net.message.Message;
import de.rebleyama.lib.net.util.ByteUtil;
import de.rebleyama.lib.net.util.NetUtil;

// @Deprecated
// public class Package {
//     private Message message;
//     private InetSocketAddress socketAddress;

//     public Package(Message message, InetSocketAddress socketAddress) {
//         this.socketAddress = socketAddress;
//         this.message = message;
//     }

//     /**
//      * @return the message
//      */
//     public Message getMessage() {
//         return message;
//     }

//     /**
//      * @return the socketAddress
//      */
//     public InetSocketAddress getSocketAddress() {
//         return socketAddress;
//     }
// }

public class InternalPackage {
    private List<Frame> frameList;
    private Message message;
    private InetSocketAddress socketAddress;

    // InternalPackage(Package origin) {
    //     this.pack = origin;
    //     this.message = origin.getMessage();
    //     try {
    //         this.frameList = segmentation(this);
    //     } catch (IOException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    // }

    public InternalPackage(Message message){
        this.message = message;
        try {
            this.frameList = segmentation(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    InternalPackage(Frame[] frames, InetSocketAddress socketAddress){
        this.frameList = Arrays.asList(frames);
        this.socketAddress = socketAddress;
        try {
        this.message = desegmentation(this.frameList);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public Frame getFrame(short seq) {
        return frameList.get(seq);
    }

    public List<Frame> getFrameList() {
        return frameList;
    }

    public short getPacketId() {
        return frameList.get(0).getFrameInfo().getId();
    }

    public Message getMessage() {
        return message;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    private static List<Frame> segmentation(InternalPackage internalPackage) throws IOException {
        byte[] data = ByteUtil.toByteArray(internalPackage.getMessage());

        byte[][] chunkedData = NetUtil.chunkArray(data, Frame.PAYLOAD_MAX_SIZE);

        List<Frame> result = new ArrayList<Frame>(chunkedData.length);
        FrameInfo frameInfo = FrameInfo.generateNew((short) (chunkedData.length - 1), FrameType.MESSAGE);
        for (byte[] payload : chunkedData) {
            result.add(new Frame(frameInfo, payload));
            frameInfo = FrameInfo.next(frameInfo);
        }

        return result;
    }

    private static Message desegmentation(List<Frame> frames) throws ClassNotFoundException, IOException {

        byte[][] chunkedData = new byte[frames.size()][];

        for(int i = 0; i < frames.size();i++){
            chunkedData[i] = frames.get(i).getPayload();
        }

        byte[] messageData = NetUtil.mergeArray(chunkedData);
        return (Message) ByteUtil.toObject(messageData);
    }
}