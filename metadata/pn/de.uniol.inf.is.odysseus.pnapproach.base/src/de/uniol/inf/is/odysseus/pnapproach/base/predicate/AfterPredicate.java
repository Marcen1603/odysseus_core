package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

/**
 * Needed because TimeIntervals and PointInTimes have other compare operations e.g. overlaps, before, equals, etc.
 * @author abolles
 *
 */
public class AfterPredicate<M extends IPosNeg> extends AbstractPredicate<IMetaAttribute<M>>{

	public static AfterPredicate theInstance = new AfterPredicate();
	
	@Deprecated
	public boolean evaluate(IMetaAttribute<M> e){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(IMetaAttribute<M> e1, IMetaAttribute<M> e2){
		return e1.getMetadata().getTimestamp().after(e2.getMetadata().getTimestamp());
	}
	
	public AfterPredicate clone(){
		return theInstance;
	}
	
	public static AfterPredicate getInstance(){
		return theInstance;
	}
}
