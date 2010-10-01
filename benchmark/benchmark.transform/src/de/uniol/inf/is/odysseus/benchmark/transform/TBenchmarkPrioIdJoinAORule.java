package de.uniol.inf.is.odysseus.benchmark.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.benchmarker.impl.PrioIdJoinAO;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.metadata.base.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.priority_interval.PriorityIdHashSweepArea;
import de.uniol.inf.is.odysseus.priority_interval.PriorityTITransferArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBenchmarkPrioIdJoinAORule extends AbstractTransformationRule<PrioIdJoinAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(PrioIdJoinAO operator,
			TransformationConfiguration config) {
		JoinTIPO joinPO = new JoinTIPO();
		joinPO.setTransferFunction(new PriorityTITransferArea());
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		joinPO.setOutputSchema(operator.getOutputSchema() == null?null:operator.getOutputSchema().clone()); 
	 	ITemporalSweepArea[] areas = new ITemporalSweepArea[2];
		areas[0] = new PriorityIdHashSweepArea(operator.getRightPos(), operator.getLeftPos());
		areas[1] = new PriorityIdHashSweepArea(operator.getLeftPos(), operator.getRightPos());
	 	joinPO.setAreas(areas);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, joinPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(operator);
		insert(joinPO);
	}

	@Override
	public boolean isExecutable(PrioIdJoinAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPriority.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval" && metaTypes contains "de.uniol.inf.is.odysseus.priority.IPriority" )
//		algebraOp : PrioIdJoinAO(allPhysicalInputSet == true)
	}

	@Override
	public String getName() {
		return "PrioIdJoinAO -> JoinTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
