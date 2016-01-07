package de.uniol.inf.is.odysseus.debsgc2016.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.debsgc2016.logicaloperator.GCQuery1AO;
import de.uniol.inf.is.odysseus.debsgc2016.physicaloperator.GCQuery1PO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class GCQuery1TransformationRule extends AbstractTransformationRule<GCQuery1AO> {

	@Override
	public void execute(GCQuery1AO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new GCQuery1PO(), config, true, true);
	}

	@Override
	public boolean isExecutable(GCQuery1AO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
