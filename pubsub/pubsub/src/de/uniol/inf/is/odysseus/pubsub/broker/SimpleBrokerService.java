package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;


public class SimpleBrokerService extends AbstractBrokerService {

	static List<Broker> brokers;
	
	public SimpleBrokerService() {
		// default constructor needed for Service
		init(false);
	}

	
	@Override
	public void init(boolean isNetworkDistributed) {
		brokers = new ArrayList<Broker>();
		if (!isNetworkDistributed){
			brokers.add(new Broker("Broker_0"));			
		} else {
			// Create brokernetwork
			// TODO
			brokers.add(new Broker("Broker_0"));
		}
	}

	@Override
	public void subscribe(String brokername, List<IPredicate<?>> predicates, SubscribePO<?> subscriber) {
		Broker b = getBrokerByName(brokername);
		b.setSubscription(predicates, subscriber);
	}

	@Override
	public void unsubscribe(String brokername, List<IPredicate<?>> predicates, SubscribePO<?> subscriber) {
		Broker b = getBrokerByName(brokername);
		b.removeSubscription(predicates, subscriber);
	}

	@Override
	public void advertise(List<IPredicate<?>> predicates, PublishPO publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unadvertise(List<IPredicate<?>> predicates, PublishPO publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transfer() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * returns a broker with a given name
	 * @param brokername
	 * @return
	 */
	private Broker getBrokerByName(String brokername) {
		for (Broker broker : brokers) {
			if (broker.getName().equals(brokername)){
				return broker;
			}
		}
		return null;
	}

}
