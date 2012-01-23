package de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;

abstract public class Rate<R extends MetaAttributeContainer<?>, W extends MetaAttributeContainer<?>>
		extends AbstractAggregateFunction<R, W> {

	protected Rate() {
		super("RATE");
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		IPartialAggregate<R> pa = new RatePartialAggregate<R>(
				(ITimeInterval) in.getMetadata());
		return pa;
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		RatePartialAggregate<R> pa = null;
		if (createNew) {
			pa = new RatePartialAggregate<R>(((RatePartialAggregate<R>) p));
		} else {
			pa = (RatePartialAggregate<R>) p;
		}
		pa.add();
		return pa;
	}
}
