package de.rebleyama.client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.rebleyama.lib.net.connection.Connector;
import de.rebleyama.lib.net.message.Message;

public class ClientConnector extends Connector{

    private InetSocketAddress serveAddress;
    private List<InetSocketAddress> serverAddressList;

    public ClientConnector(String serverAddress, int port){
        super();
        this.serveAddress = new InetSocketAddress(serverAddress, port);
        this.serverAddressList = Collections.unmodifiableList(Arrays.asList(this.serveAddress));
    }

	@Override
	protected List<InetSocketAddress> getTargets(Message p) {
		return serverAddressList;
	}

	@Override
	protected void configureChannel() {
        try {
            this.udpChannel.configureBlocking(true);
			this.udpChannel.bind(new InetSocketAddress(0));
            this.udpChannel.connect(this.serveAddress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}