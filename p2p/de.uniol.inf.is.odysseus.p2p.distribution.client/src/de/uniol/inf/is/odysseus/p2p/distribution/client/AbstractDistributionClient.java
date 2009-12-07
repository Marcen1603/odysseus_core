package de.uniol.inf.is.odysseus.p2p.distribution.client;


import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public abstract class AbstractDistributionClient<T extends AbstractPeer> implements IDistributionClient<T> {

	private AbstractPeer peer;
	
	public AbstractPeer getPeer() {
		return peer;
	}
	
	@Override
	public abstract String getDistributionStrategy();

	@Override
	public void setPeer(T peer) {
		this.peer = peer;
	}
	
	@Override
	public abstract IMessageHandler getMessageHandler();

	@Override
	public abstract void initializeService();


	@Override
	public abstract void startService();

}
