package de.uniol.inf.is.odysseus.nlp.transform;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.nlp.logicaloperator.*;
import de.uniol.inf.is.odysseus.nlp.physicaloperator.AnnotatePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TAnnotateAORule extends AbstractTransformationRule<AnnotateAO> {

	@Override
	public void execute(AnnotateAO operator, TransformationConfiguration config) throws RuleException {
		 defaultExecute(operator, new AnnotatePO(operator.getToolkit(), operator.getNlpToolkit(), operator.getInformation(), operator.getAttribute()), config, true, true);		
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