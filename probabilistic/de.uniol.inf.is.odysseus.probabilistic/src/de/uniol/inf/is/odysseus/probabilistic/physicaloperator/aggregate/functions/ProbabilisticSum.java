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
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;

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
	private final int pos;

	public static ProbabilisticSum getInstance(final int pos) {
		ProbabilisticSum ret = ProbabilisticSum.instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticSum(pos);
			ProbabilisticSum.instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticSum(final int pos) {
		super("SUM");
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
		final SumPartialAggregate<Tuple<?>> pa = new SumPartialAggregate<Tuple<?>>();

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) in
				.getAttribute(this.pos)).getValues().entrySet()) {
			pa.add(value.getKey(), value.getValue());
		}
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(
			final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge,
			final boolean createNew) {
		SumPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new SumPartialAggregate<Tuple<?>>(
					((SumPartialAggregate<Tuple<?>>) p).getSum());
		} else {
			pa = (SumPartialAggregate<Tuple<?>>) p;
		}

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) toMerge
				.getAttribute(this.pos)).getValues().entrySet()) {
			pa.add(value.getKey(), value.getValue());
		}

		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
		final SumPartialAggregate<Tuple<?>> pa = (SumPartialAggregate<Tuple<?>>) p;
		final Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Double(pa.getSum()));
		return r;
	}

}
