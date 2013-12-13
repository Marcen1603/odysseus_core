package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class PeerResourceUsageManagerService {

	private static final Logger LOG = LoggerFactory.getLogger(PeerResourceUsageManagerService.class);
	private static IPeerResourceUsageManager resourceUsageManager;
	
	public void bindPeerResourceUsageManager( IPeerResourceUsageManager manager ) {
		resourceUsageManager = manager;
		
		LOG.debug("Peer Resource Usage Manager bound {}", manager);
	}
	
	public void unbindPeerResourceUsageManager( IPeerResourceUsageManager manager ) {
		if( resourceUsageManager == manager ) {
			resourceUsageManager = null;
			
			LOG.debug("Peer Resource Usage Manager unbound {}", manager);
		}
	}
	
	public static boolean isBound() {
		return resourceUsageManager != null;
	}
	
	public static IPeerResourceUsageManager get() {
		return resourceUsageManager;
	}
}
