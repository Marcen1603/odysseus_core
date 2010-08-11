package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerAORule extends AbstractTransformationRule<BrokerAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(BrokerAO brokerAO, TransformationConfiguration trafo) {
		getLogger().debug("CREATE Broker: " + brokerAO.getIdentifier()); 
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
		getLogger().debug("CREATE Broker end.");
		
	}

	@Override
	public boolean isExecutable(BrokerAO operator, TransformationConfiguration transformConfig) {
		return (BrokerWrapperPlanFactory.getPlan(operator.getIdentifier()) == null);
	}

	@Override
	public String getName() {
		return "BrokerAO -> BrokerPO";
	}

}
