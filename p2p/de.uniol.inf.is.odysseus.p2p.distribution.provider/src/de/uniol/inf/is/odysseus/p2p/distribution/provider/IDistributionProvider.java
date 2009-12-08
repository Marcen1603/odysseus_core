package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.selector.IClientSelectorFactory;

public interface IDistributionProvider<P> {
	public void initializeService();
	public void finalizeService();
	public void startService();
	public String getDistributionStrategy();
	public void setPeer(P peer);
	public void distributePlan(IExecutionListenerCallback callback, Object serverResponse);
	public IExecutionHandlerFactory getExecutionHandlerFactory();
	public IClientSelectorFactory getClientSelectorFactory();
}
