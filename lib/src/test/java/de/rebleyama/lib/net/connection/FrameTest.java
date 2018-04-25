package de.rebleyama.lib.net.connection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FrameTest{

    @Test
    public void TestFrameInfoSerialization(){
        FrameInfo info = FrameInfo.generateNew((short)10, FrameType.MESSAGE);

        byte[] serializedData = FrameInfo.serialize(info);

        FrameInfo deserializedInfo = FrameInfo.deserialize(serializedData);

        assertEquals(info, deserializedInfo);
    }

    @Test
    void TestFrameSerialization(){
        byte[] payload = new byte[]{1,2,3,4,5,6,7,8,9,0};
        FrameInfo info = FrameInfo.generateNew((short)10, FrameType.MESSAGE);
        Frame frame = new Frame(info, payload);
        
        byte[] data = Frame.serialize(frame);

        Frame deserializedFrame = Frame.deserialize(data);

        assertEquals(frame, deserializedFrame);
        assertArrayEquals(payload, deserializedFrame.getPayload());
    }

    @Test
    void TestFrameInfoNext(){
        FrameInfo frameInfo = FrameInfo.generateNew((short)10, FrameType.MESSAGE);
        
        FrameInfo nextFrame = FrameInfo.next(frameInfo);

        assertEquals(1, nextFrame.getSeq());
    }

    @Test
    void TestFrameInfoGeneration(){
        FrameInfo frameInfo = FrameInfo.generateNew((short)10, FrameType.MESSAGE);
        FrameInfo frameInfo2 = FrameInfo.generateNew((short)10, FrameType.FRAME_REQUEST);
        
        assertEquals(frameInfo.getSeq(), 0);
        assertEquals(frameInfo.getLastseq(), (byte)10);
        assertEquals(frameInfo.getType(), FrameType.MESSAGE);

        assertEquals(frameInfo2.getId(), frameInfo.getId()+1);
        assertEquals(frameInfo2.getSeq(), 0);
        assertEquals(frameInfo2.getLastseq(), (byte)10);
        assertEquals(frameInfo2.getType(), FrameType.FRAME_REQUEST);
        
    }
}