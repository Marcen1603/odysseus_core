package de.uniol.inf.is.odysseus.p2p.splitting.sourcesplit;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;

public class SourceSplitExecutionHandler<P extends AbstractPeer,F extends AbstractSplittingStrategy> extends AbstractExecutionHandler<P, F> {
	
	
	public SourceSplitExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.SPLIT);
	}
	
	
	@Override
	public IExecutionHandler<P, F> clone()  {
		IExecutionHandler<P, F> handler = new SourceSplitExecutionHandler<P, F>();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setProvidedLifecycle(getProvidedLifecycle());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	@Override
	public String getName() {
		return "SourceSplitExecutionHandler";
	}

	@Override
	public void run() {
		if(getExecutionListenerCallback()!=null && getPeer()!=null) {
			getFunction().setCallback(getExecutionListenerCallback());
			ArrayList<ILogicalOperator> plan = getFunction().splitPlan(getExecutionListenerCallback().getQuery().getLogicalOperatorplan());
			if(plan.size() == 0 || plan == null) {
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
			}
			else{				
				for(int i=0; i<plan.size(); i++) {
					getExecutionListenerCallback().getQuery().addSubPlan(""+(i+1), new SubplanJxtaImpl(""+(i+1), plan.get(i)));	
				}
				getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
			}
		}
	}
}
