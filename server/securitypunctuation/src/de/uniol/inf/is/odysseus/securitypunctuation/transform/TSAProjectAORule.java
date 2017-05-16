package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SAProjectAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAProjectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAProjectAORule extends AbstractTransformationRule<SAProjectAO> {
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	@Override
	public int getPriority() {
		return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(SAProjectAO saProjectAO, TransformationConfiguration config) throws RuleException {
		LOG.info("ausgeführt");
		SAProjectPO<?> saProjectPO=new SAProjectPO(saProjectAO.determineRestrictList(),saProjectAO.getRestrictedAttributes());
		defaultExecute(saProjectAO, saProjectPO, config, true, true);
		
	}
	

	@Override
	public boolean isExecutable(SAProjectAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
//		return true;
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
