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
public class ProbabilisticAvg extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2188835286391575126L;
	private static Map<Integer, ProbabilisticAvg> instances = new HashMap<Integer, ProbabilisticAvg>();
	// TODO Move to a global configuration
	private static final double ERROR = 0.25;
	private static final double BOUND = 0.75;
	private final int pos;

	public static ProbabilisticAvg getInstance(final int pos, final boolean partialAggregateInput) {
		ProbabilisticAvg ret = ProbabilisticAvg.instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticAvg(pos, partialAggregateInput);
			ProbabilisticAvg.instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticAvg(final int pos, final boolean partialAggregateInput) {
		super("AVG", partialAggregateInput);
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
		final AvgPartialAggregate<ProbabilisticTuple<?>> pa = new AvgPartialAggregate<ProbabilisticTuple<?>>(ProbabilisticAvg.ERROR, ProbabilisticAvg.BOUND);
		for (final Entry<Double, Double> value : ((ProbabilisticDouble) in.getAttribute(this.pos)).getValues().entrySet()) {
			pa.update(value.getKey(), value.getValue());
		}
		return pa;
	}

	@Override
	public IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
		AvgPartialAggregate<ProbabilisticTuple<?>> pa = null;
		if (createNew) {
			pa = new AvgPartialAggregate<ProbabilisticTuple<?>>(ProbabilisticAvg.ERROR, ProbabilisticAvg.BOUND);
		} else {
			pa = (AvgPartialAggregate<ProbabilisticTuple<?>>) p;
		}

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) toMerge.getAttribute(this.pos)).getValues().entrySet()) {
			pa.update(value.getKey(), value.getValue());
		}
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
		final AvgPartialAggregate<ProbabilisticTuple<?>> pa = (AvgPartialAggregate<ProbabilisticTuple<?>>) p;
		final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, false);
		r.setAttribute(0, new Double(pa.getAvg()));
		return r;
	}

}
