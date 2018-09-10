package de.uniol.inf.is.odysseus.systemload_relational.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.logicaloperator.SystemLoadToPayloadAO;
import de.uniol.inf.is.odysseus.systemload_relational.physical.SystemLoadToPayloadPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSystemLoadToPayloadAORule extends
		AbstractTransformationRule<SystemLoadToPayloadAO> {

	@Override
	public void execute(SystemLoadToPayloadAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator,
				new SystemLoadToPayloadPO<ISystemLoad, Tuple<ISystemLoad>>(
						operator), config, true, false);
	}

	@Override
	public boolean isExecutable(SystemLoadToPayloadAO operator,
			TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class
				&& operator.getInputSchema(0).hasMetatype(
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
