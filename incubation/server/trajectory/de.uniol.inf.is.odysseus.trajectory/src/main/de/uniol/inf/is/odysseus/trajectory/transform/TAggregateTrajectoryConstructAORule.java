package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.AggregateTrajectoryConstructAO;
import de.uniol.inf.is.odysseus.trajectory.physical.AggregateTrajectoryConstructPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAggregateTrajectoryConstructAORule extends AbstractTransformationRule<AggregateTrajectoryConstructAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AggregateTrajectoryConstructAO operator,
			TransformationConfiguration config) throws RuleException {

		AggregateTrajectoryConstructPO trajectoryConstructPO = new AggregateTrajectoryConstructPO<>(
				operator.getInputSchema(), 
				operator.getOutputSchema(0), 
				operator.getGroupingAttributes(), 
				operator.getAggregations(), 
				operator.isFastGrouping()
		);
		trajectoryConstructPO.setDumpAtValueCount(operator.getDumpAtValueCount());
		trajectoryConstructPO.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		trajectoryConstructPO.setOutputPA(operator.isOutputPA());
		trajectoryConstructPO.setDrainAtDone(operator.isDrainAtDone());
		trajectoryConstructPO.setDrainAtClose(operator.isDrainAtClose());
		this.defaultExecute(operator, trajectoryConstructPO, config, true, true);
	}

	@Override
	public boolean isExecutable(AggregateTrajectoryConstructAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	
}
