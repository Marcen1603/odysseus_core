package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAOExistsRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.TRACE,"Transform AccessAO: " + accessAO);	
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());
		LoggerSystem.printlog(Accuracy.TRACE, "Transform to existing AccessPO: trafo = " + trafo);
		LoggerSystem.printlog(Accuracy.TRACE, "Transform to existing AccessPO: trafoHelper = " + trafo.getTransformationHelper());
		replace(accessAO, accessPO, trafo);		
		insert(accessPO);
		retract(accessAO);
		
	}

	@Override
	public boolean isExecutable(AccessAO operator, TransformationConfiguration transformConfig) {		
		return WrapperPlanFactory.getAccessPlan(operator.getSource().getURI(false)) != null;
	}

	@Override
	public String getName() {
		return "Transform to existing AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
}
