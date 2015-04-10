package de.uniol.inf.is.odysseus.peer.network;

public interface IP2PNetworkListener {

	public void networkStarted(IP2PNetworkManager sender);
	public void networkStopped(IP2PNetworkManager sender);
}
