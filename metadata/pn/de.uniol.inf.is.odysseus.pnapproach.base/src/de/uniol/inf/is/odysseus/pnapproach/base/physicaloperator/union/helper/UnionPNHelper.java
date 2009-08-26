package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.union.helper;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.base.UnionHelper;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.PosNeg;

public class UnionPNHelper<T extends IMetaAttribute<? extends IPosNeg>> implements UnionHelper<T>{

	public PointInTime getStart(T elem){
		return elem.getMetadata().getTimestamp();	
	}
	
	public T getReferenceElement(PointInTime minTs, T elem){
		T retval = (T)elem.clone();
		PosNeg pn = new PosNeg();
		pn.setTimestamp(minTs);
		retval.getMetadata().setTimestamp(minTs);
		return retval;
	}
}
