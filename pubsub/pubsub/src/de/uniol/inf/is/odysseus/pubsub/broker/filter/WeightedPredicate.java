package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

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
