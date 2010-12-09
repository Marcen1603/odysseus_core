package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

public class StartsAfterPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6773517774767435885L;
	private static final StartsAfterPredicate instance = new StartsAfterPredicate();
	
	@Override
	public StartsAfterPredicate clone(){
		return this;
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> elem){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left, IMetaAttributeContainer<? extends ITimeInterval> right){
		return left.getMetadata().getStart().afterOrEquals(right.getMetadata().getStart());
	}
	
	public static StartsAfterPredicate getInstance(){
		return instance;
	}
	
	@Override
	public boolean equals(IPredicate pred) {
		return (pred instanceof StartsAfterPredicate);
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof StartsAfterPredicate)) {
			return false;
		}
		return true;
	}
}
