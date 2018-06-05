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

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.IBroker;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;

/**
 * Interface for routing brokers
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IRoutingBroker <T extends IStreamObject<?>> extends IBroker<T>{
	
	/**
	 * returns the routing type. Needed for RoutingBrokerRegistry
	 * @return routing type
	 */
	String getType();
	
	/**
	 * returns the unique identifier of this broker
	 */
	String getIdentifier();
	
	/**
	 * returns a new instance of a routing broker
	 * @param name
	 * @param domain
	 * @return new routing broker instance
	 */
	IRoutingBroker<T> getInstance(String name, String domain);
	
	/**
	 * routes an object to other brokers
	 * @param object
	 * @param publisher
	 * @param sourceIdentifier
	 */
	void route(T object, String publisherUid, String sourceIdentifier);

	/**
	 * returns all connected brokers
	 */
	List<IRoutingBroker<T>> getConnectedBrokers();

	/**
	 * distribute a given advertisement to other connected brokers
	 * @param topics
	 * @param publisherUid
	 * @param sourceIdentifier
	 */
	void distributeAdvertisement(List<Topic> topics, String publisherUid, String sourceIdentifier);
	
	/**
	 * remove a distributed advertisment from all connected brokers 
	 * @param topics
	 * @param publisherUid
	 * @param sourceIdentifier
	 */
	void removeDistributedAdvertisement(List<Topic> topics, String publisherUid, String sourceIdentifier);

	
}
