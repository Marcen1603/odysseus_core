package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

public abstract class AbstractRangePredicate<T> implements IRangePredicate<T>{

	private static final long serialVersionUID = 4375668057843231705L;

	@Override
	public IRangePredicate<T> clone(){
		try{
			return (AbstractRangePredicate<T>)super.clone();
		}catch(CloneNotSupportedException e){
			throw new RuntimeException(e);
		}
	}
}
