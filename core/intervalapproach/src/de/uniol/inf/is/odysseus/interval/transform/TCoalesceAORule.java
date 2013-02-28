package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CoalesceAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.AbstractCoalescePO;
import de.uniol.inf.is.odysseus.intervalapproach.GroupCoalescePO;
import de.uniol.inf.is.odysseus.intervalapproach.PredicateCoalescePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TCoalesceAORule extends AbstractTransformationRule<CoalesceAO> {

	@Override
	public int getPriority() {
		// Must be higher than TStreamGroupingWithAggregation 
		return 10;
	}

	@Override
	public void execute(CoalesceAO operator, TransformationConfiguration config) {
		AbstractCoalescePO<ITimeInterval> po = null;
		if (operator.getPredicate() == null){
			int maxElementsPerGroup = operator.getMaxElementsPerGroup();
			po = new GroupCoalescePO<ITimeInterval>(operator.getInputSchema(0), operator.getOutputSchemaIntern(0) , operator.getGroupingAttributes(),
				operator.getAggregations(), maxElementsPerGroup);
			((GroupCoalescePO<ITimeInterval>)po).setCreateOnHeartbeat(operator.isCreateOnHeartbeat());
		}else{
			po = new PredicateCoalescePO<ITimeInterval>(operator.getInputSchema(0), operator.getOutputSchemaIntern(0) , operator.getGroupingAttributes(),
					operator.getAggregations(), operator.getPredicate());			
		}
		// TODO: Think about it
		//po.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(CoalesceAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "CoalesceAO --> CoalescePO";
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
