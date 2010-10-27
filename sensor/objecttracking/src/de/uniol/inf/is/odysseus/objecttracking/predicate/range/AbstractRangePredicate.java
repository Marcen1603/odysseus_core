package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

public abstract class AbstractRangePredicate<T> implements IRangePredicate<T>{

	@Override
	public IRangePredicate<T> clone(){
		try{
			return (AbstractRangePredicate<T>)super.clone();
		}catch(CloneNotSupportedException e){
			throw new RuntimeException(e);
		}
	}
}
