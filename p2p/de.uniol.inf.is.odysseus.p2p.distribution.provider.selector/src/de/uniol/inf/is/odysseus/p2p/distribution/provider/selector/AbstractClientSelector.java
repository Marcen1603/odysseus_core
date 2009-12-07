package de.uniol.inf.is.odysseus.p2p.distribution.provider.selector;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractClientSelector implements IClientSelector {

	private Query query;

	public AbstractClientSelector(Query query) {
		this.query = query;
	}
	
	@Override
	public void setTimetoWait(int time) {
		
	}

	@Override
	public abstract void run();

	
	@Override
	public Query getQuery() {
		return this.query;
	}
}
