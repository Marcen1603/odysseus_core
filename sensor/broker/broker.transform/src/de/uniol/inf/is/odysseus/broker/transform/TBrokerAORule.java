package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerAORule extends AbstractTransformationRule<BrokerAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BrokerAO brokerAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "CREATE Broker: " + brokerAO.getIdentifier()); 
		BrokerPO brokerPO = new BrokerPO(brokerAO.getIdentifier());
		brokerPO.setOutputSchema(brokerAO.getOutputSchema());
		brokerPO.setQueueSchema(brokerAO.getQueueSchema());
		BrokerWrapperPlanFactory.putPlan(brokerAO.getIdentifier(), brokerPO);			
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(brokerAO,brokerPO);							
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(brokerAO);
		insert(brokerPO);
		LoggerSystem.printlog(Accuracy.DEBUG, "CREATE Broker end.");
		
	}

	@Override
	public boolean isExecutable(BrokerAO operator, TransformationConfiguration transformConfig) {
		return (BrokerWrapperPlanFactory.getPlan(operator.getIdentifier()) == null);
	}

	@Override
	public String getName() {
		return "BrokerAO -> BrokerPO";
	}

	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.ACCESS;
	}

}
