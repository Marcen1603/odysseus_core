package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MemStoreSourceAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MemStoreSourcePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMemStoreReadAORule extends AbstractTransformationRule<MemStoreSourceAO> {

	@Override
	public void execute(MemStoreSourceAO operator, TransformationConfiguration config) throws RuleException {
		MemStoreSourcePO<IStreamObject<IMetaAttribute>> po = new MemStoreSourcePO<>(getDataDictionary(), operator.getStore());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(MemStoreSourceAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
