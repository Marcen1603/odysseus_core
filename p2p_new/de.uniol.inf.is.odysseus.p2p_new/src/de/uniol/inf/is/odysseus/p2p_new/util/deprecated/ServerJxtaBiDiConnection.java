package de.uniol.inf.is.odysseus.p2p_new.util.deprecated;

import java.io.IOException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import net.jxta.util.JxtaServerPipe;
import de.uniol.inf.is.odysseus.p2p_new.activator.P2PNetworkManager;

@Deprecated
public class ServerJxtaBiDiConnection extends AbstractJxtaBiDiConnection {

	private JxtaServerPipe pipe;
	private JxtaBiDiPipe bidiPipe;
	
	public ServerJxtaBiDiConnection( PipeAdvertisement adv ) {
		super(adv);
	}

	@Override
	public void connect() throws IOException {		
		pipe = new JxtaServerPipe(P2PNetworkManager.getInstance().getLocalPeerGroup(), getPipeAdvertisement());
		pipe.setPipeTimeout(0);
		
		bidiPipe = pipe.accept();
		
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
