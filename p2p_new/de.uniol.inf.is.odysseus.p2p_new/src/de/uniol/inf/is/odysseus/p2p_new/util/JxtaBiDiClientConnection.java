package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.net.SocketTimeoutException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class JxtaBiDiClientConnection extends JxtaBiDiConnection {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiClientConnection.class);
	
	private final PipeAdvertisement pipeAdvertisement;
	
	public JxtaBiDiClientConnection(PipeAdvertisement adv) {
		this.pipeAdvertisement = adv;
	}

	@Override
	public void connect() throws IOException {
		setSocket( determineJxtaClientSocket(pipeAdvertisement));
		super.connect();
	}
	
	private static JxtaSocket determineJxtaClientSocket(PipeAdvertisement pipeAdvertisement) throws IOException {
		JxtaSocket clientSocket = null;
		while (clientSocket == null) {
			try {
				clientSocket = new JxtaSocket(P2PNewPlugIn.getOwnPeerGroup(), pipeAdvertisement, 5000);
				clientSocket.setSoTimeout(0);
				LOG.debug("Client socket created: {}", clientSocket);
			} catch (final SocketTimeoutException ex) {
				// ignore... try again
				// timeout of zero does not work here...
			}
		}
		return clientSocket;
	}
}
