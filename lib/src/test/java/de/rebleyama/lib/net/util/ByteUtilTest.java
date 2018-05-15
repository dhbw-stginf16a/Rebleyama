package de.rebleyama.lib.net.util;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.rebleyama.lib.net.message.HandshakeMessage;
import de.rebleyama.lib.net.message.HeartbeatMessage;
import de.rebleyama.lib.net.message.Message;
import de.rebleyama.lib.net.message.MessageType;

class ByteUtilTest {

    @Test
    void messageSerializationTest_HandShakeMessage() throws IOException, ClassNotFoundException {
        HandshakeMessage message = new HandshakeMessage();

        byte[] data = ByteUtil.toByteArray(message);


        Message result = (Message)ByteUtil.toObject(data);

        assertSame(MessageType.HANDSHAKE, result.getMessageType());
        assertTrue(result instanceof HandshakeMessage);
    }

    @Test
    void messageSerializationTest_HeartbeatMessage() throws IOException, ClassNotFoundException {
        HeartbeatMessage message = new HeartbeatMessage((byte)101, "Test");

        byte[] data = ByteUtil.toByteArray(message);


        Message result = (Message)ByteUtil.toObject(data);

        assertSame(MessageType.HEARTBEAT, result.getMessageType());
        assertTrue(result instanceof HeartbeatMessage);
    }


}
