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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

public class SimpleRoutingBroker <T extends IStreamObject<?>> extends AbstractBroker<T>{

	private List<SimpleRoutingBroker<T>> connectedBrokers = new ArrayList<SimpleRoutingBroker<T>>();
	
	
	public SimpleRoutingBroker(String name, String domain) {
		super(name, domain);
	}

	@Override
	public void setSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubscription(List<IPredicate<? super T>> predicates,
			List<Topic> topics, SubscribePO<T> subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAdvertisement(List<Topic> topics, String publisherUid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAdvertisement(List<Topic> topics, String publisherUid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void routeToSubscribers(T object, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasSubscriptions() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<SimpleRoutingBroker<T>> getConnectedBrokers() {
		return connectedBrokers;
	}

}
