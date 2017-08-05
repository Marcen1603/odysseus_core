package de.uniol.inf.is.odysseus.wrapper.pmml.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.pmml.logicaloperator.PMMLModelSelectorAO;
import de.uniol.inf.is.odysseus.wrapper.pmml.physicaloperator.PMMLModelSelectorPO;

@SuppressWarnings({"rawtypes"})
public class TPMMLModelSelectorAORule extends AbstractTransformationRule<PMMLModelSelectorAO>{

	@Override
	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(PMMLModelSelectorAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new PMMLModelSelectorPO(operator.getModelName()), config, true, true);
	}

	@Override
	public boolean isExecutable(PMMLModelSelectorAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	 public String getName() {
	 return "PMMLModelSelectorAO -> PMMLModelSelectorPO";
	 }

	@Override
	 public Class<? super PMMLModelSelectorAO> getConditionClass() {
		return PMMLModelSelectorAO.class;
	 }

}
