package de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection;

import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;



public abstract class AbstractClientSelector<C> implements IClientSelector {

	
	private C callback;

	public C getCallback() {
		return this.callback;
	}

	public void setCallback(C callback) {
		this.callback = callback;
	}

	private int time;
	private P2PQuery query;
	final protected ILogListener log;

	public AbstractClientSelector(int time, P2PQuery query, C callback, ILogListener log) {
		this.query = query;
		this.time = time;
		this.callback = callback;
		this.log = log;
	}
	
	public AbstractClientSelector(int time, P2PQuery query, ILogListener log) {
		this.query = query;
		this.time = time;
		this.callback = null;
		this.log = log;
	}
	
	@Override
	public void setTimetoWait(int time) {
		this.time = time;
	}

	@Override
	public abstract void run();

	@Override
	public P2PQuery getQuery() {
		return query;
	}
	
	public int getTime() {
		return time;
	}
}
