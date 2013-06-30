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

package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;

public class ContentBasedFiltering<T extends IStreamObject<?>> extends AbstractFiltering<T>{

	private Collection<BrokerSubscription<T>> subscriptions = new ArrayList<BrokerSubscription<T>>();
	
	/**
	 * initialization in content based filtering not needed until now
	 */
	@Override
	public void reinitializeFilter(Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		this.subscriptions.clear();
		this.subscriptions = subscriptions;
		setReinitializationMode(false);
	}

	/**
	 * filters given object, if all subscriber predicates match
	 */
	@Override
	public List<String> filter(T object, String publisherUid) {
		ArrayList<String> result = new ArrayList<String>();
		
		for (BrokerSubscription<T> subscription : subscriptions) {
			// foreach subscription
			boolean allpredicatesValid = true;
			if (!subscription.hasPredicates()){
				// add if no predicates are available (so object matches)
				result.add(subscription.getSubscriber().getIdentifier());
			} else {
				// check if all predicates of this subscriber matches
				for (IPredicate<? super T> pred : subscription.getPredicates()) {
					if (!pred.evaluate(object)){
						allpredicatesValid = false;
					}
				}	
				// if all predicates are match, add subscriber
				if (allpredicatesValid){
					result.add(subscription.getSubscriber().getIdentifier());
				}
			}
		}
		return result;
	}

}
