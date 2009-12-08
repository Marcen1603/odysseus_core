package de.uniol.inf.is.odysseus.p2p.distribution.provider.selector;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;


public interface IClientSelectorFactory {
	public IClientSelector getNewInstance(int time, IExecutionListenerCallback callback);
	public String getName();
}
