package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public class BeforePredicate extends AbstractPredicate<IMetaAttributeContainer<? extends IPosNeg>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9185637480957451344L;
	public static BeforePredicate theInstance = new BeforePredicate();
	
	@Deprecated
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> e){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(IMetaAttributeContainer<? extends IPosNeg> e1, IMetaAttributeContainer<? extends IPosNeg> e2){
		return e1.getMetadata().getTimestamp().before(e2.getMetadata().getTimestamp());
	}
	
	@Override
	public BeforePredicate clone(){
		return theInstance;
	}
	
	public static BeforePredicate getInstance(){
		return theInstance;
	}
}
