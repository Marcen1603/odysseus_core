package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.physicaloperator.KeyValueProjectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueProjectRule extends AbstractTransformationRule<ProjectAO>{

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(ProjectAO operator, TransformationConfiguration config) throws RuleException {
		KeyValueProjectPO<KeyValueObject<IMetaAttribute>> projectPO = new KeyValueProjectPO<KeyValueObject<IMetaAttribute>>(operator.getPaths());
		defaultExecute(operator, projectPO, config, true, false);
	}

	@Override
	public boolean isExecutable(ProjectAO operator, TransformationConfiguration config) {
		return operator.getInputSchema().getType().getSimpleName().equals("KeyValueObject") && operator.isAllPhysicalInputSet();
//		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ProjectAO --> KeyValueProjectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
