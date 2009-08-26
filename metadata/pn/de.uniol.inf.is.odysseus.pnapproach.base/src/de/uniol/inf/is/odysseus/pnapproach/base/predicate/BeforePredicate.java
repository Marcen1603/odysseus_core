package de.uniol.inf.is.odysseus.pnapproach.base.predicate;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

public class BeforePredicate extends AbstractPredicate<IMetaAttribute<? extends IPosNeg>>{

	public static BeforePredicate theInstance = new BeforePredicate();
	
	@Deprecated
	public boolean evaluate(IMetaAttribute<? extends IPosNeg> e){
		throw new UnsupportedOperationException();
	}
	
	public boolean evaluate(IMetaAttribute<? extends IPosNeg> e1, IMetaAttribute<? extends IPosNeg> e2){
		return e1.getMetadata().getTimestamp().before(e2.getMetadata().getTimestamp());
	}
	
	public BeforePredicate clone(){
		return theInstance;
	}
	
	public static BeforePredicate getInstance(){
		return theInstance;
	}
}
