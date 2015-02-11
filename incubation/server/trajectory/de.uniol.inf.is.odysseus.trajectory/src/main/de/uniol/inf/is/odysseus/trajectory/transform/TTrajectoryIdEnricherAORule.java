package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryIdEnricherAO;
import de.uniol.inf.is.odysseus.trajectory.physical.TrajectoryIdEnricherPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTrajectoryIdEnricherAORule extends AbstractTransformationRule<TrajectoryIdEnricherAO> {

	@Override
	public void execute(TrajectoryIdEnricherAO operator,
			TransformationConfiguration config) throws RuleException {
		
		this.defaultExecute(operator, new TrajectoryIdEnricherPO<Tuple<ITimeInterval>>(), config, true, true);
	}

	@Override
	public boolean isExecutable(TrajectoryIdEnricherAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "TrajectoryIdEnricherAO -> TrajectoryIdEnricherPO";
	}

}
