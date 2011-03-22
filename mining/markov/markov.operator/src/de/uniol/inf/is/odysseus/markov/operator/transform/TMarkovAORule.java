package de.uniol.inf.is.odysseus.markov.operator.transform;

import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.markov.operator.logical.MarkovAO;
import de.uniol.inf.is.odysseus.markov.operator.logical.MarkovGroupingHelper;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMarkovAORule extends AbstractTransformationRule<MarkovAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(MarkovAO operator, TransformationConfiguration config) {
		StreamGroupingWithAggregationPO<ITimeInterval,IMetaAttributeContainer<ITimeInterval>> po = new StreamGroupingWithAggregationPO<ITimeInterval,IMetaAttributeContainer<ITimeInterval>>(operator.getInputSchema(), operator.getOutputSchema(), operator.getGroupingAttributes(),
				operator.getAggregations());
		po.setOutputSchema(operator.getOutputSchema());
		po.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		po.setGroupingHelper(new MarkovGroupingHelper(operator.getInputSchema(), operator.getOutputSchema(), operator.getGroupingAttributes(),
				operator.getAggregations(), operator.getHiddenMarkovModel()));			
		replace(operator, po, config);
		retract(operator);

	}

	@Override
	public boolean isExecutable(MarkovAO operator, TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "MarkovAO -> AggregatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
