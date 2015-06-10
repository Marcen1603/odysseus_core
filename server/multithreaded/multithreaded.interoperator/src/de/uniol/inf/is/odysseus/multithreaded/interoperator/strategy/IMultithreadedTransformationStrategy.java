package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IMultithreadedTransformationStrategy<T extends ILogicalOperator> {
	
	String getName();
	
	Class<T> getOperatorType();
	
	int evaluateCompatibility(ILogicalOperator operator);
	
	boolean transform(ILogicalOperator operator, int degreeOfParallelization);
}
