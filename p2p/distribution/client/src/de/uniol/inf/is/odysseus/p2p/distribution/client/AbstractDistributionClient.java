package de.uniol.inf.is.odysseus.p2p.distribution.client;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;

public abstract class AbstractDistributionClient implements IDistributionClient {

	private AbstractOdysseusPeer peer;
	private IQuerySelectionStrategy querySelectionStrategy;
	static private Logger logger = LoggerFactory
			.getLogger(AbstractDistributionClient.class);;

	public AbstractOdysseusPeer getPeer() {
		return peer;
	}

	@Override
	public abstract String getDistributionStrategy();

	@Override
	public void setPeer(IOdysseusPeer peer) {
		this.peer = (AbstractOdysseusPeer) peer;
	}

	@Override
	public abstract void initializeService();

	@Override
	public abstract void startService();

	public void bindQuerySelectionStrategy(IQuerySelectionStrategy selection) {
		getLogger().info("Binding Query Selection Strategy " + selection);
		this.querySelectionStrategy = selection;
	}

	public void unbindQuerySelectionStrategy(IQuerySelectionStrategy selection) {
		if (this.querySelectionStrategy == selection) {
			getLogger().info("Unbinding Query Selection Strategy " + selection);
			this.querySelectionStrategy = null;
		}
	}

	public Logger getLogger() {
		return logger;
	}

	@Override
	public abstract IQuerySpecificationListener<?> getQuerySpecificationListener();

	@Override
	public IQuerySelectionStrategy getQuerySelectionStrategy() {
		return this.querySelectionStrategy;
	}
}
