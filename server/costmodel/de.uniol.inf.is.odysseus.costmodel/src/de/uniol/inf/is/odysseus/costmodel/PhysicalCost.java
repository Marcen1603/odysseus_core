package de.uniol.inf.is.odysseus.costmodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.impl.Cost;
import de.uniol.inf.is.odysseus.costmodel.impl.DetailCost;

public class PhysicalCost extends Cost<IPhysicalOperator> {

	PhysicalCost(Map<IPhysicalOperator, DetailCost> detailCostMap) {
		super(detailCostMap);
	}


}
