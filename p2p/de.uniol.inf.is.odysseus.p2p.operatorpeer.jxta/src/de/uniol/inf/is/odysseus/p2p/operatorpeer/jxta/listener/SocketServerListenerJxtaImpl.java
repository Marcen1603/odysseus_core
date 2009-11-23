package de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.listener;

import java.io.IOException;
import java.net.Socket;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.jxta.peer.communication.ServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.AbstractOperatorPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IServerSocketConnectionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.communication.ISocketServerListener;

public class SocketServerListenerJxtaImpl implements ISocketServerListener {
	
	private IServerSocketConnectionHandler connectionHandler;
	private Thread connectionHandlerThread;
	

	private PipeAdvertisement serverPipeAdvertisement;
	private AbstractOperatorPeer aPeer;




	public SocketServerListenerJxtaImpl(AbstractOperatorPeer aPeer) {
		serverPipeAdvertisement = ((OperatorPeerJxtaImpl)aPeer).getServerPipeAdvertisement();
		this.aPeer = aPeer;
	}



	@Override
	public void run() {
		JxtaServerSocket serverSocket = null;
		try {
			serverSocket = new JxtaServerSocket(OperatorPeerJxtaImpl
					.getInstance().getNetPeerGroup(), serverPipeAdvertisement,
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
					setServerSocketConnectionHandler(new ServerSocketConnectionHandler(socket, this.aPeer.getMessageHandler()));
					connectionHandlerThread = new Thread(getServerSocketConnectionHandler());
					connectionHandlerThread.start();
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
	public void setServerSocketConnectionHandler(
			IServerSocketConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
		
	}
}
