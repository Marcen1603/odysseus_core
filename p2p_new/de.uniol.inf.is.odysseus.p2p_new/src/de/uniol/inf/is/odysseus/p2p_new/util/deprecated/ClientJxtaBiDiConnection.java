package de.uniol.inf.is.odysseus.p2p_new.util.deprecated;

import java.io.IOException;
import java.net.SocketTimeoutException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import de.uniol.inf.is.odysseus.p2p_new.network.P2PNetworkManager;

@Deprecated
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
				bidiPipe.connect(P2PNetworkManager.getInstance().getLocalPeerGroup(), getPipeAdvertisement(), 3000);
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
