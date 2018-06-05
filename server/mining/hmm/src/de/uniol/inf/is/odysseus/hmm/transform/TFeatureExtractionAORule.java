package de.uniol.inf.is.odysseus.hmm.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.hmm.logicaloperator.FeatureExtractionAO;
import de.uniol.inf.is.odysseus.hmm.physicaloperator.FeatureExtractionPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "rawtypes" })
public class TFeatureExtractionAORule extends
		AbstractTransformationRule<FeatureExtractionAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(FeatureExtractionAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new FeatureExtractionPO(), config, true, true);

	}

	@Override
	public boolean isExecutable(FeatureExtractionAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "FeatureExtractionAO -> FeatureExtractionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super FeatureExtractionAO> getConditionClass() {
		return FeatureExtractionAO.class;
	}
}
