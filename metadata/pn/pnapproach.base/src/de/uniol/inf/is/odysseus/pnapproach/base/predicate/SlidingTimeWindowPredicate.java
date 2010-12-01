package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

public class SlidingTimeWindowPredicate<T extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPredicate<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6486548910841710973L;
	long windowSize;
	
	public SlidingTimeWindowPredicate(long windowSize){
		this.windowSize = windowSize;
	}
	
	public SlidingTimeWindowPredicate(SlidingTimeWindowPredicate<T> old){
		this.windowSize = old.windowSize;
	}

	@Override
	@Deprecated
	public boolean evaluate(T object){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean evaluate(T left, T right){
		PointInTime end = new PointInTime(right.getMetadata().getTimestamp().sum(this.windowSize));
		if(end.beforeOrEquals(left.getMetadata().getTimestamp())){
			return true;
		}
		return false;
	}
	
	@Override
	public SlidingTimeWindowPredicate<T> clone(){
		return new SlidingTimeWindowPredicate<T>(this);
	}
}
