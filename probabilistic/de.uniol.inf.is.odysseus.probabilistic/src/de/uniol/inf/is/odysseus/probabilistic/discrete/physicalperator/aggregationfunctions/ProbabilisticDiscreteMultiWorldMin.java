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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 *         FIXME Implement probabilistic Min aggregation function
 */
public class ProbabilisticDiscreteMultiWorldMin extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6400744203001252061L;
	/** The attribute position. */
	private final int pos;
	/** The result data type. */
	private final String datatype;

	/**
	 * Gets an instance of {@link ProbabilisticDiscreteMultiWorldMin}.
	 * 
	 * @param pos
	 *            The attribute position
	 * @param partialAggregateInput
	 *            The partial aggregate input
	 * @param datatype
	 *            The result datatype
	 * @return An instance of {@link ProbabilisticDiscreteMultiWorldMin}
	 */
	public static ProbabilisticDiscreteMultiWorldMin getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
		return new ProbabilisticDiscreteMultiWorldMin(pos, partialAggregateInput, datatype);
	}

	/**
	 * Creates a new instance of {@link ProbabilisticDiscreteMultiWorldMin}.
	 * 
	 * @param pos
	 *            The attribute position
	 * @param partialAggregateInput
	 *            The partial aggregate input
	 * @param datatype
	 *            The result datatype
	 */
	protected ProbabilisticDiscreteMultiWorldMin(final int pos, final boolean partialAggregateInput, final String datatype) {
		super("MIN", partialAggregateInput);
		this.pos = pos;
		this.datatype = datatype;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions .IInitializer#init(java.lang.Object)
	 */
	@Override
	public final IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
		final MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>> pa = new MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>>(this.datatype, false);

		pa.add((AbstractProbabilisticValue<?>) in.getAttribute(this.pos));

		return pa;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions . IMerger#merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate .basefunctions.IPartialAggregate, java.lang.Object, boolean)
	 */
	@Override
	public final IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
		MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>> pa = null;
		if (createNew) {
			pa = new MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>>(((MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>>) p).getAggregate(), this.datatype, false);
		} else {
			pa = (MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>>) p;
		}

		pa.add((AbstractProbabilisticValue<?>) toMerge.getAttribute(this.pos));

		return pa;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions . IEvaluator#evaluate(de.uniol.inf.is.odysseus.core.server.physicaloperator .aggregate.basefunctions.IPartialAggregate)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
		final MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>> pa = (MultiWorldMinMaxPartialAggregate<ProbabilisticTuple<?>>) p;
		final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, true);
		r.setAttribute(0, pa.getAggregate());
		return r;
	}

}
