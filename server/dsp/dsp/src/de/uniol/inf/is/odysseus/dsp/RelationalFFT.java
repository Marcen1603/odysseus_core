package de.uniol.inf.is.odysseus.dsp;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class RelationalFFT extends FFT<Tuple<?>, Tuple<?>> {

	private static final long serialVersionUID = 8958364792258841508L;
	private final int position;

	protected RelationalFFT(int position, boolean partialAggregateInput) {
		super(partialAggregateInput);
		this.position = position;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		Tuple<?> tuple = new Tuple(1, false);
		tuple.setAttribute(0, ((FFTPartialAggregate<Tuple<?>>)p).evaluate());
		return tuple;
	}

	@Override
	protected IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		return new FFTPartialAggregate<>((double) in.getAttribute(position));
	}

	@Override
	protected IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
		IPartialAggregate<Tuple<?>> partialAggregate = createNew ? p.clone() : p;
		((FFTPartialAggregate<Tuple<?>>) partialAggregate).add(toMerge.getAttribute(position));
		return partialAggregate;
	}
}
