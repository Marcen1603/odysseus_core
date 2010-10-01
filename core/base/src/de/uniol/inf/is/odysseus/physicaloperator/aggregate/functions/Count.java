package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class Count<T> extends AbstractAggregateFunction<T> {
	
	protected Count() {
		super("COUNT");
	}
	
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
