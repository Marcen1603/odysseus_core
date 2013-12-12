package de.uniol.inf.is.odysseus.rcp.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class PeerResourceManagerService {

	private static final Logger LOG = LoggerFactory.getLogger(PeerResourceManagerService.class);
	private static IPeerResourceUsageManager manager;
	
	public void bindPeerResourceUsageManager( IPeerResourceUsageManager man ) {
		manager = man;
		
		LOG.debug("Peer resource usage manager bound");
	}
	
	public void unbindPeerResourceUsageManager( IPeerResourceUsageManager man ) {
		if( man == manager ) {
			manager = null;
			LOG.debug("Peer resource usage manager  unbound");
		}
	}
	
	public static IPeerResourceUsageManager get() {
		return manager;
	}
	
	public static boolean isBound() {
		return manager != null;
	}
}
