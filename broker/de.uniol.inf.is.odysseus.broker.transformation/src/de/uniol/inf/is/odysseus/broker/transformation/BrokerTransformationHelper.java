package de.uniol.inf.is.odysseus.broker.transformation;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

public class BrokerTransformationHelper {


	@SuppressWarnings("unchecked")
	public static Collection<ILogicalOperator> replace(ILogicalOperator logical, BrokerPO<?> physical) {
		Collection<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();		
		int queuePort = -1;
		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {		
			int newPort = psub.getSinkInPort();
			if(psub.getSinkInPort()==0){
				newPort = BrokerDictionary.getInstance().addNewTransaction(physical.getIdentifier(), WriteTransaction.Continuous);							
			}else if(psub.getSinkInPort()==1){
				newPort = BrokerDictionary.getInstance().addNewTransaction(physical.getIdentifier(), WriteTransaction.Timestamp);
				queuePort = newPort;				
			}else{
				LoggerFactory.getLogger("TBrokerAO - transformation").warn("A writing port should be either 0 or 1!");				
			}
			physical.subscribeToSource((ISource)psub.getTarget(), newPort, psub.getSourceOutPort(), psub.getSchema());
		}		
		for (LogicalSubscription l : logical.getSubscriptions()) {
			ILogicalOperator target = l.getTarget();
			if (target instanceof TopAO) {
				((TopAO) target).setPhysicalInputPO(physical);
			}
		}
		ret.add(logical);
		for (LogicalSubscription l : logical.getSubscriptions()) {
			int newReadingPort = l.getSourceOutPort();
			if(queuePort!=-1){
				newReadingPort = BrokerDictionary.getInstance().addNewTransaction(physical.getIdentifier(), ReadTransaction.OneTime);
				QueuePortMapping mapping = new QueuePortMapping(newReadingPort, queuePort);
				BrokerDictionary.getInstance().addQueuePortMapping(physical.getIdentifier(), mapping);
			}else{
				newReadingPort = BrokerDictionary.getInstance().addNewTransaction(physical.getIdentifier(), ReadTransaction.Continuous);
			}
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(), newReadingPort, l.getSchema());
			ret.add(l.getTarget());
		}
		return ret;
	}

}
