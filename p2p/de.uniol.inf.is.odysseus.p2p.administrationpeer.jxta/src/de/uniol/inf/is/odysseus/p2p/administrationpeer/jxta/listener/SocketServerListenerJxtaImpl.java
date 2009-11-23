package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.io.IOException;
import java.net.Socket;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.AbstractAdministrationPeer;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.ServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;

public class SocketServerListenerJxtaImpl implements ISocketServerListener {

	

	private IServerSocketConnectionHandler connectionHandler;
	private Thread connectionHandlerThread;
	private PipeAdvertisement serverPipeAdvertisement;
	private AbstractAdministrationPeer aPeer;

	

	public SocketServerListenerJxtaImpl(AbstractAdministrationPeer aPeer) {
		setServerPipeAdvertisement(((AdministrationPeerJxtaImpl)aPeer).getServerPipeAdvertisement());
		this.aPeer = aPeer;		
//		AdministrationPeerJxtaImpl.getInstance().setServerPipeAdvertisement(AdvertisementTools.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
	}

	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			
			serverSocket = new JxtaServerSocket(PeerGroupTool.getPeerGroup(), AdministrationPeerJxtaImpl.getInstance().getServerPipeAdvertisement(),
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
					this.connectionHandler = new ServerSocketConnectionHandler(socket, this.aPeer.getMessageHandler());
					this.connectionHandlerThread = new Thread(this.connectionHandler);
					this.connectionHandlerThread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IServerSocketConnectionHandler getServerSocketConnectionHandler() {
		return this.connectionHandler;
	}

	@Override
	public void setServerSocketConnectionHandler(IServerSocketConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	public void setServerPipeAdvertisement(PipeAdvertisement serverPipeAdvertisement) {
		this.serverPipeAdvertisement = serverPipeAdvertisement;
	}

	public PipeAdvertisement getServerPipeAdvertisement() {
		return serverPipeAdvertisement;
	}


	
	

}
