package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardexecutioncostmodel;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.ByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.ICostCalculator;
import de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel.IPlanExecutionCostModel;

/**
 * 
 * @author Tobias Witt
 *
 */
public class PlanExecutionCostModel implements IPlanExecutionCostModel {
	
	private Map<Class<?>, PlanExecutionCost> operatorCost;

	public PlanExecutionCostModel() {
		this.operatorCost = new HashMap<Class<?>, PlanExecutionCost>();
		
		// base
		this.operatorCost.put(SelectPO.class, 				new PlanExecutionCost(1, 20, 1, 1));
		this.operatorCost.put(MetadataCreationPO.class, 	new PlanExecutionCost(1, 5, 1, 1));
		// access
		this.operatorCost.put(ByteBufferReceiverPO.class, 	new PlanExecutionCost(1, 6, 1, 1));
		// intervalapproach
		this.operatorCost.put(JoinTIPO.class, 				new PlanExecutionCost(1, 30, 10, 1));
		// relational
		this.operatorCost.put(RelationalProjectPO.class, 	new PlanExecutionCost(1, 2, 1, 1));
	}
	
	@Override
	public ICostCalculator<IPhysicalOperator> getCostCalculator() {
		return new PlanExecutionCostCalculator(this);
	}
	
	PlanExecutionCost getCost(IPhysicalOperator op) {
		return new PlanExecutionCost(this.operatorCost.get(op.getClass()));
	}
	
	void calculateScore(PlanExecutionCost cost) {
		cost.setScore((int)(1.0f * cost.getCpuTime()
				+ 0.3f * cost.getLatency()
				+ 0.1f * cost.getMemoryConsumption()
				+ 0.05f * cost.getNetworkBandwidth()));
	}
}
