package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticMax<R extends Comparable<R>, W> extends
		AbstractAggregateFunction<R, W> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8103927421161762878L;

	protected ProbabilisticMax() {
		super("MAX");
	}

	@Override
	public IPartialAggregate<R> init(R in) {
		return new ElementPartialAggregate<R>(in);
	}

	@Override
	public IPartialAggregate<R> merge(IPartialAggregate<R> p, R toMerge,
			boolean createNew) {
		ElementPartialAggregate<R> pa = null;

		return pa;
	}

	@Override
	public W evaluate(IPartialAggregate<R> p) {
		@SuppressWarnings("unchecked")
		ElementPartialAggregate<W> pa = (ElementPartialAggregate<W>) p;
		return pa.getElem();
	}

}
