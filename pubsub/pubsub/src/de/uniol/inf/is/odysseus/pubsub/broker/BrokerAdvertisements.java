package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class BrokerAdvertisements {

	private PublishPO<?> publisher;

	private List<Topic> topics;

	public BrokerAdvertisements(PublishPO<?> publisher, List<Topic> topics){
		this.publisher = publisher;
		this.topics = topics;
	}
	
	public boolean hasTopics(){
		if (!topics.isEmpty())
			return true;
		else
			return false;
	}
	
	public PublishPO<?> getPublisher() {
		return publisher;
	}

	public void setPublisher(PublishPO<?> publisher) {
		this.publisher = publisher;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

}
