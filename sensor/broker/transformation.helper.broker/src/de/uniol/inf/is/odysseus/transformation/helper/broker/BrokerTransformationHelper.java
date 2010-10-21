package de.uniol.inf.is.odysseus.transformation.helper.broker;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.StandardTransformationHelper;

@SuppressWarnings("unchecked")
public class BrokerTransformationHelper extends StandardTransformationHelper{
	
	@Override
	public Collection<ILogicalOperator> replace(
			ILogicalOperator logical, ISource physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();

		for (LogicalSubscription l : logical.getSubscriptions()) {
			
			// if the following operator is a broker, then do not
			// subscribe to the logical one, but to the physical one
			if(l.getTarget() instanceof BrokerAO){
				BrokerPO brokerPO = BrokerWrapperPlanFactory.getPlan(((BrokerAO)l.getTarget()).getIdentifier());
				physical.subscribeSink(brokerPO, l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
			}
			else{
				l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(),
						l.getSourceOutPort(), l.getSchema());
				
				// the target of the current subscription has only to be returned
				// if it is a logical operator. Then this logical operator has
				// to be updated by the drools engine, to be checked in the
				// next turn of rule checking.
				// If the target is a broker, then we have subscribed the current phyiscal
				// to the corresponding physical broker. However, this means that
				// there is no logical operator to be updated by the drools engine
				ret.add(l.getTarget());
			}
			
		}
		return ret;
	}
	

}
