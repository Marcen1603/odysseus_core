package de.uniol.inf.is.odysseus.costmodel.logical.impl;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.costmodel.Cost;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.logical.ILogicalCost;

public class LogicalCost extends Cost<ILogicalOperator> implements ILogicalCost {

	LogicalCost(Map<ILogicalOperator, DetailCost> detailCostMap) {
		super(detailCostMap);
	}

}
