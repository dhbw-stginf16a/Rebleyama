package de.rebleyama.lib.net.connection;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Frame {
    private FrameInfo frameInfo;
    private byte[] payload;

    static final int FRAME_MAX_SIZE = 512;
    static final int PAYLOAD_MAX_SIZE = FRAME_MAX_SIZE - FrameInfo.FRAME_INFO_SIZE;

    Frame(FrameInfo frameInfo, byte[] payload){
        this.frameInfo = frameInfo;
        this.payload = payload;
    }

    FrameInfo getFrameInfo(){
        return this.frameInfo;
    }

    byte[] getPayload(){
        return this.payload;
    }

    static Frame deserialize(byte [] data){
        byte[] frameInfoData = new byte[FrameInfo.FRAME_INFO_SIZE];
        byte[] payload = new byte[data.length-FrameInfo.FRAME_INFO_SIZE];
        System.arraycopy(data, 0, frameInfoData, 0, FrameInfo.FRAME_INFO_SIZE);
        System.arraycopy(data, FrameInfo.FRAME_INFO_SIZE, payload, 0, data.length-FrameInfo.FRAME_INFO_SIZE);

        return new Frame(FrameInfo.deserialize(frameInfoData), payload);
    }

    static byte[] serialize(Frame frame){
        byte[] result = new byte[FrameInfo.FRAME_INFO_SIZE+frame.getPayload().length];
        System.arraycopy(FrameInfo.serialize(frame.getFrameInfo()), 0 , result, 0, FrameInfo.FRAME_INFO_SIZE);
        System.arraycopy(frame.getPayload(), 0, result, FrameInfo.FRAME_INFO_SIZE, frame.getPayload().length);

        return result;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Frame){
            Frame o = (Frame) other;
            return o.getFrameInfo().equals(this.getFrameInfo());
        }
        return super.equals(other);
    }
}

class FrameInfo {
    private short id, seq, lastseq;
    private FrameType type;

    private static short lastId = 0;
    static final int FRAME_INFO_SIZE = 7;

    private FrameInfo(short id, short seq, short lastseq, FrameType type){
        this.id = id;
        this.seq = seq;
        this.lastseq = lastseq;
        this.type = type;
    }

    private static short nextId(){
        return lastId++;
    }

    static FrameInfo deserialize(byte [] data){
        return new FrameInfo(
            (byte)(data[0] << 8 | data[1]),
            (byte)(data[2] << 8 | data[3]),
            (byte)(data[4] << 8 | data[5]),
            FrameType.getFrameType(data[6])
        );
    }

    static byte[] serialize(FrameInfo frameInfo){
        byte[] result = new byte[]{
            (byte)(frameInfo.id >> 8),
            (byte)(frameInfo.id),
            (byte)(frameInfo.seq >> 8),
            (byte)(frameInfo.seq),
            (byte)(frameInfo.lastseq >> 8),
            (byte)(frameInfo.lastseq),
            (byte)(frameInfo.type.getFlag()),  
        };
        return result;
    }

    static FrameInfo next(FrameInfo frameInfo){
        return new FrameInfo(frameInfo.id, (short)(frameInfo.seq + 1) ,frameInfo.lastseq, frameInfo.type);
    }

    static FrameInfo generateNew(short lastseq, FrameType type){
        return new FrameInfo(FrameInfo.nextId(), (short)0, lastseq, type);
    }

    /**
     * @return the id
     */
    short getId(){
        return this.id;
    }

    /**
     * @return the seq
     */
    short getSeq() {
        return this.seq;
    }

    /**
     * @return the lastseq
     */
    short getLastseq() {
        return this.lastseq;
    }

    /**
     * @return the type
     */
    FrameType getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof FrameInfo){
            FrameInfo o = (FrameInfo) other;
            return (o.id == this.id && o.seq == this.seq && o.lastseq == this.lastseq && o.type == this.type);
        }
        return super.equals(other);
    }
}

enum FrameType{
    MESSAGE,
    FRAME_REQUEST;

    byte flag;

    private FrameType(){
        this.flag = (byte)this.ordinal();
    }

    byte getFlag(){
        return flag;
    }

    private static List<FrameType> values = Collections.unmodifiableList(Arrays.asList(FrameType.values()));

    static FrameType getFrameType(byte flag) {
        return values.get(flag);
    }
}

class FrameRequest{
    Frame frame;
    InetSocketAddress socketAddress;

    //Todo Implement
}