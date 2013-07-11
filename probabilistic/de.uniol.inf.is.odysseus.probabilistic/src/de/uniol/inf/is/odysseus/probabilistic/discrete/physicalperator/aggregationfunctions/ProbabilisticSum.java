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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticSum extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6272207178324419258L;
	private static Map<Integer, ProbabilisticSum> instances = new HashMap<Integer, ProbabilisticSum>();
	private final int pos;

	public static ProbabilisticSum getInstance(final int pos, final boolean partialAggregateInput) {
		ProbabilisticSum ret = ProbabilisticSum.instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticSum(pos, partialAggregateInput);
			ProbabilisticSum.instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticSum(final int pos, final boolean partialAggregateInput) {
		super("SUM", partialAggregateInput);
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
		final SumPartialAggregate<ProbabilisticTuple<?>> pa = new SumPartialAggregate<ProbabilisticTuple<?>>();

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) in.getAttribute(this.pos)).getValues().entrySet()) {
			pa.add(value.getKey(), value.getValue());
		}
		return pa;
	}

	@Override
	public IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
		SumPartialAggregate<ProbabilisticTuple<?>> pa = null;
		if (createNew) {
			pa = new SumPartialAggregate<ProbabilisticTuple<?>>(((SumPartialAggregate<ProbabilisticTuple<?>>) p).getSum());
		} else {
			pa = (SumPartialAggregate<ProbabilisticTuple<?>>) p;
		}

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) toMerge.getAttribute(this.pos)).getValues().entrySet()) {
			pa.add(value.getKey(), value.getValue());
		}

		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
		final SumPartialAggregate<ProbabilisticTuple<?>> pa = (SumPartialAggregate<ProbabilisticTuple<?>>) p;
		final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, false);
		r.setAttribute(0, new Double(pa.getSum()));
		return r;
	}

}
