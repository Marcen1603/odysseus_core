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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.Count;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.CountPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

@SuppressWarnings({ "rawtypes" })
public class RelationalCount extends Count<Tuple<?>, Tuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -788185013111220731L;
	private final int pos;

	private RelationalCount(int pos, boolean partialAggregateInput) {
		super(partialAggregateInput);
		this.pos = pos;
	}

	public static RelationalCount getInstance(int pos,
			boolean partialAggregateInput) {
		return new RelationalCount(pos, partialAggregateInput);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPartialAggregate<Tuple<?>> init(Tuple<?> in) {
		if (isPartialAggregateInput()) {
			return super.init((IPartialAggregate) in.getAttribute(pos));
		} else {
			return super.init(in);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized IPartialAggregate<Tuple<?>> merge(
			IPartialAggregate<Tuple<?>> p, Tuple<?> toMerge, boolean createNew) {
		if (isPartialAggregateInput()) {
			return super.init((IPartialAggregate) toMerge.getAttribute(pos));
		} else {
			return super.merge(p, toMerge, createNew);
		}
	}

	@Override
	public Tuple<?> evaluate(IPartialAggregate<Tuple<?>> p) {
		CountPartialAggregate<Tuple<?>> pa = (CountPartialAggregate<Tuple<?>>) p;
		Tuple<?> r = new Tuple(1, false);
		r.setAttribute(0, new Integer(pa.getCount()));
		return r;
	}

}
