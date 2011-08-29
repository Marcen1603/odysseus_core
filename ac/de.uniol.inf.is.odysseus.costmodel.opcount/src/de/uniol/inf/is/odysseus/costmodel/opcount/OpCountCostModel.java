package de.uniol.inf.is.odysseus.costmodel.opcount;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

public class OpCountCostModel implements ICostModel {

	@Override
	public ICost getMaximumCost() {
		return new OpCountCost(5);
	}

	@Override
	public ICost estimateCost(List<IPhysicalOperator> operators, boolean useQuerySharing) {
		return new OpCountCost(operators);
	}

	@Override
	public ICost getZeroCost() {
		return new OpCountCost(0);
	}

}
