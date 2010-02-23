package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.standardexecutioncostmodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * 
 * @author Tobias Witt
 *
 */
public class PlanExecutionCostVisitor implements INodeVisitor<IPhysicalOperator, StandardPlanExecutionCost> {
	
	private StandardPlanExecutionCost cost;
	private StandardPlanExecutionCostModel model;
	private Stack<Double> datarates;
	private Map<IPhysicalOperator, Integer> passCount;
	
	private Logger logger;
	
	public PlanExecutionCostVisitor(StandardPlanExecutionCostModel costModel) {
		this.cost = new StandardPlanExecutionCost(0, 0, 0, 0);
		this.model = costModel;
		this.datarates = new Stack<Double>();
		this.logger = LoggerFactory.getLogger(PlanExecutionCostVisitor.class);
		this.passCount = new HashMap<IPhysicalOperator, Integer>();
	}

	@Override
	public void ascend(IPhysicalOperator to) {
		// propagate datarate bottom-up
		int numSources = ((ISink<?>)to).getSubscribedToSource().size();
		if (numSources > 1) {
			// not all input datarates collected yet?
			if (!this.passCount.containsKey(to)) {
				this.passCount.put(to, new Integer(1));
				return;
			} else {
				this.passCount.put(to, this.passCount.get(to) + 1);
				if (this.passCount.get(to) < numSources) {
					return;
				}
			}
		}
		Double datarate = 0.0;
		for (int i=0; i<numSources; i++) {
			datarate += this.datarates.pop();
		}
		IMonitoringData<Double> selectivity = to.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
		if (selectivity!=null) {
			StandardPlanExecutionCost opCost = this.model.getCost(to);
			if (opCost!=null) {
				opCost.scaleWithDatarate(datarate.floatValue());
				this.cost.add(opCost);
				this.logger.debug("adding cost "+opCost.getCpuTime()+", operator "+to.getName()+", input datarate: "+datarate+", output datarate: "+(datarate+selectivity.getValue()));
			} else {
				this.logger.warn("No cost defined for operator "+to.getName()+".");
			}
			datarate *= selectivity.getValue();
		} else if (to.isPipe()) {
			this.logger.warn("Operator "+to.getName()+" has no selectivity monitoring data.");
		}
		this.datarates.push(datarate);
	}

	@Override
	public void descend(IPhysicalOperator to) {
	}
	
	@Override
	public StandardPlanExecutionCost getResult() {
		this.model.calculateScore(this.cost);
		//this.logger.debug("total plan score: "+this.cost.getScore());
		return this.cost;
	}

	@Override
	public void node(IPhysicalOperator op) {
		// get input datarate from source
		if (!op.isSink()) {
			IMonitoringData<Double> datarate = op.getMonitoringData(MonitoringDataTypes.DATARATE.name);
			if (datarate!=null) {
				StandardPlanExecutionCost opCost = this.model.getCost(op);
				if (opCost!=null) {
					opCost.scaleWithDatarate(datarate.getValue().floatValue());
					this.cost.add(opCost);
					this.logger.debug("adding cost "+opCost.getCpuTime()+", operator "+op.getName()+", ouput datarate: "+datarate.getValue());
				} else {
					this.logger.warn("No cost defined for operator "+op.getName()+".");
				}
				this.datarates.push(datarate.getValue());
			} else {
				this.logger.warn("Source "+op.getName()+" has no datarate monitoring data.");
				this.datarates.push(new Double(0.0));
			}
		}
	}

}
