package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;

abstract public class Count<T> implements IAggregateFunction<T> {

	public IPartialAggregate<T> init(T in) {
		IPartialAggregate<T> pa = new CountPartialAggregate<T>(1); 
		return pa;
	}

	public synchronized IPartialAggregate<T> merge(IPartialAggregate<T> p, T toMerge, boolean createNew) {
		CountPartialAggregate<T> pa = null;
		if (createNew){
			pa = new CountPartialAggregate<T>(((CountPartialAggregate<T>) p).getCount());
		}else{
			pa = (CountPartialAggregate<T>) p;
		}		
		pa.add();
		return pa;
	}

}
