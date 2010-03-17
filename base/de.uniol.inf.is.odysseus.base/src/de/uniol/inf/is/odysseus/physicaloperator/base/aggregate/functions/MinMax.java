package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;

public class MinMax<T extends Comparable<T>> implements IAggregateFunction<T>{
	
	boolean isMax = true;
	
	public MinMax(boolean isMax) {
		this.isMax = isMax;
	}
	
	public IPartialAggregate<T> init(T in) {
		return new ElementPartialAggregate<T>(in);
	}

	public IPartialAggregate<T> merge(IPartialAggregate<T> p, T toMerge, boolean createNew) {
		ElementPartialAggregate<T> pa = null;
		if (createNew){
			pa = new ElementPartialAggregate<T>(p);
		}else{
			pa = (ElementPartialAggregate<T>) p;	
		}		
		if (isMax){
			if (pa.getElem().compareTo(toMerge) < 0){
				pa.setElem(toMerge);
			}
		}else{
			if (pa.getElem().compareTo(toMerge) > 0){
				pa.setElem(toMerge);
			}			
		}
		return pa;
	}

	public T evaluate(IPartialAggregate<T> p) {
		ElementPartialAggregate<T> pa = (ElementPartialAggregate<T>) p;
		return pa.getElem();
	}

}
