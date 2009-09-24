package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Evaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Initializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Merger;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;

abstract public class Count<T> implements Initializer<T>, Merger<T>, Evaluator<T> {

	public PartialAggregate<T> init(T in) {
		PartialAggregate<T> pa = new CountPartialAggregate<T>(1); 
		return pa;
	}

	public synchronized PartialAggregate<T> merge(PartialAggregate<T> p, T toMerge, boolean createNew) {
		CountPartialAggregate<T> pa = null;
		if (createNew){
			pa = new CountPartialAggregate<T>(((CountPartialAggregate) p).getCount());
		}else{
			pa = (CountPartialAggregate<T>) p;
		}		
		pa.add();
		return pa;
	}

}
