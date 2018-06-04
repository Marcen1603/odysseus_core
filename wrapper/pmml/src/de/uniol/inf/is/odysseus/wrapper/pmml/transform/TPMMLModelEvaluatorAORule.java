package de.uniol.inf.is.odysseus.wrapper.pmml.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.pmml.logicaloperator.PMMLModelEvaluatorAO;
import de.uniol.inf.is.odysseus.wrapper.pmml.physicaloperator.PMMLModelEvaluatorPO;

@SuppressWarnings({"rawtypes"})
public class TPMMLModelEvaluatorAORule extends AbstractTransformationRule<PMMLModelEvaluatorAO>{

	@Override
	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(PMMLModelEvaluatorAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new PMMLModelEvaluatorPO(
				operator.getModelName(), 
				operator.getOuputMode(), 
				operator.getStackSize()), config, true, true);
	}

	@Override
	public boolean isExecutable(PMMLModelEvaluatorAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	 public String getName() {
	 return "PMMLModelEvaluatorAO -> PMMLModelEvaluatorPO";
	 }

	@Override
	 public Class<? super PMMLModelEvaluatorAO> getConditionClass() {
		return PMMLModelEvaluatorAO.class;
	 }
}
