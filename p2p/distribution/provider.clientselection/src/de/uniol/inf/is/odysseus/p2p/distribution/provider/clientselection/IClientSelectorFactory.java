package de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public interface IClientSelectorFactory<C> {
	public IClientSelector getNewInstance(int time, P2PQuery query, C callback, ILogListener log);
	public String getName();
}
