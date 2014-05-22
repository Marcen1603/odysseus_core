package de.uniol.inf.is.odysseus.systemload.rule;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoadMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJoinTIPOAddSystemLoadMetadataMergeRule extends AbstractTransformationRule<JoinTIPO<?,?>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(JoinTIPO operator, TransformationConfiguration config) throws RuleException {
		if(config.getMetaTypes().size()>1){
			((CombinedMergeFunction) operator.getMetadataMerge()).add(new SystemLoadMergeFunction());
		}else{
			operator.setMetadataMerge(TIMergeFunction.getInstance());
		}
	}

	@Override
	public boolean isExecutable(JoinTIPO<?, ?> operator, TransformationConfiguration config) {
		if (config.getMetaTypes().contains(ISystemLoad.class.getCanonicalName())) {
			if (operator.getMetadataMerge() != null) {
				if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super JoinTIPO<?, ?>> getConditionClass() {
		return JoinTIPO.class;
	}
	
	@Override
	public String getName() {
		return "JoinTIPO add MetadataMerge (ISystemLoad)";
	}
}
