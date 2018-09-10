package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ConsoleSinkAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ConsoleSinkPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TConsoleSinkAORule extends
		AbstractTransformationRule<ConsoleSinkAO> {

	@Override
	public void execute(ConsoleSinkAO operator,
			TransformationConfiguration config) throws RuleException {
		ConsoleSinkPO physical = new ConsoleSinkPO();
		physical.setDumpPunctuation(operator.isDumpPunctuation());
		physical.setPrintPort(operator.isPrintPort());
		defaultExecute(operator, physical, config, true, true);
	}

	@Override
	public boolean isExecutable(ConsoleSinkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
