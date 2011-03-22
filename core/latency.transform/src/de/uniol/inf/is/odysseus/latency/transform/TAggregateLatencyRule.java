package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.intervalapproach.AggregateTIPO;

public class TAggregateLatencyRule extends
		AbstractTransformationRule<AggregateTIPO<?, ?, ?>> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AggregateTIPO<?, ?, ?> operator,
			TransformationConfiguration config) {
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new LatencyMergeFunction());		
		
	}

	@Override
	public boolean isExecutable(AggregateTIPO<?, ?,?> operator,
			TransformationConfiguration config) {
		if(operator.getMetadataMerge() instanceof CombinedMergeFunction){
			if(config.getMetaTypes().contains("de.uniol.inf.is.odysseus.latency.ILatency")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateTIPO add MetadataMerge (ILatency)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
