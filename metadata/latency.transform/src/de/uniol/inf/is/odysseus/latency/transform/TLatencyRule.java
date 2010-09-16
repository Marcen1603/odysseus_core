package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.metadata.base.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyRule extends AbstractTransformationRule<JoinTIPO<?,?>> {

	@Override
	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(JoinTIPO<?, ?> joinPO, TransformationConfiguration config) {
		((CombinedMergeFunction)joinPO.getMetadataMerge()).add(new LatencyMergeFunction());		
	}

	@Override
	public boolean isExecutable(JoinTIPO<?, ?> joinPO, TransformationConfiguration config) {
		if(joinPO.getMetadataMerge() instanceof CombinedMergeFunction){
			if(config.getMetaTypes().contains("de.uniol.inf.is.odysseus.latency.ILatency")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return  "JoinTIPO add MetadataMerge (ILatency)";
	}
	
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	
}
