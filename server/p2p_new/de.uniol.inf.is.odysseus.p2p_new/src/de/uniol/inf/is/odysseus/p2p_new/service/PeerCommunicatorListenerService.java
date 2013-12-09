package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.communication.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.communication.PeerCommunicatorListenerRegistry;

public class PeerCommunicatorListenerService {

	private static final Logger LOG = LoggerFactory.getLogger(PeerCommunicatorListenerService.class);

	public void bindPeerCommunicatorListener( IPeerCommunicatorListener listener ) {
		PeerCommunicatorListenerRegistry.getInstance().add(listener);
		
		LOG.debug("Bound peer communicator listener {}", listener);
	}
	
	public void unbindPeerCommunicatorListener( IPeerCommunicatorListener listener ) {
		PeerCommunicatorListenerRegistry.getInstance().remove(listener);
		
		LOG.debug("Unbound peer communicator listener {}", listener);
	}
}
