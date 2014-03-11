package de.uniol.inf.is.odysseus.peer.logging;

import org.apache.log4j.Logger;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

public class P2PNetworkManagerListener implements IP2PNetworkListener {

	private final JXTAAppender jxtaAppender = new JXTAAppender();

	@Override
	public void networkStarted(IP2PNetworkManager sender) {
		Logger.getRootLogger().addAppender(jxtaAppender);
	}

	@Override
	public void networkStopped(IP2PNetworkManager p2pNetworkManager) {
		Logger.getRootLogger().removeAppender(jxtaAppender);
	}

}
