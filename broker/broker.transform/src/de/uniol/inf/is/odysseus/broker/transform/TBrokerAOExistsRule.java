package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerAOExistsRule extends AbstractTransformationRule<BrokerAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BrokerAO brokerAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "Reuse Broker: " + brokerAO);
		LoggerSystem.printlog(Accuracy.DEBUG, "Using existing BrokerPO");  		
		BrokerPO<?> brokerPO = BrokerWrapperPlanFactory.getPlan(brokerAO.getIdentifier());				
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(brokerAO,brokerPO);										
		for (ILogicalOperator o:toUpdate){
			LoggerSystem.printlog(Accuracy.DEBUG, "Insert: "+o);		
			update(o);
		}			
		LoggerSystem.printlog(Accuracy.DEBUG, "Retracting BrokerAO: "+brokerAO);		 
		retract(brokerAO);	
		insert(brokerPO);
		
	}

	@Override
	public boolean isExecutable(BrokerAO operator, TransformationConfiguration transformConfig) {
		if(BrokerWrapperPlanFactory.getPlan(operator.getIdentifier())!=null){
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "BrokerAO -> Existing BrokerPO";
	}

}
