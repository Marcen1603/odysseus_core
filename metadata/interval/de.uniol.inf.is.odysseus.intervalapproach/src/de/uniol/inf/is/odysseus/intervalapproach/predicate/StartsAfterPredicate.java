package de.uniol.inf.is.odysseus.intervalapproach.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

public class StartsAfterPredicate extends AbstractPredicate<IMetaAttribute<? extends ITimeInterval>>{

	private static final StartsAfterPredicate instance = new StartsAfterPredicate();
	
	public StartsAfterPredicate clone(){
		return this;
	}
	
	public boolean evaluate(IMetaAttribute<? extends ITimeInterval> elem){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(IMetaAttribute<? extends ITimeInterval> left, IMetaAttribute<? extends ITimeInterval> right){
		return left.getMetadata().getStart().afterOrEquals(right.getMetadata().getStart());
	}
	
	public static StartsAfterPredicate getInstance(){
		return instance;
	}
}
