package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

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
			if(config.getMetaTypes().contains(ILatency.class.getCanonicalName())){
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
	
	@Override
	public Class<?> getConditionClass() {	
		return AggregateTIPO.class;
	}

}
