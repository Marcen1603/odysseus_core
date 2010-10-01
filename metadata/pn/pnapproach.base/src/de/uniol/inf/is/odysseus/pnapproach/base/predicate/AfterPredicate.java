package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;

/**
 * Needed because TimeIntervals and PointInTimes have other compare operations e.g. overlaps, before, equals, etc.
 * @author abolles
 *
 */
@SuppressWarnings("unchecked")
public class AfterPredicate<M extends IPosNeg> extends AbstractPredicate<IMetaAttributeContainer<M>>{

	private static final long serialVersionUID = 1L;
	public static AfterPredicate theInstance = new AfterPredicate();
	
	@Deprecated
	public boolean evaluate(IMetaAttributeContainer<M> e){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(IMetaAttributeContainer<M> e1, IMetaAttributeContainer<M> e2){
		return e1.getMetadata().getTimestamp().after(e2.getMetadata().getTimestamp());
	}
	
	@Override
	public AfterPredicate clone(){
		return theInstance;
	}
	
	public static AfterPredicate getInstance(){
		return theInstance;
	}
}
