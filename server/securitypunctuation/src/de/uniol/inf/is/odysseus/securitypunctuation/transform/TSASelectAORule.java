package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SASelectAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SASelectPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSASelectAORule extends AbstractTransformationRule<SASelectAO> {
	
	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SASelectAO saSelectAO, TransformationConfiguration config) throws RuleException {
		SASelectPO<?> saSelectPO=new SASelectPO(saSelectAO.getPredicate());
		defaultExecute(saSelectAO, saSelectPO, config, true, true);

	}

	@Override
	public boolean isExecutable(SASelectAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super SASelectAO> getConditionClass() {
		return SASelectAO.class;
	}
	
	@Override
	public String getName() {
		return "SASelectAO -> SASelectPO";
	}

}
