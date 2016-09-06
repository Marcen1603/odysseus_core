package de.uniol.inf.is.odysseus.incubation.graph.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.GraphBuilderAO;
import de.uniol.inf.is.odysseus.incubation.graph.physicaloperator.GraphBuilderPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TGraphBuilderAORule extends AbstractTransformationRule<GraphBuilderAO> {

	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(GraphBuilderAO graphBuilderAO, TransformationConfiguration config) throws RuleException {		
		defaultExecute(graphBuilderAO, new GraphBuilderPO<ITimeInterval>(graphBuilderAO), config, true, true);		
	}

	@Override
	public boolean isExecutable(GraphBuilderAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super GraphBuilderAO> getConditionClass() {
		return GraphBuilderAO.class;
	}

}
