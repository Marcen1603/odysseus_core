package de.uniol.inf.is.odysseus.peer.rcp;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class P2PNetworkListener implements IP2PNetworkListener {

	@Override
	public void networkStarted(IP2PNetworkManager sender) {
		RCPP2PNewPlugIn.setWindowTitleSuffix( RCPP2PNewPlugIn.determineP2PTitleSuffix() );
	}

	@Override
	public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
		RCPP2PNewPlugIn.setWindowTitleSuffix(RCPP2PNewPlugIn.NO_P2P_CONNECTION_TEXT);
	}
}
