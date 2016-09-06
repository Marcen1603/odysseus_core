package de.uniol.inf.is.odysseus.incubation.graph.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.SearchNodeAO;
import de.uniol.inf.is.odysseus.incubation.graph.physicaloperator.SearchNodePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSearchNodeAORule extends AbstractTransformationRule<SearchNodeAO> {

	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(SearchNodeAO ao, TransformationConfiguration config) throws RuleException {
		defaultExecute(ao, new SearchNodePO<ITimeInterval>(ao), config, true, true);
	}

	@Override
	public boolean isExecutable(SearchNodeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	public Class<? super SearchNodeAO> getConditionClass() {
		return SearchNodeAO.class;
	}

}
