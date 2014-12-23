package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.physicaloperator.KeyValueSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueSelectRule extends AbstractTransformationRule<SelectAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException {
		KeyValueSelectPO<?> keyValueSelectPO = new KeyValueSelectPO(operator.getPredicate());
		defaultExecute(operator, keyValueSelectPO, config, true, false);
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == KeyValueObject.class || 
				operator.getInputSchema().getType() == NestedKeyValueObject.class) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "SelectAO --> KeyValueSelectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super SelectAO> getConditionClass() {	
		return SelectAO.class;
	}

}
