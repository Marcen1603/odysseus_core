package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public class LiesInPNPredicate extends AbstractPredicate<IMetaAttribute<? extends IPosNeg>>{

	PointInTime t_start;
	PointInTime t_end;
	
	public LiesInPNPredicate(PointInTime t_start, PointInTime t_end){
		this.t_start = t_start;
		this.t_end = t_end;
	}
	
	public boolean evaluate(IMetaAttribute<? extends IPosNeg> left, IMetaAttribute<? extends IPosNeg> right){
		if(left.getMetadata().getTimestamp().afterOrEquals(this.t_start) &&
				left.getMetadata().getTimestamp().beforeOrEquals(this.t_end)){
			return true;
		}
		return false;
	}
	
	/**
	 * @deprecated This method is not supported by this predicate.
	 */
	public boolean evaluate(IMetaAttribute<? extends IPosNeg> input) {
		throw new UnsupportedOperationException();
	}
	
	public LiesInPNPredicate clone(){
		return new LiesInPNPredicate(this.t_start, this.t_end);
	}
}
