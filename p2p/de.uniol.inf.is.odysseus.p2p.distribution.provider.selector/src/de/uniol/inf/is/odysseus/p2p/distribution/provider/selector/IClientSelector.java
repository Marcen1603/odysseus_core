package de.uniol.inf.is.odysseus.p2p.distribution.provider.selector;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;


public interface IClientSelector extends Runnable{
	public void setTimetoWait(int time);
	public IExecutionListenerCallback getCallback();
	public String getName();
}
