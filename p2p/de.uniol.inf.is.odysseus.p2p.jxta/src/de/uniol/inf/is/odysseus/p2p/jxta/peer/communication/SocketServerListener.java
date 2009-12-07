package de.uniol.inf.is.odysseus.p2p.jxta.peer.communication;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.ServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;

public class SocketServerListener implements ISocketServerListener {
	private Map<String, IMessageHandler> messageHandler = new HashMap<String, IMessageHandler>();
	private PipeAdvertisement serverPipeAdvertisement;
	private AbstractPeer aPeer;

	public SocketServerListener(AbstractPeer aPeer) {
		setServerPipeAdvertisement((PipeAdvertisement) aPeer.getServerResponseAddress());
		setaPeer(aPeer);
	}

	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			
			serverSocket = new JxtaServerSocket(PeerGroupTool.getPeerGroup(), getServerPipeAdvertisement(),
					10);
			serverSocket.setSoTimeout(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				if (socket != null) {
					IServerSocketConnectionHandler connectionHandler = new ServerSocketConnectionHandler(socket, getMessageHandler());
					Thread connectionHandlerThread = new Thread(connectionHandler);
					connectionHandlerThread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setServerPipeAdvertisement(PipeAdvertisement serverPipeAdvertisement) {
		this.serverPipeAdvertisement = serverPipeAdvertisement;
	}

	public PipeAdvertisement getServerPipeAdvertisement() {
		return serverPipeAdvertisement;
	}

	@Override
	public synchronized boolean deregisterMessageHandler(IMessageHandler messageHandler) {
		if(getMessageHandler().containsKey(messageHandler.getInterestedNamespace())) {
			getMessageHandler().remove(messageHandler.getInterestedNamespace());
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean registerMessageHandler(IMessageHandler messageHandler) {
		if(!getMessageHandler().containsKey(messageHandler.getInterestedNamespace())) {
			getMessageHandler().put(messageHandler.getInterestedNamespace(), messageHandler);
			return true;
		}
		return false;
	}
	

	public AbstractPeer getaPeer() {
		return aPeer;
	}

	private void setaPeer(AbstractPeer aPeer) {
		this.aPeer = aPeer;
	}
	
	private synchronized Map<String, IMessageHandler> getMessageHandler() {
		return messageHandler;
	}

	@Override
	public synchronized boolean deregisterMessageHandler(List<IMessageHandler> messageHandler) {
		boolean success = true;
		for (IMessageHandler iMessageHandler : messageHandler) {
			if(!deregisterMessageHandler(iMessageHandler)) {
				success = false;
			}
		}
		return success;
	}

	@Override
	public synchronized boolean registerMessageHandler(List<IMessageHandler> messageHandler) {
		boolean success = true;
		for (IMessageHandler iMessageHandler : messageHandler) {
			if(!registerMessageHandler(iMessageHandler)) {
				success = false;
			}
		}
		return success;
	}


	
	

}
