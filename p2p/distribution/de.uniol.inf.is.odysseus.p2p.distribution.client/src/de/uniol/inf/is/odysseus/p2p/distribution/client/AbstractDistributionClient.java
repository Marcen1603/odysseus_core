package de.uniol.inf.is.odysseus.p2p.distribution.client;


import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.IPeer;

public abstract class AbstractDistributionClient implements IDistributionClient {

	
	private AbstractPeer peer;
	private IQuerySelectionStrategy querySelectionStrategy;
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
	public void setPeer(IPeer peer) {
		this.peer = (AbstractPeer) peer;
	}
	
//	@Override
//	public abstract IMessageHandler getMessageHandler();

	@Override
	public abstract void initializeService();


	@Override
	public abstract void startService();

//	@Override
//	public IQuerySelectionStrategy getQuerySelectionStrategy() {
//		return this.querySelectionStrategy;
//	}
	
	public void bindQuerySelectionStrategy(IQuerySelectionStrategy selection) {
		getLogger().info("Binding Query Selection Strategy " +selection);
		this.querySelectionStrategy = selection;
	}
	
	public void unbindQuerySelectionStrategy(IQuerySelectionStrategy selection) {
		if(this.querySelectionStrategy == selection) {
			getLogger().info("Unbinding Query Selection Strategy " +selection);
			this.querySelectionStrategy = null;
		}
	}
	
	public Logger getLogger() {
		return logger;
	}

	@Override
	public abstract IQuerySpecificationListener<?> getQuerySpecificationListener();
	
	@Override
	public IQuerySelectionStrategy getQuerySelectionStrategy() {
		return this.querySelectionStrategy;
	}
}
