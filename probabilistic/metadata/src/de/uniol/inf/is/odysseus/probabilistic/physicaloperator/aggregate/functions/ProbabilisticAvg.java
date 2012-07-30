package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAvg extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2188835286391575126L;
	private static Map<Integer, ProbabilisticCount> instances = new HashMap<Integer, ProbabilisticCount>();
	private int pos;

	public static ProbabilisticCount getInstance(int pos) {
		ProbabilisticCount ret = instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticCount(pos);
			instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticAvg(int pos) {
		super("AVG");
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		// TODO estimate good values
		IPartialAggregate<Tuple<?>> pa = new AvgPartialAggregate<Tuple<?>>(
				0.25, 0.7);
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p,
			Tuple<?> toMerge, boolean createNew) {
		AvgPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new AvgPartialAggregate<Tuple<?>>(0.25, 0.7);
		} else {
			pa = (AvgPartialAggregate<Tuple<?>>) p;
		}

		// TODO Set probability from metadata
		pa.update(((Number) toMerge.getAttribute(pos)).doubleValue(), 0.3);
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		AvgPartialAggregate<Tuple<?>> pa = (AvgPartialAggregate<Tuple<?>>) p;
		Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Double(pa.reconstruct()));
		return r;
	}

}
