package de.rebleyama.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.rebleyama.client.RebleyamaClient;
import de.rebleyama.client.connection.ServerConnection;
import de.rebleyama.client.net.ClientConnector;
import de.rebleyama.lib.net.message.HandshakeMessage;
import de.rebleyama.lib.net.message.Message;

public class DesktopLauncher {

	private DesktopLauncher() {
		
	}

	public static void main (String[] arg) {
		// LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// config.title = "Rebleyama";
		// config.width = 1280;
		// config.height = 720;
		// new LwjglApplication(new RebleyamaClient(), config);

		ClientConnector connector = new ClientConnector("127.0.0.1", 21012);
		connector.connect();
		connector.send(new HandshakeMessage());
		boolean waiting = true;

		while(waiting){
			Message result = connector.receive();
			if(result != null){
				waiting = false;
				System.out.println("Client id is " + result.getClientId());
			}
		}
		connector.disconnect();

		// DatagramChannel channel;
		// try {
		// 	channel = DatagramChannel.open();
		// 	channel.socket().bind(new InetSocketAddress(9999));
		// 	String newData = "New String to write to file..."
        //             + System.currentTimeMillis();
    
		// 	ByteBuffer buf = ByteBuffer.allocate(48);
		// 	buf.clear();
		// 	buf.put(newData.getBytes());
		// 	buf.flip();

		// 	int bytesSent = channel.send(buf, new InetSocketAddress("192.168.3.100", 80));
		// 	System.out.println("Bytes Sent " + bytesSent);
		// } catch (IOException e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }
	}
}
