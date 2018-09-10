package de.uniol.inf.is.odysseus.securitypunctuation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator.SPAnalyzerAO;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SPAnalyzerPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSPAnalyzerAORule extends AbstractTransformationRule<SPAnalyzerAO>{

	@Override
	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(SPAnalyzerAO spAnalyzerAO, TransformationConfiguration config) throws RuleException {
		@SuppressWarnings("rawtypes")
		SPAnalyzerPO<?> spAnalyzerPO=new SPAnalyzerPO();
		defaultExecute(spAnalyzerAO, spAnalyzerPO, config, true, true);
		
	}

	@Override
	public boolean isExecutable(SPAnalyzerAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "SPAnalyzerAO -> SPAnalyzerPO";
	}
	@Override
	public Class<? super SPAnalyzerAO> getConditionClass() {
		return SPAnalyzerAO.class;
	}


}
