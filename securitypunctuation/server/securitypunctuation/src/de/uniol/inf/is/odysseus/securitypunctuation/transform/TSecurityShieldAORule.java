package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SecurityShieldAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSecurityShieldAORule extends AbstractTransformationRule<SecurityShieldAO>{

	@Override	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(SecurityShieldAO securityShieldAO, TransformationConfiguration config) throws RuleException {
	
		List<? extends IRole> roles=securityShieldAO.getOwner().get(0).getSession().getUser().getRoles();
		SecurityShieldPO<?,?> securityShieldPO=new SecurityShieldPO(securityShieldAO.getTupleRangeAttribute(),roles);
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
