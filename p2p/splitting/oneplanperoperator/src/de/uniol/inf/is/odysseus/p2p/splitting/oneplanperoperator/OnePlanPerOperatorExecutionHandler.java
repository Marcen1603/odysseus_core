package de.uniol.inf.is.odysseus.p2p.splitting.oneplanperoperator;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;


public class OnePlanPerOperatorExecutionHandler<F extends AbstractSplittingStrategy>
		extends AbstractSplittingExecutionHandler<F> {

	public OnePlanPerOperatorExecutionHandler(
			OnePlanPerOperatorExecutionHandler<F> onePlanPerOperatorExecutionHandler) {
		super(onePlanPerOperatorExecutionHandler);
	}

	public OnePlanPerOperatorExecutionHandler() {
		super();
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new OnePlanPerOperatorExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "OnePlanPerOperatorExecutionHandler";
	}

}
