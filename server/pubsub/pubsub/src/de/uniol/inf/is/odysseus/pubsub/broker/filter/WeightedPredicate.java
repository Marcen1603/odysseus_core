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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * weighted predicate combines predicate and weight 
 * needed for content based filtering 
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class WeightedPredicate<T extends IStreamObject<?>> implements Comparable<WeightedPredicate<T>> {
	
	private IPredicate<? super T> predicate;
	private Integer weight;
	

	public WeightedPredicate(IPredicate<? super T> predicate) {
		this.predicate = predicate;
		this.weight = 1;
	}

	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	public void setPredicate(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}

	public int getWeight() {
		return weight;
	}

	public void incrementWeight() {
		this.weight++;
	}
	
	public boolean isPredicateEqual(IPredicate<? super T> other){
		if (this.predicate.equals(other)){
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(WeightedPredicate<T> other) {
		return weight.compareTo(other.getWeight());
	}
	

}
