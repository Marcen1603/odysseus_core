package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.CacheAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.CacheTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TCacheAORule extends AbstractIntervalTransformationRule<CacheAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(CacheAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new CacheTIPO<>(operator.getTimeSize(), operator.getBaseTimeUnit()), config, true, true);
	}

	@Override
	public boolean isExecutable(CacheAO operator,
			TransformationConfiguration config) {
		return operator.getTimeSize() != null && operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "CacheAO --> CacheTIPO";
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
