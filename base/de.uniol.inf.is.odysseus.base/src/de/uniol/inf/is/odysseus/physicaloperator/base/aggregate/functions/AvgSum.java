package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.AbstractAggregateFunction;

abstract public class AvgSum<T> extends AbstractAggregateFunction<T>{

	boolean isAvg;
	
	public AvgSum(boolean isAvg) {
		super (isAvg?"AVG":"SUM");
		this.isAvg = isAvg;
	}

	public boolean isAvg(){
		return isAvg;
	}
	
}
