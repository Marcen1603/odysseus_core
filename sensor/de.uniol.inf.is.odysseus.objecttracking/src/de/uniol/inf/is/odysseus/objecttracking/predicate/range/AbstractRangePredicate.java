package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;

public abstract class AbstractRangePredicate<T> implements IRangePredicate<T>{

	public IRangePredicate<T> clone(){
		try{
			return (AbstractRangePredicate<T>)super.clone();
		}catch(CloneNotSupportedException e){
			throw new RuntimeException(e);
		}
	}
}
