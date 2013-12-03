package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyValidatorRule extends
		AbstractTransformationRule<IHasMetadataMergeFunction<?>> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(IHasMetadataMergeFunction<?> operator,
			TransformationConfiguration config) {
		if (!((CombinedMergeFunction)operator.getMetadataMerge()).providesMergeFunctionFor(ILatency.class)){
			// TODO: Make logger
			System.err.println(this+" WARN: No Latency merge function set for "+operator);
		}
	}

	@Override
	public boolean isExecutable(IHasMetadataMergeFunction<?> operator,
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
		return "Latency Validation";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.VALIDATE;
	}

	

}
