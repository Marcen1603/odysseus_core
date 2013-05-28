package de.uniol.inf.is.odysseus.p2p_new.util;

import java.io.IOException;

import net.jxta.protocol.PipeAdvertisement;
import net.jxta.util.JxtaBiDiPipe;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.impl.P2PDictionary;

public class JxtaBiDiClientConnection extends JxtaBiDiConnection {

	private final PipeAdvertisement pipeAdvertisement;
	
	public JxtaBiDiClientConnection(PipeAdvertisement adv) {
		super(new JxtaBiDiPipe());
		
		this.pipeAdvertisement = adv;
	}

	@Override
	public void connect() throws IOException {
		getPipe().connect(P2PDictionary.getInstance().getLocalPeerGroup(), null, pipeAdvertisement, Integer.MAX_VALUE, this);
		super.connect();
	}
}
