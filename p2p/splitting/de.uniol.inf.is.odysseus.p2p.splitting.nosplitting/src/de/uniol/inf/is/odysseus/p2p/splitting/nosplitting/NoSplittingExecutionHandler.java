package de.uniol.inf.is.odysseus.p2p.splitting.nosplitting;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;

public class NoSplittingExecutionHandler extends AbstractExecutionHandler<AbstractPeer, AbstractSplittingStrategy>{

	@Override
	public IExecutionHandler<AbstractPeer, AbstractSplittingStrategy> clone() throws CloneNotSupportedException {
		IExecutionHandler<AbstractPeer, AbstractSplittingStrategy> handler = new NoSplittingExecutionHandler();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setProvidedLifecycle(getProvidedLifecycle());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	@Override
	public String getName() {
		return "NoSplittingExecutionHandler";
	}

	@Override
	public void run() {
		if(getExecutionListenerCallback()!=null && getPeer()!= null) {
			getFunction().setCallback(getExecutionListenerCallback());
			ArrayList<ILogicalOperator> plan = getFunction().splitPlan(getExecutionListenerCallback().getQuery().getLogicalOperatorplan());
			if(plan.size() == 1) {
				getExecutionListenerCallback().getQuery().addSubPlan("1", new SubplanJxtaImpl("1", plan.get(0)));
				getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
			}
			else {
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
			}
		}
	}

}
