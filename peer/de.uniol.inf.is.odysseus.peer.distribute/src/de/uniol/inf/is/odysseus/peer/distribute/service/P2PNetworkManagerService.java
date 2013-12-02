package de.uniol.inf.is.odysseus.peer.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class P2PNetworkManagerService {

	private static final Logger LOG = LoggerFactory.getLogger(P2PNetworkManagerService.class);

	private static IP2PNetworkManager networkManager;

	public final void bindP2PNetworkManager(IP2PNetworkManager manager) {
		networkManager = manager;
		LOG.debug("P2P network manager bound {}", manager);
	}

	public final void unbindP2PNetworkManager(IP2PNetworkManager manager) {
		if (networkManager == manager) {
			networkManager = null;
			LOG.debug("P2P network manager unbound {}", manager);
		}
	}

	public static IP2PNetworkManager get() {
		return networkManager;
	}

	public static boolean isBound() {
		return get() != null;
	}
}