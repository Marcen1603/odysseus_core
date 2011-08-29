package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface ICost extends Comparable<ICost>{

	public Collection<IPhysicalOperator> getOperators();
	public ICost getCostOfOperator(IPhysicalOperator operator);
	
	public ICost merge( ICost otherCost );
	public ICost substract( ICost otherCost );
}
