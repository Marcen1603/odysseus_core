package de.uniol.inf.is.odysseus.systemload.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.logicaloperator.SystemLoadAO;
import de.uniol.inf.is.odysseus.systemload.physicaloperator.SystemLoadPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSystemLoadAORule extends
		AbstractTransformationRule<SystemLoadAO> {

	@Override
	public void execute(SystemLoadAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator,
				new SystemLoadPO<IStreamObject<ISystemLoad>>(
						operator.getLoadName()), config, true, false);
	}

	@Override
	public boolean isExecutable(SystemLoadAO operator,
			TransformationConfiguration config) {
		if (operator.getInputSchema(0).hasMetatype(
						ISystemLoad.class)) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
