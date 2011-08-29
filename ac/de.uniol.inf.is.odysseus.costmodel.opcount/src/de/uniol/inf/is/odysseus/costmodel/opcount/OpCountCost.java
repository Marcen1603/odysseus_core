package de.uniol.inf.is.odysseus.costmodel.opcount;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public class OpCountCost implements ICost {

	private Collection<IPhysicalOperator> operators;
	private final double opCount;
	
	public OpCountCost( Collection<IPhysicalOperator> operators ) {
		this.opCount = operators.size();
		this.operators = operators;
	}

	public OpCountCost( double opCount ) {
		this.opCount = operators.size();
		operators = new ArrayList<IPhysicalOperator>();
	}

	@Override
	public int compareTo(ICost o) {
		if( !(o instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);
		
		OpCountCost cost = (OpCountCost)o;
		if( opCount < cost.opCount ) return -1;
		if( opCount == cost.opCount ) return 0;
		return 1;
	}

	@Override
	public OpCountCost merge(ICost otherCost) {
		if( otherCost == null ) 
			return new OpCountCost(operators);
		
		if( !(otherCost instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);
		
		OpCountCost cost = (OpCountCost)otherCost;
		
		return new OpCountCost(opCount + cost.opCount);
	}

	@Override
	public OpCountCost substract(ICost otherCost) {
		if( otherCost == null ) 
			return new OpCountCost(opCount);

		if( !(otherCost instanceof OpCountCost))
			throw new IllegalArgumentException("o is not type " + OpCountCost.class);
		
		OpCountCost cost = (OpCountCost)otherCost;

		return new OpCountCost(opCount - cost.opCount);
	}

	@Override
	public Collection<IPhysicalOperator> getOperators() {
		return operators;
	}

	@Override
	public ICost getCostOfOperator(IPhysicalOperator operator) {
		return new OpCountCost(1.0 / opCount);
	}

}
