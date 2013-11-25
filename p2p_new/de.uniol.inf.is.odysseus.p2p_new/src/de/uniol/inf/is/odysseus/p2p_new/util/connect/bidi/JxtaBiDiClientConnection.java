package de.uniol.inf.is.odysseus.p2p_new.util.connect.bidi;

import java.io.IOException;
import java.net.SocketTimeoutException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNetworkManager;

public class JxtaBiDiClientConnection extends JxtaBiDiConnection {

	private static final Logger LOG = LoggerFactory.getLogger(JxtaBiDiClientConnection.class);
	private static final int MAX_CONNECT_WAITING_TIME_MILLIS = 45000;
	private final PipeAdvertisement pipeAdvertisement;
	
	public JxtaBiDiClientConnection(PipeAdvertisement adv) {
		super(new JxtaBiDiPipe());
		
		this.pipeAdvertisement = adv;
	}

	@Override
	public void connect() throws IOException {
		try {
			boolean connected = false;
			final long startTime = System.currentTimeMillis();
			while( !connected ) {
				try {
					getPipe().connect(P2PNetworkManager.getInstance().getLocalPeerGroup(), null, pipeAdvertisement, 1000, this);
					connected = true;
				} catch( SocketTimeoutException ex ) {
					if( System.currentTimeMillis() - startTime > MAX_CONNECT_WAITING_TIME_MILLIS) {
						throw new IOException("Connecting takes too long");
					}
				}
			}
		} catch (Throwable t) {
			LOG.error("Could not connect to server", t);
			setConnectFail();
			throw t;
		}

		super.connect();
	}
}
