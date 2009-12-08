package de.uniol.inf.is.odysseus.p2p.distribution.client;


import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.distribution.client.receiver.IReceiverStrategy;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public interface IDistributionClient<T extends IPeer> {
	public void initializeService();
	public void startService();
	public void setPeer(T peer);
	public String getDistributionStrategy();
	public IMessageHandler getMessageHandler();
	public IReceiverStrategy getReceiverStrategy();
	
}
