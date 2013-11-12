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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 *         FIXME Implement probabilistic Min aggregation function
 */
public class ProbabilisticDiscreteMin extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4241950598685654559L;
	/** The attribute position. */
	@SuppressWarnings("unused")
	private final int pos;
	/** The result data type. */
	private final String datatype;

	/**
	 * Gets an instance of {@link ProbabilisticDiscreteMin}.
	 * 
	 * @param pos
	 *            The attribute position
	 * @param partialAggregateInput
	 *            The partial aggregate input
	 * @param datatype
	 *            The result datatype
	 * @return An instance of {@link ProbabilisticDiscreteMin}
	 */
	public static ProbabilisticDiscreteMin getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {

		return new ProbabilisticDiscreteMin(pos, partialAggregateInput, datatype);
	}

	/**
	 * Creates a new instance of {@link ProbabilisticDiscreteMin}.
	 * 
	 * @param pos
	 *            The attribute position
	 * @param partialAggregateInput
	 *            The partial aggregate input
	 * @param datatype
	 *            The result datatype
	 */
	protected ProbabilisticDiscreteMin(final int pos, final boolean partialAggregateInput, final String datatype) {
		super("MIN", partialAggregateInput);
		this.pos = pos;
		this.datatype = datatype;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IInitializer#init(java.lang.Object)
	 */
	@Override
	public final IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
		return new ElementPartialAggregate<ProbabilisticTuple<?>>(in, this.datatype);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IMerger#merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate, java.lang.Object, boolean)
	 */
	@Override
	public final IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
		final ElementPartialAggregate<ProbabilisticTuple<?>> pa = null;

		return pa;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IEvaluator#evaluate(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate)
	 */
	@Override
	public final ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
		final ElementPartialAggregate<ProbabilisticTuple<?>> pa = (ElementPartialAggregate<ProbabilisticTuple<?>>) p;
		return pa.getElem();
	}

	/**
	 * TEST CODE.
	 * 
	 * @return Computed bins
	 */
	private Object[] computeBins() {
		final Object[] bins = new Object[] {};
		// final int[] p = new int[] {};
		// final int[] b = new int[] {};
		// int i = 1;
		// final int l = 0;
		// final int e = 1;
		// while (i < l) {
		// final double k = Math.log(b[i]) / Math.log(1 + e);
		// int q = 0;
		// bins = bins;
		// FIXME Test for floating point equality
		// while (k == (Math.log(b[i]) / Math.log(1 + e))) {
		// q = q + p[i];
		// i++;
		// }
		// }
		return bins;
	}

	/**
	 * TEST CODE.
	 * 
	 * @return estimated mi9nimum
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private double estimateMin() {
		final List p = new ArrayList();

		while (!p.isEmpty()) {

			final Object[] bins = this.computeBins();
			double w = 1;
			double u = 0;
			double v = 0;
			final double q = 0;
			for (final Object k : bins) {
				u = ((q / w) * v) + u;
				v = (1 - (q / w)) * v;
				w = w - q;
			}

		}
		final double v = 0;
		final double u = 0;
		final double e = 1;
		double min = 0;
		final double n = 0;
		final double t = (2 * Math.log(n)) / Math.log(1 + e);
		for (int i = 0; i <= t; i++) {
			double tmp = 0;
			for (int j = 0; j <= (i - 1); j++) {
				tmp *= v;
			}
			min += Math.pow((1 + e), i) * u * tmp;
		}
		return min;

	}
}
