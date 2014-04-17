package de.uniol.inf.is.odysseus.costmodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.costmodel.impl.Cost;
import de.uniol.inf.is.odysseus.costmodel.impl.DetailCost;

public class LogicalCost extends Cost<ILogicalOperator> {

	LogicalCost(Map<ILogicalOperator, DetailCost> detailCostMap) {
		super(detailCostMap);
	}

}
