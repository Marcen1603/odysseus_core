package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class JxtaBiDiClientConnection extends JxtaBiDiConnection {

	private final PipeAdvertisement pipeAdvertisement;
	private boolean isConnected;
	
	public JxtaBiDiClientConnection(PipeAdvertisement adv) {
		super(new JxtaBiDiPipe());
		
		this.pipeAdvertisement = adv;
	}

	@Override
	public void connect() throws IOException {
		getPipe().connect(P2PNewPlugIn.getOwnPeerGroup(), null, pipeAdvertisement, 30000, this);
		isConnected = true;
	}
	
	@Override
	public boolean isConnected() {
		return isConnected;
	}
	
	@Override
	public void disconnect() {
		isConnected = false;
		
		super.disconnect();
	}
}
