package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class AttributeTimeIntervalComparator<T extends IClone> implements Comparator<RelationalTuple<ITimeInterval>>{
	private int attributePos = 1;
	
	@Override
	public int compare(RelationalTuple<ITimeInterval> left, RelationalTuple<ITimeInterval> right) {	
		int compare = left.getMetadata().compareTo(right.getMetadata());
		if(compare==0){
			int idLeft = ((Integer)left.getAttribute(attributePos)).intValue();
			int idRight = ((Integer)right.getAttribute(attributePos)).intValue();
			if(idLeft<idRight){
				return -1;
			}else{
				if(idLeft>idRight){
					return 1;
				}else{
					return 0;
				}
			}
		}else{
			return compare;
		}
		
	}


}
