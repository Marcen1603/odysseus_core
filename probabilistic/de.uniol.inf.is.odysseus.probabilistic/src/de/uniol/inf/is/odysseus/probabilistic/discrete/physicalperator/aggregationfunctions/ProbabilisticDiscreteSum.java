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

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticDiscreteSum extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6272207178324419258L;
	/** The attribute position. */
	private final int pos;
	/** The result data type. */
	private final String datatype;

	/**
	 * Gets an instance of {@link ProbabilisticDiscreteSum}.
	 * 
	 * @param pos
	 *            The attribute position
	 * @param partialAggregateInput
	 *            The partial aggregate input
	 * @param datatype
	 *            The result datatype
	 * @return An instance of {@link ProbabilisticDiscreteSum}
	 */
	public static ProbabilisticDiscreteSum getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
		return new ProbabilisticDiscreteSum(pos, partialAggregateInput, datatype);
	}

	/**
	 * Creates a new instance of {@link ProbabilisticDiscreteSum}.
	 * 
	 * @param pos
	 *            The attribute position
	 * @param partialAggregateInput
	 *            The partial aggregate input
	 * @param datatype
	 *            The result datatype
	 */
	protected ProbabilisticDiscreteSum(final int pos, final boolean partialAggregateInput, final String datatype) {
		super("SUM", partialAggregateInput);
		this.pos = pos;
		this.datatype = datatype;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IInitializer#init(java.lang.Object)
	 */
	@Override
	public final IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
		final SumPartialAggregate<ProbabilisticTuple<?>> pa = new SumPartialAggregate<ProbabilisticTuple<?>>(datatype);

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) in.getAttribute(this.pos)).getValues().entrySet()) {
			pa.add(value.getKey(), value.getValue());
		}
		return pa;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IMerger#merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate, java.lang.Object, boolean)
	 */
	@Override
	public final IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
		SumPartialAggregate<ProbabilisticTuple<?>> pa = null;
		if (createNew) {
			pa = new SumPartialAggregate<ProbabilisticTuple<?>>(((SumPartialAggregate<ProbabilisticTuple<?>>) p).getSum(), datatype);
		} else {
			pa = (SumPartialAggregate<ProbabilisticTuple<?>>) p;
		}

		for (final Entry<Double, Double> value : ((ProbabilisticDouble) toMerge.getAttribute(this.pos)).getValues().entrySet()) {
			pa.add(value.getKey(), value.getValue());
		}

		return pa;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IEvaluator#evaluate(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
		final SumPartialAggregate<ProbabilisticTuple<?>> pa = (SumPartialAggregate<ProbabilisticTuple<?>>) p;
		final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, false);
		r.setAttribute(0, new Double(pa.getSum()));
		return r;
	}

}
