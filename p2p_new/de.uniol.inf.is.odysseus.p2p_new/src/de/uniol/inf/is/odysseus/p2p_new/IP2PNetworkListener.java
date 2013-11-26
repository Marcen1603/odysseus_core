package de.uniol.inf.is.odysseus.p2p_new;

public interface IP2PNetworkListener {

	public void networkStarted(IP2PNetworkManager sender);
	public void networkStopped(IP2PNetworkManager p2pNetworkManager);
}
