package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

public class PredicateResult<T extends IStreamObject<?>> {
	private IPredicate<? super T> predicate;
	
	private boolean result;

	
	public PredicateResult(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}

	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	public void setPredicate(IPredicate<? super T> predicate) {
		this.predicate = predicate;
	}
	
	public boolean evaluate(T object){
		boolean resultValue = predicate.evaluate(object);
		result = resultValue;
		return resultValue;
	}
	
	public boolean getResult(){
		return result;
	}
	
	public boolean isPredicateEqual(IPredicate<? super T> other){
		if (this.predicate.equals(other)){
			return true;
		}
		return false;
	}
}
