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
import de.uniol.inf.is.odysseus.pubsub.broker.filter.WeightedPredicate;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * This class contains subscription data from subscriber
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class BrokerSubscription<T extends IStreamObject<?>> implements Comparable<BrokerSubscription<T>>{

	private SubscribePO<T> subscriber;
	private List<Topic> topics;
	private List<IPredicate<? super T>> predicates;
	
	private List<WeightedPredicate<T>> weightedPredicates = new ArrayList<WeightedPredicate<T>>();

	public BrokerSubscription(SubscribePO<T> subscriber,
			List<IPredicate<? super T>> predicates, List<Topic> topics) {
		this.setPredicates(predicates);
		this.setTopics(topics);
		this.setSubscriber(subscriber);
	}

	public BrokerSubscription(SubscribePO<T> subscriber) {
		this.setSubscriber(subscriber);
		this.setPredicates(subscriber.getPredicates2());
		this.setTopics(subscriber.getTopics());
	}

	public boolean hasTopics() {
		if (!topics.isEmpty())
			return true;
		return false;
	}

	public boolean hasPredicates() {
		if (!predicates.isEmpty())
			return true;
		return false;
	}

	public SubscribePO<T> getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(SubscribePO<T> subscriber) {
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

	public List<WeightedPredicate<T>> getWeightedPredicates() {
		return weightedPredicates;
	}

	public void setWeightedPredicates(List<WeightedPredicate<T>> weightedPredicates) {
		this.weightedPredicates = weightedPredicates;
	}
	
	public int getNumberOfPredicates(){
		return weightedPredicates.size();
	}
	
	/**
	 * returns the highest weight of all predicates
	 * needed for predicate tree in content based filtering
	 * @return highest predicate weight
	 */
	public Integer getHighestPredicateWeight(){
		Integer highestWeight = 0;
		for (WeightedPredicate<T> weightedPredicate : weightedPredicates) {
			if (weightedPredicate.getWeight() > highestWeight){
				highestWeight = weightedPredicate.getWeight();
			}
		}
		return highestWeight;
	}

	@Override
	public int compareTo(BrokerSubscription<T> other) {
		if (weightedPredicates.isEmpty()){
			return -1;			
		}
		// get highest predicate weight of this subscription
		Integer highestWeight = this.getHighestPredicateWeight();
		
		// get highest predicate weight of other subscription
		Integer highestWeightOther = other.getHighestPredicateWeight();
		
		return highestWeight.compareTo(highestWeightOther);
		
	}

}
