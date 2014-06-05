package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.logicaloperator.KeyValueToTupleAO;
import de.uniol.inf.is.odysseus.keyvalue.physicaloperator.KeyValueToTuplePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueToTupleRule extends AbstractTransformationRule<KeyValueToTupleAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(KeyValueToTupleAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new KeyValueToTuplePO<IMetaAttribute>(operator.isKeepInputObject(), operator.getOutputSchema()), config, true, false);
	}

	@Override
	public boolean isExecutable(KeyValueToTupleAO operator, TransformationConfiguration config) {
		String type = operator.getInputSchema().getType().getSimpleName();
		return (type.equals("KeyValueObject") || type.equals("NestedKeyValueObject")) && operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "KeyValueToTupleAO --> KeyValueToTuplePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super KeyValueToTupleAO> getConditionClass() {
		return KeyValueToTupleAO.class;
	}
}
