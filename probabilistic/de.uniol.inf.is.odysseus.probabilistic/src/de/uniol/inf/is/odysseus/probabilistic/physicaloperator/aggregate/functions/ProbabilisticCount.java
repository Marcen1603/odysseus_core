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
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticCount extends
		AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8734164350164631514L;
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

	protected ProbabilisticCount(int pos) {
		super("COUNT");
		this.pos = pos;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		IPartialAggregate<Tuple<?>> pa = new CountPartialAggregate<Tuple<?>>(
				((IProbabilistic) in.getMetadata()).getProbability(pos));
		return pa;
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(IPartialAggregate<Tuple<?>> p,
			Tuple<?> toMerge, boolean createNew) {
		CountPartialAggregate<Tuple<?>> pa = null;
		if (createNew) {
			pa = new CountPartialAggregate<Tuple<?>>(
					((CountPartialAggregate<Tuple<?>>) p).getCount());
		} else {
			pa = (CountPartialAggregate<Tuple<?>>) p;
		}

		pa.add(((IProbabilistic) toMerge.getMetadata()).getProbability(pos));
		return pa;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		CountPartialAggregate<Tuple<?>> pa = (CountPartialAggregate<Tuple<?>>) p;
		Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Double(pa.getCount()));
		return r;
	}

}
