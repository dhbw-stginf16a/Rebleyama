package de.rebleyama.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.rebleyama.lib.net.connection.Connector;
import de.rebleyama.lib.net.message.HandshakeMessage;
import de.rebleyama.lib.net.message.Message;
import de.rebleyama.lib.net.connection.InternalPackage;

public class ServerConnector extends Connector{

    private InetSocketAddress serveAddress;
    private Map<Byte, InetSocketAddress> clientMap;
    private ClientIdCreator idCreator;


    public ServerConnector(String listenAddress, int serverPort, ClientIdCreator idCreator){
        super();
        this.serveAddress = new InetSocketAddress(listenAddress, serverPort);
        this.idCreator = idCreator;
        this.clientMap = new ConcurrentHashMap<Byte, InetSocketAddress>();
    }

	@Override
	protected List<InetSocketAddress> getTargets(Message message) {
		if(message.getClientId() == 0){
            return (List<InetSocketAddress>)clientMap.values();
        }
        return Arrays.asList(clientMap.get(message.getClientId()));
	}

	@Override
	protected void configureChannel() {
        try {
            this.udpChannel.bind(this.serveAddress);
            this.udpChannel.configureBlocking(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public Message receive(){
        InternalPackage internalPackage = this.receiverThread.getReceiverQueue().poll();
        if(internalPackage == null){
            return null;
        }
        Message message = internalPackage.getMessage();
        if(message instanceof HandshakeMessage){
            byte id = idCreator.registerClient(((HandshakeMessage) message).getPlayerName());
            message = new HandshakeMessage(id, ((HandshakeMessage) message).getPlayerName());
            clientMap.put(id, internalPackage.getSocketAddress());
        }
        return message;
    }

}

interface ClientIdCreator {
    byte registerClient(String playerName);
}