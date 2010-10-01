package de.uniol.inf.is.odysseus.broker.transform;

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionDetector;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerCycleDetectionRule extends AbstractTransformationRule<BrokerPO<?>> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(BrokerPO<?> operator, TransformationConfiguration transformConfig) {		
		LoggerSystem.printlog(Accuracy.DEBUG, "Searching for cycles and reorganize broker transactions..."); 
		new TransactionDetector().reorganizeTransactions(BrokerWrapperPlanFactory.getAllBrokerPOs());		
		LoggerSystem.printlog(Accuracy.DEBUG, "Searching done"); 
		retract(operator);
	}

	@Override
	public boolean isExecutable(BrokerPO<?> operator, TransformationConfiguration transformConfig) {
		return (!BrokerWrapperPlanFactory.isEmpty());
	}

	@Override
	public String getName() {
		return "Detecting Cycles Rule";
	}
	
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.TRANSFORMATION;
	}


}
