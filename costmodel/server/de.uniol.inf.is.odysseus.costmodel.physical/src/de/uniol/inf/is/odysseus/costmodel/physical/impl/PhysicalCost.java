package de.uniol.inf.is.odysseus.costmodel.physical.impl;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.costmodel.Cost;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.physical.IPhysicalCost;

public class PhysicalCost extends Cost<IPhysicalOperator> implements IPhysicalCost {

	PhysicalCost(Map<IPhysicalOperator, DetailCost> detailCostMap) {
		super(detailCostMap);
	}

}
