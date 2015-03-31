package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.physicaloperator.KeyValueRenamePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueRenameRule extends AbstractTransformationRule<RenameAO> {

	@Override
	public void execute(RenameAO renameAO, TransformationConfiguration config)
			throws RuleException {
		KeyValueRenamePO<KeyValueObject<?>> renamePO = new KeyValueRenamePO<KeyValueObject<?>>(renameAO);
		defaultExecute(renameAO, renamePO, config, true, true, true);
	}

	@Override
	public boolean isExecutable(RenameAO operator,
			TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == KeyValueObject.class || 
				operator.getInputSchema().getType() == NestedKeyValueObject.class) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "RenameAO --> KeyValueRenamePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		return 1;
	}
	
	@Override
	public Class<? super RenameAO> getConditionClass() {	
		return RenameAO.class;
	}
}
