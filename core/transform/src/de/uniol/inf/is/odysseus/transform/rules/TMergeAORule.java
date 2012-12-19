package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.MergeAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MergePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMergeAORule extends AbstractTransformationRule<MergeAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(MergeAO operator, TransformationConfiguration config) {
		defaultExecute(operator, new MergePO(), config, true, true);
	}

	@Override
	public boolean isExecutable(MergeAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "MergeAO --> MergePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super MergeAO> getConditionClass() {
		return MergeAO.class;
	}

}
