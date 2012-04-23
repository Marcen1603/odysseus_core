package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.RenamePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TRenameAORule extends AbstractTransformationRule<RenameAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(RenameAO operator, TransformationConfiguration config) {
		@SuppressWarnings({"rawtypes" })
		RenamePO<?> renamePO = new RenamePO();
		renamePO.setOutputSchema(operator.getOutputSchema());
		replace(operator, renamePO, config);		
		retract(operator);
	}

	@Override
	public boolean isExecutable(RenameAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "RenameAO --> RenamePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<?> getConditionClass() {
		return RenameAO.class;
	}
	
}
