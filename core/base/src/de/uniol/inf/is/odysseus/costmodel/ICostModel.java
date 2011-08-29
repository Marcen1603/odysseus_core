package de.uniol.inf.is.odysseus.costmodel;

import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public interface ICostModel {

	public ICost estimateCost(List<IPhysicalOperator> operators, boolean onUpdate );
	
	public ICost getMaximumCost();
	public ICost getZeroCost();
	
}
