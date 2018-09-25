package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SetTimeProgessMarkerAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SetTimeProgessMarkerPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSetTimeProgressMarker extends AbstractTransformationRule<SetTimeProgessMarkerAO> {

	@Override
	public void execute(SetTimeProgessMarkerAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new SetTimeProgessMarkerPO<IStreamObject<?>>(operator.getValue()), config, true, true);
	}

	@Override
	public boolean isExecutable(SetTimeProgessMarkerAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SetTimeProgessMarkerAO> getConditionClass() {
		return SetTimeProgessMarkerAO.class;
	}
	
}
