package de.uniol.inf.is.odysseus.p2p.distribution.client;

import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;

public interface IQuerySpecificationListener<S> extends Runnable {
	public void startListener();
	public IQuerySpecificationHandler<S> getQuerySpecificationHandler(S spec);
	public IQuerySelectionStrategy getQuerySelectionStrategy();
}
