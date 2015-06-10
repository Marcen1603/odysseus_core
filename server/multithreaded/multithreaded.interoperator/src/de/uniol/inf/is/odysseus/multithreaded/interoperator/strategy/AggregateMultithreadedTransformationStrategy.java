package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;

public class AggregateMultithreadedTransformationStrategy extends
		AbstractMultithreadedTransformationStrategy<AggregateAO> {

	@Override
	public String getName() {
		return "AggregateMultithreadedTransformationStrategy";
	}

	@Override
	public int evaluateCompatibility(ILogicalOperator operator) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean transform(ILogicalOperator operator, int degreeOfParallelization) {
		// TODO Auto-generated method stub
		return false;
	}

}
