/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractProbabilisticContinuousMinusNumberOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3009938859392034200L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getPrecedence()
	 */
	@Override
	public final int getPrecedence() {
		return 6;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "-";
	}

	/**
	 * Subtracts the given value from the given distribution.
	 * 
	 * @param a
	 *            The distribution
	 * @param b
	 *            The value
	 * @return The distribution of a-b
	 */
	protected final NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final Double b) {
		final NormalDistributionMixture result = a.clone();
		final List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		for (final Pair<Double, MultivariateNormalDistribution> entry : a.getMixtures().getComponents()) {
			final MultivariateNormalDistribution normalDistribution = entry.getValue();
			final Double weight = entry.getKey();
			final double[] means = normalDistribution.getMeans();
			for (int i = 0; i < means.length; i++) {
				means[i] -= b;
			}
			final MultivariateNormalDistribution component = new MultivariateNormalDistribution(means, normalDistribution.getCovariances().getData());
			mvns.add(new Pair<Double, MultivariateNormalDistribution>(weight, component));
		}
		result.setMixtures(new MixtureMultivariateNormalDistribution(mvns));
		final Interval[] support = new Interval[result.getSupport().length];
		for (int i = 0; i < result.getSupport().length; i++) {
			support[i] = result.getSupport(i).sub(b);
		}
		result.setSupport(support);
		return result;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getAssociativity()
	 */
	@Override
	public final de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isCommutative()
	 */
	@Override
	public boolean isCommutative() {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isAssociative()
	 */
	@Override
	public final boolean isAssociative() {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isLeftDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isLeftDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isRightDistributiveWith(de.uniol.inf.is.odysseus.core.server.mep.IOperator)
	 */
	@Override
	public final boolean isRightDistributiveWith(final IOperator<NormalDistributionMixture> operator) {
		return false;
	}

}
