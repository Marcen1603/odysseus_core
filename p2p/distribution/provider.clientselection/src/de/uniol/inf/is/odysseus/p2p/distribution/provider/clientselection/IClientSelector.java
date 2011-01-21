package de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;


public interface IClientSelector extends Runnable{
	public void setTimetoWait(int time);
	public String getName();
	P2PQuery getQuery();
}
