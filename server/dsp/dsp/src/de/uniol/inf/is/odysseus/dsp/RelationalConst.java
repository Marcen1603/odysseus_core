package de.uniol.inf.is.odysseus.dsp;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class RelationalConst extends Const<Tuple<?>, Tuple<?>> {

	private static final long serialVersionUID = 8958364792258841508L;

	protected RelationalConst(boolean partialAggregateInput) {
		super(partialAggregateInput);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		Tuple<?> tuple = new Tuple(1, false);
		tuple.setAttribute(0, new Integer(42));
		return tuple;
	}
}
