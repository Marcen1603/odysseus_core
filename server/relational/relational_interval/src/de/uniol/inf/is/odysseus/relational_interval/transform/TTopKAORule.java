package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopKAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalTopKPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TTopKAORule extends
		AbstractRelationalIntervalTransformationRule<TopKAO> {

	@Override
	public void execute(TopKAO operator, TransformationConfiguration config)
			throws RuleException {
		RelationalTopKPO<Tuple<ITimeInterval>,ITimeInterval> po = new RelationalTopKPO<>(
				operator.getInputSchema(0), operator.getScoringFunction().expression,
				operator.getK(), operator.isDescending(), operator.isSuppressDuplicates());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super TopKAO> getConditionClass() {
		return TopKAO.class;
	}

}
