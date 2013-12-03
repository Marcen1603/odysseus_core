package de.uniol.inf.is.odysseus.p2p_new.nogui;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class P2PNetworkManagerService {

	private static IP2PNetworkManager networkManager;
	
	public void bindP2PNetworkManager( IP2PNetworkManager manager ) {
		networkManager = manager;
	}
	
	public void unbindP2PNetworkManager( IP2PNetworkManager manager ) {
		networkManager = null;
	}
	
	public static IP2PNetworkManager get() {
		return networkManager;
	}
	
	public static boolean isBound() {
		return get() != null;
	}
}
