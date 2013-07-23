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
	
	private List<PredicateResult<T>> predicateResults = new ArrayList<PredicateResult<T>>();
	
	/**
	 * Initialize content based filtering
	 * all predicates are compressed, so each predicate needs to be evaluated one time
	 */
	@Override
	public void reinitializeFilter(Collection<BrokerSubscription<T>> subscriptions,
			Collection<BrokerAdvertisements> advertisements) {
		this.subscriptions.clear();
		this.predicateResults.clear();
		
		this.subscriptions = subscriptions;
		
		// compress all subscriber predicates 
		for (BrokerSubscription<T> brokerSubscription : subscriptions) {
			for (IPredicate<? super T> predicate : brokerSubscription.getPredicates()) {
				if (!isPredicateAlreadyAdded(predicate)){
					// Predicate is not already added to result list
					predicateResults.add(new PredicateResult<T>(predicate));
				}
			}
		}
		setReinitializationMode(false);
	}
	
	/**
	 * Checks if a predicate already exists in resultTable
	 * @param predicate
	 * @return true if predicate exists, else false
	 */
	private boolean isPredicateAlreadyAdded(IPredicate<? super T> predicate){
		for (PredicateResult<T> predicateResult : predicateResults) {
			if (predicateResult.isPredicateEqual(predicate)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns the result value of the given predicate (no evaluation needed)
	 * if predicate does not exists in result table -> evaluate it and reinitialize 
	 * result table on next filtering
	 * @param predicate
	 * @param object
	 * @return
	 */
	private boolean getResultForPredicate(IPredicate<? super T> predicate, T object){
		for (PredicateResult<T> predicateResult : predicateResults) {
			if (predicateResult.isPredicateEqual(predicate)){
				return predicateResult.getResult();
			}
		}
		
		// Result not available, may initialization failed -> reinitialize on next filtering
		setReinitializationMode(true);
		return predicate.evaluate(object);
	}

	/**
	 * filters given object, if all subscriber predicates match
	 */
	@Override
	public List<String> filter(T object, String publisherUid) {
		ArrayList<String> result = new ArrayList<String>();

		// evaluate all predicates first, but only one time
		for (PredicateResult<T> predicateResult : predicateResults) {
			predicateResult.evaluate(object);
		}
		
		// iterate all subscriptions
		for (BrokerSubscription<T> subscription : subscriptions) {
			
			boolean allpredicatesValid = true;
			if (!subscription.hasPredicates()){
				// add if no predicates are available (--> object matches)
				result.add(subscription.getSubscriber().getIdentifier());
			} else {
				// check if all predicates of this subscriber matches
				for (IPredicate<? super T> predicate : subscription.getPredicates()) {
					if (!getResultForPredicate(predicate, object)){
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
