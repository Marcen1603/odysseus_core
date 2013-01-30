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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 *         FIXME Implement probabilistic StdDev aggregation function
 */
public class ProbabilisticStdDev extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -45894921488698597L;
	private static Map<Integer, ProbabilisticStdDev> instances = new HashMap<Integer, ProbabilisticStdDev>();
	@SuppressWarnings("unused")
	private final int pos;

	public static ProbabilisticStdDev getInstance(final int pos) {
		ProbabilisticStdDev ret = ProbabilisticStdDev.instances.get(pos);
		if (ret == null) {
			ret = new ProbabilisticStdDev(pos);
			ProbabilisticStdDev.instances.put(pos, ret);
		}
		return ret;
	}

	protected ProbabilisticStdDev(final int pos) {
		super("STDDEV");
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
}
