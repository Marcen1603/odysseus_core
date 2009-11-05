package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

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

	@Deprecated
	public boolean evaluate(T object){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(T left, T right){
		PointInTime end = new PointInTime(right.getMetadata().getTimestamp().sum(this.windowSize, 0));
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
