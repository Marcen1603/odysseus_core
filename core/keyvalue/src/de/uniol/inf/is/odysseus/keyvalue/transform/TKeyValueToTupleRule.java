package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.logicaloperator.KeyValueToTupleAO;
import de.uniol.inf.is.odysseus.keyvalue.physicaloperator.KeyValueToTuplePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueToTupleRule extends AbstractTransformationRule<KeyValueToTupleAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(KeyValueToTupleAO operator,
			TransformationConfiguration config) {
		defaultExecute(operator, new KeyValueToTuplePO<IMetaAttribute>(operator.isKeepInputObject()), config, true, false);
	}

	@Override
	public boolean isExecutable(KeyValueToTupleAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "KeyValueToTupleAO --> KeyValueToTuplePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
