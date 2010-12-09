package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

public class StartsBeforePredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1689799456375135808L;
	private static final StartsBeforePredicate instance = new StartsBeforePredicate();
	
	@Override
	public StartsBeforePredicate clone(){
		return this;
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> elem){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left, IMetaAttributeContainer<? extends ITimeInterval> right){
		return left.getMetadata().getStart().beforeOrEquals(right.getMetadata().getStart());
	}
	
	public static StartsBeforePredicate getInstance(){
		return instance;
	}
	
	@Override
	public boolean equals(IPredicate pred) {
		return (pred instanceof StartsBeforePredicate);
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof StartsBeforePredicate)) {
			return false;
		}
		return true;
	}
}
