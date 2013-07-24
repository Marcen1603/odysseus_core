package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

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
