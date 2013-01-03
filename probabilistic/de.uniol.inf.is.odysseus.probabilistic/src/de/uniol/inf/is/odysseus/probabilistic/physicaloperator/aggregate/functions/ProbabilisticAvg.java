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

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;

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
	// TODO Move to a global configuration
	private static final double ERROR = 0.25;
	private static final double BOUND = 0.75;
	private final int pos;

	public static ProbabilisticCount getInstance(final int pos) {
		ProbabilisticCount ret = ProbabilisticAvg.instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticCount(pos);
			ProbabilisticAvg.instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticAvg(final int pos) {
		super("AVG");
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
		final AvgPartialAggregate<Tuple<?>> pa = new AvgPartialAggregate<Tuple<?>>(
				ProbabilisticAvg.ERROR, ProbabilisticAvg.BOUND);
		for (final IPair<Double, Double> value : ((ProbabilisticDouble) in
				.getAttribute(this.pos)).getValues()) {
			pa.update(value.getE1(), value.getE2());
		}
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(
			final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge,
			final boolean createNew) {
		AvgPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new AvgPartialAggregate<Tuple<?>>(ProbabilisticAvg.ERROR,
					ProbabilisticAvg.BOUND);
		} else {
			pa = (AvgPartialAggregate<Tuple<?>>) p;
		}

		for (final IPair<Double, Double> value : ((ProbabilisticDouble) toMerge
				.getAttribute(this.pos)).getValues()) {
			pa.update(value.getE1(), value.getE2());
		}
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
		final AvgPartialAggregate<Tuple<?>> pa = (AvgPartialAggregate<Tuple<?>>) p;
		final Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Double(pa.getAvg()));
		return r;
	}

}
