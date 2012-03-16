package de.uniol.inf.is.odysseus.markov.operator.aggregate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class ForwardAggregationFunction extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9078982639010201605L;
	
	public ForwardAggregationFunction(HiddenMarkovModel hmm) {
		super("FORWARD");
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
