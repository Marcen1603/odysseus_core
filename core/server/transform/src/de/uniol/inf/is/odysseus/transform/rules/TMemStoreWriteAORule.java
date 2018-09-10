package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MemStoreWriteAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MemStoreWritePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMemStoreWriteAORule extends AbstractTransformationRule<MemStoreWriteAO> {

	@Override
	public void execute(MemStoreWriteAO operator, TransformationConfiguration config) throws RuleException {
		MemStoreWritePO<IStreamObject<IMetaAttribute>> po = new MemStoreWritePO<>(getDataDictionary(), operator.getStore(), operator.isClearStore());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(MemStoreWriteAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
