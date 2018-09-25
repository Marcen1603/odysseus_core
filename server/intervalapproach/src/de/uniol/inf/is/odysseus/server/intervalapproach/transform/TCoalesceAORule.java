package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.AbstractCoalescePO;
import de.uniol.inf.is.odysseus.server.intervalapproach.GroupCoalescePO;
import de.uniol.inf.is.odysseus.server.intervalapproach.PredicateCoalescePO;
import de.uniol.inf.is.odysseus.server.intervalapproach.StartStopPredicateCoalescePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TCoalesceAORule extends
		AbstractIntervalTransformationRule<CoalesceAO> {

	@Override
	public int getPriority() {
		// Must be higher than TStreamGroupingWithAggregation
		return 10;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(CoalesceAO operator, TransformationConfiguration config)
			throws RuleException {
		IPhysicalOperator po = null;
		if (operator.getStartPredicate() != null) {
			po = new StartStopPredicateCoalescePO<ITimeInterval>(
					operator.getInputSchema(0),
					operator.getOutputSchemaIntern(0),
					operator.getGroupingAttributes(),
					operator.getAggregations(), operator.getStartPredicate(),
					operator.getEndPredicate());
		} else if (operator.getPredicate() == null) {
			int maxElementsPerGroup = operator.getMaxElementsPerGroup();
			po = new GroupCoalescePO<ITimeInterval>(operator.getInputSchema(0),
					operator.getOutputSchemaIntern(0),
					operator.getGroupingAttributes(),
					operator.getAggregations(), maxElementsPerGroup);
			((GroupCoalescePO<ITimeInterval>) po).setCreateOnHeartbeat(operator
					.isCreateOnHeartbeat());
		} else {
			po = new PredicateCoalescePO<ITimeInterval>(
					operator.getInputSchema(0),
					operator.getOutputSchemaIntern(0),
					operator.getGroupingAttributes(),
					operator.getAggregations(), operator.getPredicate());
		}
		if (po instanceof AbstractCoalescePO) {
			((AbstractCoalescePO<ITimeInterval>) po).setHeartbeatRate(operator
					.getHeartbeatrate());
		}
		// TODO: Think about it
		// po.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super CoalesceAO> getConditionClass() {
		return CoalesceAO.class;
	}
}
