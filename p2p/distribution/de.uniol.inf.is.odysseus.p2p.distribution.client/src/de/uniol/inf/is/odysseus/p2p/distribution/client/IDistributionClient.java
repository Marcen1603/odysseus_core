package de.uniol.inf.is.odysseus.p2p.distribution.client;

import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;



public interface IDistributionClient {
	public void initializeService();
	public void startService();
	public void setPeer(IOdysseusPeer peer);
	public String getDistributionStrategy();
	public IQuerySpecificationListener<?> getQuerySpecificationListener();
	public IQuerySelectionStrategy getQuerySelectionStrategy();
	
	
}
