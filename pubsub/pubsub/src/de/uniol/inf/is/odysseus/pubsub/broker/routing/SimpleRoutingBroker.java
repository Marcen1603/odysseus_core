package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class SimpleRoutingBroker<T extends IStreamObject<?>> extends AbstractRoutingBroker<T> {
		//private static Logger logger = LoggerFactory.getLogger(FloodingBroker.class);
		private static final String ROUTING_TYPE = "SimpleRouting";
		private List<IRoutingBroker<T>> connectedBrokers = new ArrayList<IRoutingBroker<T>>();
		
		
		public SimpleRoutingBroker() {
			// needed for OSGi
			super("", "");
		}
		
		public SimpleRoutingBroker(String name, String domain) {
			super(name, domain);
		}
		
		@Override
		public List<IRoutingBroker<T>> getConnectedBrokers() {
			return connectedBrokers;
		}

		@Override
		public String getType() {
			return ROUTING_TYPE;
		}

		@Override
		public IRoutingBroker<T> getInstance(String name, String domain) {
			return new FloodingBroker<T>(name, domain);
		}
		
		@Override
		public void distributeAdvertisement(List<Topic> topics,
				String publisherUid, String sourceIdentifier) {
			super.setAdvertisement(topics, publisherUid);
			for (IRoutingBroker<T> conBroker : connectedBrokers) {
				if (!conBroker.getIdentifier().equals(sourceIdentifier)){
					conBroker.distributeAdvertisement(topics, publisherUid, this.getIdentifier());
				}
			}
		}

		@Override
		public void removeDistributedAdvertisement(List<Topic> topics,
				String publisherUid, String sourceIdentifier) {
			super.removeAdvertisement(topics, publisherUid);
			for (IRoutingBroker<T> conBroker : connectedBrokers) {
				if (!conBroker.getIdentifier().equals(sourceIdentifier)){
					conBroker.removeDistributedAdvertisement(topics, publisherUid, this.getIdentifier());
				}
			}
		}
		
		@Override
		public void route(T object, PublishPO<T> publisher, String sourceIdentifier) {
			// Send to Subscribers needs to return if topics match
			
			super.sendToSubscribers(object, publisher);
			
			// route Method should return if minimal one connected Brokers matches
			// else dont route anymore to this broker, if publisher id is equal
			// !! Remember if broker (or connected Broker) needs message from this publisher
			for (IRoutingBroker<T> conBroker : connectedBrokers) {
				if (!conBroker.getIdentifier().equals(sourceIdentifier)){
					conBroker.route(object, publisher, this.getIdentifier());
				}
			}
		}
}
