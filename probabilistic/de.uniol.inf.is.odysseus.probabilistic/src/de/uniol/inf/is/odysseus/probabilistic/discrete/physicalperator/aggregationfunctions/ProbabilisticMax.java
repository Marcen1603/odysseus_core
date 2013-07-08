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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 *         FIXME Implement probabilistic Max aggregation function
 */
public class ProbabilisticMax extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8103927421161762878L;
	// private static Map<Integer, ProbabilisticMax> instances = new HashMap<Integer, ProbabilisticMax>();
	@SuppressWarnings("unused")
	private final int pos;
	final private String datatype;

	public static ProbabilisticMax getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
		return new ProbabilisticMax(pos, partialAggregateInput, datatype);
	}

	protected ProbabilisticMax(final int pos, final boolean partialAggregateInput, final String datatype) {
		super("MAX", partialAggregateInput);
		this.pos = pos;
		this.datatype = datatype;
	}

	@Override
	public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
		return new ElementPartialAggregate<Tuple<?>>(in, this.datatype);
	}

	@Override
	public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge, final boolean createNew) {
		final ElementPartialAggregate<Tuple<?>> pa = null;

		return pa;
	}

	@Override
	public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
		final ElementPartialAggregate<Tuple<?>> pa = (ElementPartialAggregate<Tuple<?>>) p;
		return pa.getElem();
	}

}
