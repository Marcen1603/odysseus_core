package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

public class P2PNetworkManagerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(P2PNetworkManagerService.class);

	private static IP2PNetworkManager networkManager;

	// called by OSGi
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		networkManager = serv;
		LOG.debug("Bound p2p network manager {}", serv.getClass().getSimpleName());
	}

	// called by OSGi
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (serv == networkManager ) {
			LOG.debug("Unbound p2p network manager {}", serv.getClass().getSimpleName());
			
			networkManager = null;
		}
	}
	
	public static boolean isBound() {
		return networkManager != null;
	}
	
	public static IP2PNetworkManager getInstance() {
		return networkManager;
	}

	public static void waitForStart() {
		while( !isBound() || !networkManager.isStarted() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}
