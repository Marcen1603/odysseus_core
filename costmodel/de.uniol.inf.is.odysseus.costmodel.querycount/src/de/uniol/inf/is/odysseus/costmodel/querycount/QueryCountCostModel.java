package de.uniol.inf.is.odysseus.costmodel.querycount;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;

/**
 * Repräsentiert das Kostenmodell nach Anfragezahl.
 * 
 * @author Timo Michelsen
 *
 */
public class QueryCountCostModel implements ICostModel {

	@Override
	public ICost getMaximumCost() {
		return new QueryCountCost(3);
	}

	@Override
	public ICost estimateCost(List<IPhysicalOperator> operators, boolean useQuerySharing) {
		return new QueryCountCost(operators);
	}

	@Override
	public ICost getZeroCost() {
		return new QueryCountCost(0.0);
	}

}
