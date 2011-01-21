package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelectorFactory;

public interface IDistributionProvider<R> {
	public void initializeService();
	public void finalizeService();
	public void startService();
	public String getDistributionStrategy();
	public void setPeer(IOdysseusPeer peer);
	public void distributePlan(P2PQuery q, R serverResponse);
	public IClientSelectorFactory<?> getClientSelectorFactory();
}
