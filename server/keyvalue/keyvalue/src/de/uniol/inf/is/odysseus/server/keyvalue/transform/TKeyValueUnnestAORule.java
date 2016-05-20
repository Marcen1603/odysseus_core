package de.uniol.inf.is.odysseus.server.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.KeyValueUnnestAO;
import de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator.KeyValueUnnestPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TKeyValueUnnestAORule extends AbstractKeyValueIntervalTransformationRule<KeyValueUnnestAO> {

	@Override
	public void execute(KeyValueUnnestAO operator, TransformationConfiguration config) throws RuleException {
		@SuppressWarnings("rawtypes")
		KeyValueUnnestPO po = new KeyValueUnnestPO(operator.getAttribute());
		defaultExecute(operator, po, config, true, true);
	}
		
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super KeyValueUnnestAO> getConditionClass() {
		return KeyValueUnnestAO.class;
	}

}
