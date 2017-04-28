package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SASelectAO;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SecurityShieldAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SPGeneratorPO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSecurityShieldAORule extends AbstractTransformationRule<SecurityShieldAO>{

	@Override
	public void execute(SecurityShieldAO securityShieldAO, TransformationConfiguration config) throws RuleException {
		@SuppressWarnings("rawtypes")
		SecurityShieldPO<?> securityShieldPO=new SecurityShieldPO();
		defaultExecute(securityShieldAO, securityShieldPO, config, true, true);
		
	}

	@Override
	public boolean isExecutable(SecurityShieldAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	@Override
	public Class<? super SecurityShieldAO> getConditionClass() {
		return SecurityShieldAO.class;
	}
	
	@Override
	public String getName() {
		return "SecurityShieldAO -> SecurityShieldPO";
	
	}
}
