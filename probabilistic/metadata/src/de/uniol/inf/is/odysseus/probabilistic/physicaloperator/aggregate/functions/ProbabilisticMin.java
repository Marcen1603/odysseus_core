package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticMin<R extends Comparable<R>, W> extends
		AbstractAggregateFunction<R, W> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4241950598685654559L;

	protected ProbabilisticMin() {
		super("MIN");

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

	private Object[] computeBins() {
		Object[] bins = new Object[] {};
		int[] p = new int[] {};
		int[] b = new int[] {};
		int i = 1;
		int l = 0;
		int e = 1;
		while (i < l) {
			double k = Math.log(b[i]) / Math.log(1 + e);
			int q = 0;
			bins = bins;
			while (k == Math.log(b[i]) / Math.log(1 + e)) {
				q = q + p[i];
				i++;
			}
		}
		return bins;
	}

	private double estimateMin() {
		List p = new ArrayList();

		while (!p.isEmpty()) {

			Object[] bins = computeBins();
			double w = 1;
			double U = 0;
			double V = 0;
			double q = 0;
			for (Object k : bins) {
				U = (q / w) * V + U;
				V = (1 - q / w) * V;
				w = w - q;
			}

		}
		double V = 0;
		double U = 0;
		double e = 1;
		double min = 0;
		double n = 0;
		double t = 2 * Math.log(n) / Math.log(1 + e);
		for (int i = 0; i <= t; i++) {
			double tmp = 0;
			for (int j = 0; j <= i - 1; j++) {
				tmp *= V;
			}
			min += Math.pow((1 + e), i) * U * tmp;
		}
		return min;

	}
}
