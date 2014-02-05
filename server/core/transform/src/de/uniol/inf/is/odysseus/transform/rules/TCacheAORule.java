package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.CacheAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.CachePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCacheAORule extends AbstractTransformationRule<CacheAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(CacheAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new CachePO<>(operator.getMaxElements()), config, true, true);
	}

	@Override
	public boolean isExecutable(CacheAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "CacheAO --> CachePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super CacheAO> getConditionClass() {
		return CacheAO.class;
	}
}
