package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class SplittingExecutionHandler<F extends AbstractSplittingStrategy>
		extends AbstractExecutionHandler<F> implements IExecutionHandler<F> {

	public SplittingExecutionHandler() {
		super(Lifecycle.SPLIT);
	}

	public SplittingExecutionHandler(
			SplittingExecutionHandler<F> other) {
		super(other);
	}

	@Override
	public void run() {
		if (getExecutionListenerCallback() != null && getPeer() != null) {
			P2PQuery query = getExecutionListenerCallback().getQuery();
			getFunction().setCallback(getExecutionListenerCallback());
			int subplanID = 0;
			for (ILogicalOperator fullPlan : query.getLogicalOperatorplan()) {
				ArrayList<ILogicalOperator> plan = getFunction().splitPlan(
						fullPlan);
				if (plan == null || plan.size() == 0) {
					getExecutionListenerCallback()
							.changeState(Lifecycle.FAILED);
					return;
				} else {
					for (int i = 0; i < plan.size(); i++) {
						getExecutionListenerCallback().getQuery().addSubPlan(
								new Subplan(query.getId() + "_"+(subplanID++),
										plan.get(i)), i == 0);
					}
				}
			}
			getExecutionListenerCallback().changeState(
					Lifecycle.SUCCESS);

		}
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new SplittingExecutionHandler<F>(this);
	}
}
