package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SAProjectAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAProjectPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAProjectAORule extends AbstractTransformationRule<SAProjectAO> {
	@Override
	public int getPriority() {
		return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(SAProjectAO saProjectAO, TransformationConfiguration config) throws RuleException {
		SAProjectPO<?> saProjectPO=new SAProjectPO(saProjectAO.determineRestrictList(),saProjectAO.getRestrictedAttributes());
		defaultExecute(saProjectAO, saProjectPO, config, true, true);
		
	}
	

	@Override
	public boolean isExecutable(SAProjectAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super SAProjectAO> getConditionClass() {
		return SAProjectAO.class;
	}
	
	@Override
	public String getName() {
		return "SAProjectAO -> SAProjectPO";
	}


}
