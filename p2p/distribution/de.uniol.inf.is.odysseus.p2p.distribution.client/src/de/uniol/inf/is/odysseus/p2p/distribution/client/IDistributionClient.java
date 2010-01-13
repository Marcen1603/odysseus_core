package de.uniol.inf.is.odysseus.p2p.distribution.client;

import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.peer.IPeer;



public interface IDistributionClient {
	public void initializeService();
	public void startService();
	public void setPeer(IPeer peer);
	public String getDistributionStrategy();
//	public IMessageHandler getMessageHandler();
//	public IQuerySpecificationListener<S> getQuerySpecificationListener();

	public IQuerySpecificationListener<?> getQuerySpecificationListener();
	public IQuerySelectionStrategy getQuerySelectionStrategy();
	
	
}
