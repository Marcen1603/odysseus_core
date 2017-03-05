package de.uniol.inf.is.odysseus.nlp.transform;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.nlp.logicaloperator.AnnotateAO;
import de.uniol.inf.is.odysseus.nlp.physicaloperator.AnnotateKeyValuePO;
import de.uniol.inf.is.odysseus.nlp.physicaloperator.AnnotateObjectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAnnotateAORule extends AbstractTransformationRule<AnnotateAO> {

	@Override
	public void execute(AnnotateAO operator, TransformationConfiguration config) throws RuleException {
		if(operator.getOutputType().equals("KeyValueObject"))
			defaultExecute(operator, new AnnotateKeyValuePO(operator.getNlpToolkit(), operator.getPipeline(), operator.getAttribute(), operator.getConfiguration()), config, true, true);	
		else
			defaultExecute(operator, new AnnotateObjectPO(operator.getNlpToolkit(), operator.getPipeline(), operator.getAttribute(), operator.getConfiguration()), config, true, true);	
	}

	@Override
	public boolean isExecutable(AnnotateAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
 
}