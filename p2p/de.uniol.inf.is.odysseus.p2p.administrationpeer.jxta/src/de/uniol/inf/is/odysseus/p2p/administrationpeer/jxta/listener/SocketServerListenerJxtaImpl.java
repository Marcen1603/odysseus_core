package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.listener;

import java.io.IOException;
import java.net.Socket;

import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.ISocketServerListener;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.IServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;

public class SocketServerListenerJxtaImpl implements ISocketServerListener {

	

	private IServerSocketConnectionHandler connectionHandler;
	private Thread connectionHandlerThread;
	

	

	public SocketServerListenerJxtaImpl() {
		AdministrationPeerJxtaImpl.getInstance().setServerPipeAdvertisement(AdvertisementTools.getServerPipeAdvertisement(PeerGroupTool.getPeerGroup()));
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
					this.connectionHandler = AdministrationPeerJxtaImpl.getInstance().getDistributionProvider().getServerSocketConnectionHandler();
					this.connectionHandler.setSocket(socket);
					this.connectionHandlerThread = new Thread(this.connectionHandler);
					this.connectionHandlerThread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	

}
