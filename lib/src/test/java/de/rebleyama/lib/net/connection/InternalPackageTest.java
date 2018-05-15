package de.rebleyama.lib.net.connection;

import de.rebleyama.lib.net.message.HeartbeatMessage;
import de.rebleyama.lib.net.message.MessageType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.InetSocketAddress;
import java.util.List;

public class InternalPackageTest {

    @Test
    public void TestPackageSegmentation(){
        InternalPackage internalPackage = new InternalPackage(new HeartbeatMessage((byte)0, "Hash"));

        List<Frame> frames = internalPackage.getFrameList();
        Frame[] fArray = new Frame[frames.size()];
        fArray = frames.toArray(fArray);

        InternalPackage result = new InternalPackage(fArray, new InetSocketAddress(21012));

        assertEquals(result.getMessage().getMessageType(), internalPackage.getMessage().getMessageType());
        assertEquals(result.getMessage().getMessageType(), MessageType.HEARTBEAT);
    }

}
