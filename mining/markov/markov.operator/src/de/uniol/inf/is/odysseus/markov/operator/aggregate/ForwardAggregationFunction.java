package de.uniol.inf.is.odysseus.markov.operator.aggregate;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class ForwardAggregationFunction extends AbstractAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9078982639010201605L;
	
	public ForwardAggregationFunction(HiddenMarkovModel hmm) {
		super("FORWARD");
	}

	@Override
	public IPartialAggregate<RelationalTuple<?>> init(RelationalTuple<?> in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPartialAggregate<RelationalTuple<?>> merge(IPartialAggregate<RelationalTuple<?>> p, RelationalTuple<?> toMerge, boolean createNew) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelationalTuple<?> evaluate(IPartialAggregate<RelationalTuple<?>> p) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
