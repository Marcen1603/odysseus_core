package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;

public abstract class AbstractSplittingStrategy implements ISplittingStrategy {

	private IExecutionListenerCallback callback;
	private AbstractOdysseusPeer peer;

	public IExecutionListenerCallback getCallback() {
		return this.callback;
	}

	public void setCallback(IExecutionListenerCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public abstract ArrayList<ILogicalOperator> splitPlan(
			ILogicalOperator ao);

	@Override
	public abstract String getName();
	
	@Override
	public void setPeer(IOdysseusPeer peer) {
		this.peer = (AbstractOdysseusPeer) peer;
	}
	protected AbstractOdysseusPeer getPeer() {
		return this.peer;
	}
	
	@Override
	public void finalizeService() {
	}

	@Override
	public void startService() {
	}

}
