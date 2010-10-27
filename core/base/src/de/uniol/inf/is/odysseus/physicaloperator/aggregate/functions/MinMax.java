package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class MinMax<T extends Comparable<T>> extends AbstractAggregateFunction<T>{
	
	boolean isMax = true;
	
	protected MinMax(boolean isMax) {
		super (isMax?"MAX":"MIN");
		this.isMax = isMax;
	}
		
	
	@Override
	public IPartialAggregate<T> init(T in) {
		return new ElementPartialAggregate<T>(in);
	}

	@Override
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

	@Override
	public T evaluate(IPartialAggregate<T> p) {
		ElementPartialAggregate<T> pa = (ElementPartialAggregate<T>) p;
		return pa.getElem();
	}

}
