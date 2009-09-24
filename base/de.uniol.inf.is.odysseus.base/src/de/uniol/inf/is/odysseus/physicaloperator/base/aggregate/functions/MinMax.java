package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Evaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Initializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Merger;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;

public class MinMax<T extends Comparable> implements Initializer<T>, Merger<T>, Evaluator<T>{
	
	boolean isMax = true;
	
	public MinMax(boolean isMax) {
		this.isMax = isMax;
	}
	
	public PartialAggregate<T> init(T in) {
		return new ElementPartialAggregate<T>(in);
	}

	public PartialAggregate<T> merge(PartialAggregate<T> p, T toMerge, boolean createNew) {
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

	public T evaluate(PartialAggregate<T> p) {
		ElementPartialAggregate<T> pa = (ElementPartialAggregate<T>) p;
		return pa.getElem();
	}

}
