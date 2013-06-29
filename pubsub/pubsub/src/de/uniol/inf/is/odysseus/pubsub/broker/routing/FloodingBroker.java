/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class FloodingBroker <T extends IStreamObject<?>> extends AbstractRoutingBroker<T>{

	//private static Logger logger = LoggerFactory.getLogger(FloodingBroker.class);
	private static final String ROUTING_TYPE = "Flooding";
	private List<IRoutingBroker<T>> connectedBrokers = new ArrayList<IRoutingBroker<T>>();
	
	
	public FloodingBroker() {
		// needed for OSGi
		super("", "");
	}
	
	public FloodingBroker(String name, String domain) {
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
		super.sendToSubscribers(object, publisher);
		for (IRoutingBroker<T> conBroker : connectedBrokers) {
			if (!conBroker.getIdentifier().equals(sourceIdentifier)){
				conBroker.route(object, publisher, this.getIdentifier());
			}
		}
	}

	

	
}
