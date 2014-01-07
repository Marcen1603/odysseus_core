package de.uniol.inf.is.odysseus.peer.ping.service;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class P2PNetworkManagerService {

	private static IP2PNetworkManager manager;

	// called by OSGi-DS
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		manager = serv;
	}

	// called by OSGi-DS
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (manager == serv) {
			manager = null;
		}
	}

	public static boolean isBound() {
		return get() != null;
	}

	public static IP2PNetworkManager get() {
		return manager;
	}

	public static IP2PNetworkManager waitFor() {
		while (!isBound()) {
			try {
				Thread.sleep(250);
			} catch (Throwable t) {
			}
		}

		return get();
	}
}
