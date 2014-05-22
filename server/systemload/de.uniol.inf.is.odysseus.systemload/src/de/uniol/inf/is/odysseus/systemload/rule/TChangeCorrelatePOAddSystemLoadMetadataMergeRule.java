package de.uniol.inf.is.odysseus.systemload.rule;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.ChangeCorrelatePO;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoadMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TChangeCorrelatePOAddSystemLoadMetadataMergeRule extends AbstractTransformationRule<ChangeCorrelatePO<?,?>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(ChangeCorrelatePO operator, TransformationConfiguration config) throws RuleException {
		((CombinedMergeFunction) operator.getMetadataMerge()).add(new SystemLoadMergeFunction());
	}

	@Override
	public boolean isExecutable(ChangeCorrelatePO<?, ?> operator, TransformationConfiguration config) {
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
	public Class<? super ChangeCorrelatePO<?, ?>> getConditionClass() {
		return ChangeCorrelatePO.class;
	}
	
	@Override
	public String getName() {
		return "ChangeCorrelatePO add MetadataMerge (ISystemLoad)";
	}
}
