package de.uniol.inf.is.odysseus.rcp.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;

public class PeerCommunicatorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PeerCommunicatorService.class);
	
	private static IPeerCommunicator communicator;

	public void bindPeerCommunicator( IPeerCommunicator peerCommunicator ) {
		communicator = peerCommunicator;
		
		LOG.debug("Bound peer communicator {}", peerCommunicator);
	}
	
	public void unbindPeerCommunicator( IPeerCommunicator peerCommunicator ) {
		if( peerCommunicator == communicator ) {
			communicator = null;
			
			LOG.debug("Unbound peer communicator {}", peerCommunicator);
		}
	}
	
	public static boolean isBound() {
		return communicator != null;
	}
	
	public static IPeerCommunicator get() {
		return communicator;
	}
}
