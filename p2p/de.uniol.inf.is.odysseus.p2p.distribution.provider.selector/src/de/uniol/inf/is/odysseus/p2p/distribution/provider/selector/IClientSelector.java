package de.uniol.inf.is.odysseus.p2p.distribution.provider.selector;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IClientSelector extends Runnable{
	public void setTimetoWait(int time);
	public Query getQuery();
}
