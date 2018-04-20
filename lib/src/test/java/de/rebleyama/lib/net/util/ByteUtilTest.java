package de.rebleyama.lib.net.util;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import de.rebleyama.lib.net.message.HandshakeMessage;
import de.rebleyama.lib.net.message.Message;
import de.rebleyama.lib.net.message.MessageType;

public class ByteUtilTest {

    @Test
    @Disabled
    public void messageSerializationTest_HandShakeMessage() throws IOException, ClassNotFoundException {
        HandshakeMessage message = new HandshakeMessage();

        byte[] data = ByteUtil.toByteArray(message);


        Message result = (Message)ByteUtil.toObject(data);

        assertTrue(MessageType.HANDSHAKE == result.getMessageType());
        assertTrue(result instanceof HandshakeMessage);
    }
}