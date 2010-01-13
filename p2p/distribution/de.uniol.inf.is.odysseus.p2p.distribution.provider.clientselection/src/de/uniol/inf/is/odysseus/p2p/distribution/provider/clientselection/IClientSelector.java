package de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;


public interface IClientSelector extends Runnable{
	public void setTimetoWait(int time);
	public String getName();
	Query getQuery();
}
