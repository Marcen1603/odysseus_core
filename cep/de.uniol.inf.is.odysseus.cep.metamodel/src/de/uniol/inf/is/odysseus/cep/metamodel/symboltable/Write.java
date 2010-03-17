package de.uniol.inf.is.odysseus.cep.metamodel.symboltable;

import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.functions.ElementPartialAggregate;

public class Write<R> extends AbstractAggregateFunction<R> {
	
	public Write() {
		super("WRITE");
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		return new ElementPartialAggregate<R>(in);
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		((ElementPartialAggregate<R>)p).setElem(toMerge);
		return p;
	}

	@Override
	public R evaluate(IPartialAggregate<R> p) {
		return ((ElementPartialAggregate<R>)p).getElem();
	}

	
}
