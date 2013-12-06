package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CouldNotPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.CostModelService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.PlanProOperatorPartitioner;

public class OperatorCostsCalculator {

	private final PlanProOperatorPartitioner operatorPartitioner;

	public OperatorCostsCalculator(PlanProOperatorPartitioner operatorPartitioner) {
		super();
		this.operatorPartitioner = operatorPartitioner;
	}

	@SuppressWarnings("unchecked")
	public Map<String, CostSummary> calcCostsProOperator(ILogicalOperator query, String transCfgName) {
		try {
			Map<String, CostSummary> operatorCost = new HashMap<>();

			int i = 0;
			List<SubPlan> parts = operatorPartitioner.partitionWithDummyOperators(query, null, null);

			for (SubPlan part : parts) {
				if (part.getOperators().isEmpty() || skipOperator(part))
					continue;
				ILogicalQuery q = Helper.transformToQuery(part.getOperators().get(0), "subplan_" + i);
				IPhysicalQuery p = Helper.getPhysicalQuery(q, transCfgName);
				String id = getOperatorId(part);

				ICost<IPhysicalOperator> cost = CostModelService.get().estimateCost(p.getPhysicalChilds(), false);
				OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);

				double cpuCost = c.getCpuCost();
				double memCost = c.getMemCost();
				operatorCost.put(id, new CostSummary(id, cpuCost, memCost, query));
				i++;

			}
			return operatorCost;
		} catch (CouldNotPartitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean skipOperator(SubPlan part) {
		// return (part.getOperators().get(0) instanceof AccessAO) ||
		// (part.getOperators().get(0) instanceof TimestampAO);
		return (part.getOperators().get(0) instanceof AccessAO);
	}

	private String getOperatorId(SubPlan part) {
		return Helper.getId(part.getOperators().get(0));
	}
}
