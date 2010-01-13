package de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IClientSelectorFactory<C> {
	public IClientSelector getNewInstance(int time, Query query);
	public IClientSelector getNewInstance(int time, Query query, C callback);
	public String getName();
}
