package de.uniol.inf.is.odysseus.server.xml.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.xml.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.server.xml.physicaloperator.JoinPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TJoinAORule extends AbstractTransformationRule<JoinAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinAO operator, TransformationConfiguration config) throws RuleException {
		JoinPO<IMetaAttribute> po = new JoinPO<IMetaAttribute>(operator.getPredicate(), operator.getMinimumSize());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "JoinAO -> JoinPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

}
