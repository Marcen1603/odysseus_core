package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

public class LiesInPNPredicate extends AbstractPredicate<IMetaAttributeContainer<? extends IPosNeg>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2502764977802196462L;
	PointInTime t_start;
	PointInTime t_end;
	
	public LiesInPNPredicate(PointInTime t_start, PointInTime t_end){
		this.t_start = t_start;
		this.t_end = t_end;
	}
	
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> left, IMetaAttributeContainer<? extends IPosNeg> right){
		if(left.getMetadata().getTimestamp().afterOrEquals(this.t_start) &&
				left.getMetadata().getTimestamp().beforeOrEquals(this.t_end)){
			return true;
		}
		return false;
	}
	
	/**
	 * @deprecated This method is not supported by this predicate.
	 */
	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> input) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public LiesInPNPredicate clone(){
		return new LiesInPNPredicate(this.t_start, this.t_end);
	}
}
