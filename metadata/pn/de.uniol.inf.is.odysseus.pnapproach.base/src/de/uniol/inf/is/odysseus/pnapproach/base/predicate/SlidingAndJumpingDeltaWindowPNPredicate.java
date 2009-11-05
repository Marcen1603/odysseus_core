package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

/**
 * Dieses Praedikat kann fuer SlidingDeltaWindows und JumpingTimeWindows im PN-Ansatz verwendet werden.
 * 
 * @author abolles
 *
 */
public class SlidingAndJumpingDeltaWindowPNPredicate<T extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPredicate<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1138770857347924972L;
	long windowSize;
	long windowAdvance;
	
	public SlidingAndJumpingDeltaWindowPNPredicate(long windowSize, long windowAdvance){
		this.windowSize = windowSize;
		this.windowAdvance = windowAdvance;
	}
	
	public SlidingAndJumpingDeltaWindowPNPredicate(SlidingAndJumpingDeltaWindowPNPredicate<T> old){
		this.windowAdvance = old.windowAdvance;
		this.windowSize = old.windowSize;
	}
	
	@Override
	public boolean evaluate(T input) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	/**
	 * @param left Referenzelement
	 * @param right zu evaluierendes Element
	 */
	public boolean evaluate(T left, T right) {
		// TODO Auto-generated method stub
		// first calc the end time stamp of the right element
		PointInTime end = new PointInTime(right.getMetadata().getTimestamp().getMainPoint()/windowAdvance * windowAdvance + windowSize, right.getMetadata().getTimestamp().getSubpoint());
		if(end.beforeOrEquals(left.getMetadata().getTimestamp())){
			return true;
		}
		return false;
	}
	
	@Override
	public SlidingAndJumpingDeltaWindowPNPredicate<T> clone(){
		return new SlidingAndJumpingDeltaWindowPNPredicate<T>(this);
	}

}
