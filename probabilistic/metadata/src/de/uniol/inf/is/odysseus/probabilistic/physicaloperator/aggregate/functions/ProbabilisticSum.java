package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbability;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticSum extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6272207178324419258L;
	private static Map<Integer, ProbabilisticSum> instances = new HashMap<Integer, ProbabilisticSum>();
	private int pos;

	public static ProbabilisticSum getInstance(int pos) {
		ProbabilisticSum ret = instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticSum(pos);
			instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticSum(int pos) {
		super("SUM");
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		IPartialAggregate<Tuple<?>> pa = new SumPartialAggregate<Tuple<?>>(
				((Number) in.getAttribute(pos)).doubleValue(),
				((IProbability) in.getMetadata()).getProbability(pos));
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p,
			Tuple<?> toMerge, boolean createNew) {
		SumPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new SumPartialAggregate<Tuple<?>>(
					((SumPartialAggregate<Tuple<?>>) p).getSum());
		} else {
			pa = (SumPartialAggregate<Tuple<?>>) p;
		}

		pa.add(((Number) toMerge.getAttribute(pos)).doubleValue(),
				((IProbability) toMerge.getMetadata()).getProbability(pos));
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		SumPartialAggregate<Tuple<?>> pa = (SumPartialAggregate<Tuple<?>>) p;
		Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Double(pa.getSum()));
		return r;
	}

}
