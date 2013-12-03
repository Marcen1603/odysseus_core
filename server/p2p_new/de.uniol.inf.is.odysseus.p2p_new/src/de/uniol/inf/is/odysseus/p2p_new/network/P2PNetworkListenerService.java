package de.uniol.inf.is.odysseus.p2p_new.network;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;

public class P2PNetworkListenerService {

	// called by OSGi-DS
	public void bindP2PNetworkListener(IP2PNetworkListener listener) {
		P2PNetworkListenerRegistry.getInstance().add(listener);
	}

	// called by OSGi-DS
	public void unbindP2PNetworkListener(IP2PNetworkListener listener) {
		P2PNetworkListenerRegistry.getInstance().remove(listener);
	}

}
