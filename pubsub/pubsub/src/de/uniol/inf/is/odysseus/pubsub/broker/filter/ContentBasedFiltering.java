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
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class ContentBasedFiltering<T extends IStreamObject<?>> extends AbstractFiltering<T>{

	private Collection<BrokerSubscription<T>> subscriptions;
	
	@Override
	public void reinitializeFilter(Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		this.subscriptions.clear();
		this.subscriptions = subscriptions;
		setReinitializationMode(false);
	}

	@Override
	public List<String> filter(T object, PublishPO<T> publisher) {
		ArrayList<String> result = new ArrayList<String>();
		
		for (BrokerSubscription<T> subscription : subscriptions) {
			boolean allpredicatesValid = true;
			if (!subscription.hasPredicates()){
				result.add(subscription.getSubscriber().getIdentifier());
			} else {
				for (IPredicate<? super T> pred : subscription.getPredicates()) {
					if (!pred.evaluate(object)){
						allpredicatesValid = false;
					}
				}				
				if (allpredicatesValid){
					result.add(subscription.getSubscriber().getIdentifier());
				}
			}
		}
		return result;
	}

}
