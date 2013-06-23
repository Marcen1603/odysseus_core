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

package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public interface IBroker<T extends IStreamObject<?>> {

	/**
	 * Returns the Name of the Broker
	 * @return
	 */
	String getName();
	
	/**
	 * Returns the Domain of the Topology
	 * @return
	 */
	String getDomain();

	/**
	 * 
	 * @param predicates
	 * @param subscriber
	 */
	void setSubscription(List<IPredicate<? super T>> predicates, List<Topic> topics,
			SubscribePO<T> subscriber);
	
	/**
	 * 
	 * @param predicates
	 * @param subscriber
	 */
	void removeSubscription(List<IPredicate<? super T>> predicates, List<Topic> topics,
			SubscribePO<T> subscriber);
	
	
	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void setAdvertisement(List<Topic> topics,
			PublishPO<T> publisher);
	
	/**
	 * 
	 * @param topics
	 * @param publisher
	 */
	void removeAdvertisement(List<Topic> topics,
			PublishPO<T> publisher);
	
	/**
	 * 
	 * @param object
	 */
	void routeToSubscribers(T object, PublishPO<T> publisher);
	
	/**
	 * 
	 * @return
	 */
	boolean hasSubscriptions();
}
