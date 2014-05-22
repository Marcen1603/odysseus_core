package de.uniol.inf.is.odysseus.systemload.rule;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.BufferedFilterPO;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoadMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBufferedFilterPOAddSystemLoadMetadataMergeRule extends AbstractTransformationRule<BufferedFilterPO<?,?>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(BufferedFilterPO operator, TransformationConfiguration config) throws RuleException {
		((CombinedMergeFunction) operator.getMetadataMerge()).add(new SystemLoadMergeFunction());
	}

	@Override
	public boolean isExecutable(BufferedFilterPO<?, ?> operator, TransformationConfiguration config) {
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
	public Class<? super BufferedFilterPO<?, ?>> getConditionClass() {
		return BufferedFilterPO.class;
	}
	
	@Override
	public String getName() {
		return "BufferedFilterPO add MetadataMerge (ISystemLoad)";
	}
}
