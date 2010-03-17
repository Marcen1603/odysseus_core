package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;


public class CountPartialAggregate<T> implements IPartialAggregate<T> {
	int count = 0;

	
	public CountPartialAggregate(int count) {
		this.count = count;
	}

	public synchronized int getCount() {
		return count;
	}
	
	public synchronized void add(){
		//System.out.println("CountPartialAggregate "+count+" --> "+(count+1));
		count=count+1;
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("CountPartialAggregate (").append(hashCode()).append(")").append(count);
		return ret.toString();
	}
}
