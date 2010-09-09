package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class LiesInPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends ITimeInterval>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1178472407512980479L;
	private static final LiesInPredicate instance = new LiesInPredicate();
	
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
}
