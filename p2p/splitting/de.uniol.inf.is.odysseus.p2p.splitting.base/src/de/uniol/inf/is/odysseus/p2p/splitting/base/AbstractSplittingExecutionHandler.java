package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public abstract class AbstractSplittingExecutionHandler<F extends AbstractSplittingStrategy>
		extends AbstractExecutionHandler<F> implements IExecutionHandler<F> {

	public AbstractSplittingExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.SPLIT);
	}

	public AbstractSplittingExecutionHandler(
			AbstractSplittingExecutionHandler<F> other) {
		super(other);
	}

	@Override
	public void run() {
		if (getExecutionListenerCallback() != null && getPeer() != null) {
			getFunction().setCallback(getExecutionListenerCallback());
			ArrayList<ILogicalOperator> plan = getFunction().splitPlan(
					getExecutionListenerCallback().getQuery()
							.getLogicalOperatorplan());
			if (plan.size() == 0 || plan == null) {
				getExecutionListenerCallback().changeState(Lifecycle.FAILED);
			} else {
				for (int i = 0; i < plan.size(); i++) {
					getExecutionListenerCallback().getQuery().addSubPlan(
							"" + (i + 1),
							new Subplan("" + (i + 1), plan.get(i)));
				}
				getExecutionListenerCallback().changeState(Lifecycle.SUCCESS);
			}
		}
	}

}
