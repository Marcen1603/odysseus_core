package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;

abstract public class AvgSum<T> extends AbstractAggregateFunction<T>{

	boolean isAvg;
	
	protected AvgSum(boolean isAvg) {
		super (isAvg?"AVG":"SUM");
		this.isAvg = isAvg;
	}

	public boolean isAvg(){
		return isAvg;
	}
		
	
}
