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
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * Predicate node needed for predicate tree in content based filtering
 * 
 * @author ChrisToenjesDeye
 *
 */
public class PredicateNode<T extends IStreamObject<?>> {
	private IPredicate<? super T> predicate;
	
	private List<PredicateNode<T>> childPredicates = new ArrayList<PredicateNode<T>>();
	private List<String> subscriberUids = new ArrayList<String>();
	
	public PredicateNode(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}

	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	public void setPredicate(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}
	
	public boolean evaluate(T object){
		return predicate.evaluate(object);
	}
	
	public boolean isPredicateEqual(IPredicate<? super T> other){
		if (this.predicate.equals(other)){
			return true;
		}
		return false;
	}

	public List<PredicateNode<T>> getChildPredicates() {
		return childPredicates;
	}

	public void setChildPredicates(List<PredicateNode<T>> childPredicates) {
		this.childPredicates = childPredicates;
	}

	public List<String> getSubscriberUids() {
		return subscriberUids;
	}
}
