package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.calculator.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModel;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.PlanProOperatorPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.SubPlanManipulator;

public class OperatorCostsCalculator {
	private final IServerExecutor executor;
	private final IPQLGenerator generator;
	@SuppressWarnings("rawtypes")
	private final OperatorCostModel costModel;
	@SuppressWarnings("unused")
	private final SubPlanManipulator manipulator;

	@SuppressWarnings("rawtypes")
	public OperatorCostsCalculator(IServerExecutor executor, IPQLGenerator generator, OperatorCostModel costModel, SubPlanManipulator manipulator) {
		super();
		this.executor = executor;
		this.generator = generator;
		this.costModel = costModel;
		this.manipulator = manipulator;
	}

	@SuppressWarnings("unchecked")
	public Map<String, CostSummary> calcCostsProOperator(ILogicalOperator query, String transCfgName) {
		Map<String, CostSummary> operatorCost = new HashMap<>();

		int i = 0;
		List<SubPlan> parts = PlanProOperatorPartitioner.partitionWithDummyOperators(query, null, null);

		for (SubPlan part : parts) {
			if (part.getOperators().isEmpty() || skipOperator(part))
				continue;
			ILogicalQuery q = Helper.transformToQuery(part.getOperators().get(0), generator, "subplan_" + i);
			IPhysicalQuery p = Helper.getPhysicalQuery(executor, q, transCfgName);
			String id = getOperatorId(part);

			ICost<IPhysicalOperator> cost = costModel.estimateCost(p.getPhysicalChilds(), false);
			OperatorCost<IPhysicalOperator> c = ((OperatorCost<IPhysicalOperator>) cost);

			double cpuCost = c.getCpuCost();
			double memCost = c.getMemCost();
			operatorCost.put(id, new CostSummary(id, cpuCost, memCost, query));
			i++;

		}
		return operatorCost;
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
