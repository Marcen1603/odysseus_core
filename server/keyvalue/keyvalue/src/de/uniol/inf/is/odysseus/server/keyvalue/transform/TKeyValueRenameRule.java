package de.uniol.inf.is.odysseus.server.keyvalue.transform;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator.KeyValueRenamePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueRenameRule extends AbstractTransformationRule<RenameAO> {

	@Override
	public void execute(RenameAO renameAO, TransformationConfiguration config)
			throws RuleException {
		KeyValueRenamePO<KeyValueObject<IMetaAttribute>, IMetaAttribute> renamePO = new KeyValueRenamePO<KeyValueObject<IMetaAttribute>, IMetaAttribute>(renameAO);
		defaultExecute(renameAO, renamePO, config, true, true, true);
	}

	@Override
	public boolean isExecutable(RenameAO operator,
			TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == KeyValueObject.class) &&
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
