package de.uniol.inf.is.odysseus.p2p.distribution.client;


import de.uniol.inf.is.odysseus.p2p.distribution.client.receiver.IReceiverStrategy;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDistributionClient<T extends AbstractPeer> implements IDistributionClient<T> {

	
	private AbstractPeer peer;
	private IReceiverStrategy receiverStrategy;
	private Logger logger;
	
	public AbstractDistributionClient() {
		this.logger = LoggerFactory.getLogger(AbstractDistributionClient.class);
	}
	
	
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

	@Override
	public IReceiverStrategy getReceiverStrategy() {
		return this.receiverStrategy;
	}
	
	public void bindReceiverStrategy(IReceiverStrategy receiver) {
		getLogger().info("Binding Receiver Strategy " +receiver);
		this.receiverStrategy = receiver;
	}
	
	public void unbindReceiverStrategy(IReceiverStrategy receiver) {
		if(this.receiverStrategy == receiver) {
			getLogger().info("Unbinding Receiver Strategy " +receiver);
			this.receiverStrategy = null;
		}
	}
	
	public Logger getLogger() {
		return logger;
	}
}
