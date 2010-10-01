package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.splitting.refinement.base.IRefinementStrategy;

public abstract class AbstractSplittingStrategy implements ISplittingStrategy {

	private IExecutionListenerCallback callback;
	private AbstractPeer peer;

	public IExecutionListenerCallback getCallback() {
		return this.callback;
	}

	public void setCallback(IExecutionListenerCallback callback) {
		this.callback = callback;
	}

	private IRefinementStrategy refinement = null;
	
	@Override
	public abstract ArrayList<ILogicalOperator> splitPlan(
			ILogicalOperator ao);

	@Override
	public abstract String getName();

	public void bindRefinement(IRefinementStrategy rs) {
		this.refinement = rs;
	}

	public void unbindRefinement(IRefinementStrategy rs) {
		if(this.refinement == rs) {
			this.refinement = null;
		}
	}
	
	public IRefinementStrategy getRefinement() {
		return refinement;
	}
	
	@Override
	public void setPeer(IPeer peer) {
		this.peer = (AbstractPeer) peer;
	}
	protected AbstractPeer getPeer() {
		return this.peer;
	}
}
