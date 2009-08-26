package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

public class LiesInPredicate extends AbstractPredicate<IMetaAttribute<? extends ITimeInterval>>{

	private static final LiesInPredicate instance = new LiesInPredicate();
	
	public boolean evaluate(IMetaAttribute<? extends ITimeInterval> left, IMetaAttribute<? extends ITimeInterval> right){
		if(left.getMetadata().getStart().beforeOrEquals(right.getMetadata().getStart()) &&
				left.getMetadata().getEnd().afterOrEquals(right.getMetadata().getEnd())){
			return true;
		}
		return false;
	}
	
	/**
	 * @deprecated This method is not supported by this predicate.
	 */
	public boolean evaluate(IMetaAttribute<? extends ITimeInterval> input) {
		throw new UnsupportedOperationException();
	}
	
	public LiesInPredicate clone(){
		return this;
	}
	
	public static LiesInPredicate getInstance(){
		return instance;
	}
}
