package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

public class LiesInPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1178472407512980479L;
	private static final LiesInPredicate instance = new LiesInPredicate();
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> left, IMetaAttributeContainer<? extends ITimeInterval> right){
		if(left.getMetadata().getStart().beforeOrEquals(right.getMetadata().getStart()) &&
				left.getMetadata().getEnd().afterOrEquals(right.getMetadata().getEnd())){
			return true;
		}
		return false;
	}
	
	/**
	 * @deprecated This method is not supported by this predicate.
	 */
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public LiesInPredicate clone(){
		return this;
	}
	
	public static LiesInPredicate getInstance(){
		return instance;
	}
	
	@Override
	public boolean equals(IPredicate pred) {
		if(!(pred instanceof LiesInPredicate)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isContainedIn(Object o) {
		if(!(o instanceof LiesInPredicate)) {
			return false;
		}
		return true;
	}
}
