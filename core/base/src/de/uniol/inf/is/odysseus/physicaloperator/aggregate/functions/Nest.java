package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class Nest<R> extends AbstractAggregateFunction<R> {

	protected Nest() {
		super("NEST");
	}
		
	@Override
	public IPartialAggregate<R> init(R in) {
		return new ListPartialAggregate<R>(in);
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		return ((ListPartialAggregate)p).addElem(toMerge);
	}


}
