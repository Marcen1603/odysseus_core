package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class BrokerSubscription<T> {

	private SubscribePO<?> subscriber;

	private List<Topic> topics;

	private List<IPredicate<? super T>> predicates;

	public BrokerSubscription(SubscribePO<?> subscriber, List<IPredicate<? super T>> predicates,
			List<Topic> topics) {
		this.setPredicates(predicates);
		this.setTopics(topics);
		this.setSubscriber(subscriber);
	}
	
	public boolean hasTopics(){
		if (!topics.isEmpty())
			return true;
		else
			return false;
	}
	
	public boolean hasPredicates(){
		if (!predicates.isEmpty())
			return true;
		else
			return false;
	}
	

	public SubscribePO<?> getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(SubscribePO<?> subscriber) {
		this.subscriber = subscriber;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public List<IPredicate<? super T>> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<IPredicate<? super T>> predicates) {
		this.predicates = predicates;
	}

}
