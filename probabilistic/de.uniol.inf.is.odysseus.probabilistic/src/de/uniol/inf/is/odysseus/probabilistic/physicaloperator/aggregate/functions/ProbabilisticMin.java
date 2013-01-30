/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 *         FIXME Implement probabilistic Min aggregation function
 */
public class ProbabilisticMin extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4241950598685654559L;
	private static Map<Integer, ProbabilisticMin> instances = new HashMap<Integer, ProbabilisticMin>();
	@SuppressWarnings("unused")
	private final int pos;

	public static ProbabilisticMin getInstance(final int pos) {
		ProbabilisticMin ret = ProbabilisticMin.instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticMin(pos);
			ProbabilisticMin.instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticMin(final int pos) {
		super("MIN");
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
		return new ElementPartialAggregate<Tuple<?>>(in);
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(
			final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge,
			final boolean createNew) {
		final ElementPartialAggregate<Tuple<?>> pa = null;

		return pa;
	}

	@Override
	public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
		final ElementPartialAggregate<Tuple<?>> pa = (ElementPartialAggregate<Tuple<?>>) p;
		return pa.getElem();
	}

	private Object[] computeBins() {
		Object[] bins = new Object[] {};
		final int[] p = new int[] {};
		final int[] b = new int[] {};
		int i = 1;
		final int l = 0;
		final int e = 1;
		while (i < l) {
			final double k = Math.log(b[i]) / Math.log(1 + e);
			int q = 0;
			// bins = bins;
			while (k == (Math.log(b[i]) / Math.log(1 + e))) {
				q = q + p[i];
				i++;
			}
		}
		return bins;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private double estimateMin() {
		final List p = new ArrayList();

		while (!p.isEmpty()) {

			final Object[] bins = this.computeBins();
			double w = 1;
			double U = 0;
			double V = 0;
			final double q = 0;
			for (final Object k : bins) {
				U = ((q / w) * V) + U;
				V = (1 - (q / w)) * V;
				w = w - q;
			}

		}
		final double V = 0;
		final double U = 0;
		final double e = 1;
		double min = 0;
		final double n = 0;
		final double t = (2 * Math.log(n)) / Math.log(1 + e);
		for (int i = 0; i <= t; i++) {
			double tmp = 0;
			for (int j = 0; j <= (i - 1); j++) {
				tmp *= V;
			}
			min += Math.pow((1 + e), i) * U * tmp;
		}
		return min;

	}
}
