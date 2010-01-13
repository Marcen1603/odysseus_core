package de.uniol.inf.is.odysseus.p2p.distribution.client;

import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;

public interface IQuerySpecificationHandler<S> {
	public void handleQuerySpezification(S adv);
	public IQuerySelectionStrategy getQuerySelectionStrategy();
}
