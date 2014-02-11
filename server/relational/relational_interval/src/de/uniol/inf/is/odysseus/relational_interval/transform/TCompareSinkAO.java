package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.CompareSinkAO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.CompareSinkPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TCompareSinkAO extends AbstractTransformationRule<CompareSinkAO> {

	@Override
	public void execute(CompareSinkAO operator,
			TransformationConfiguration config) throws RuleException {
		CompareSinkPO po = new CompareSinkPO();
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(CompareSinkAO operator,
			TransformationConfiguration config) {
		return operator.getInputSchema(0).getType() == Tuple.class
				&& operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super CompareSinkAO> getConditionClass() {
		return CompareSinkAO.class;
	}
	
}
