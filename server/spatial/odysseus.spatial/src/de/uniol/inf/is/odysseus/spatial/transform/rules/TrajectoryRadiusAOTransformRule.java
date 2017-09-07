package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.TrajectoryRadiusAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.TrajectoryRadiusPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TrajectoryRadiusAOTransformRule extends AbstractTransformationRule<TrajectoryRadiusAO> {

	@Override
	public void execute(TrajectoryRadiusAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new TrajectoryRadiusPO<Tuple<ITimeInterval>>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(TrajectoryRadiusAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
