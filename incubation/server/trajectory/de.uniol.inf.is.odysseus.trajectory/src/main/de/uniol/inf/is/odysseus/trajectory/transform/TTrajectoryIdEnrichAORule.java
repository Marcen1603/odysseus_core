package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryIdEnrichAO;
import de.uniol.inf.is.odysseus.trajectory.physicaloperator.TrajectoryIdEnrichPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTrajectoryIdEnrichAORule extends AbstractTransformationRule<TrajectoryIdEnrichAO> {

	@Override
	public void execute(TrajectoryIdEnrichAO operator,
			TransformationConfiguration config) throws RuleException {
		
		this.defaultExecute(operator, new TrajectoryIdEnrichPO<Tuple<ITimeInterval>>(), config, true, true);
	}

	@Override
	public boolean isExecutable(TrajectoryIdEnrichAO operator,
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
