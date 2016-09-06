package de.uniol.inf.is.odysseus.incubation.graph.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.GraphBuilderDebsAO;
import de.uniol.inf.is.odysseus.incubation.graph.physicaloperator.GraphBuilderDebsPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TGraphBuilderDebsAORule extends AbstractTransformationRule<GraphBuilderDebsAO> {
	
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(GraphBuilderDebsAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new GraphBuilderDebsPO<ITimeInterval>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(GraphBuilderDebsAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super GraphBuilderDebsAO> getConditionClass() {
		return GraphBuilderDebsAO.class;
	}
}
