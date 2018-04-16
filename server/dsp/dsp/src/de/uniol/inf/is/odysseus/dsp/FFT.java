package de.uniol.inf.is.odysseus.dsp;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;

public abstract class FFT<R, W> extends AbstractAggregateFunction<R, W> {

	private static final long serialVersionUID = 7144950381259595844L;

	protected FFT(boolean partialAggregateInput) {
		super("FFT", partialAggregateInput);
	}
}
