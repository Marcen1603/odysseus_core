package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;
import java.net.SocketTimeoutException;

import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;

public class ClientJxtaBiDiConnection extends AbstractJxtaBiDiConnection {

	private JxtaBiDiPipe bidiPipe;
	
	public ClientJxtaBiDiConnection(PipeAdvertisement adv) {
		super(adv);
	}

	@Override
	public void connect() throws IOException {
		
		bidiPipe = new JxtaBiDiPipe();
//		bidiPipe.setReliable(false);
		
		boolean done = false;
		
		while( !done ) {
			try {
				bidiPipe.connect(P2PNewPlugIn.getOwnPeerGroup(), getPipeAdvertisement(), 3000);
				done = true;
			} catch( SocketTimeoutException ex ) {
				// try again
			}
		}
		
		super.connect();
	}
	
	@Override
	public void disconnect() {
		try {
			bidiPipe.close();
		} catch (IOException e) {
		}
		
		super.disconnect();
	}
	
	@Override
	protected JxtaBiDiPipe getBiDiPipe() {
		return bidiPipe;
	}

}
