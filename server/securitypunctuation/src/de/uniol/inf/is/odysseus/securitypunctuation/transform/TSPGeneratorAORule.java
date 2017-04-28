package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SPGeneratorAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SPGeneratorPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "rawtypes" })
public class TSPGeneratorAORule extends AbstractTransformationRule<SPGeneratorAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SPGeneratorAO spGeneratorAO, TransformationConfiguration config) throws RuleException {
		
		SPGeneratorPO<?> spGeneratorPO=new SPGeneratorPO();
		defaultExecute(spGeneratorAO, spGeneratorPO, config, true, true);

	}

	@Override
	public boolean isExecutable(SPGeneratorAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SPGeneratorAO -> SPGeneratorPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SPGeneratorAO> getConditionClass() {
		return SPGeneratorAO.class;
	}

}
