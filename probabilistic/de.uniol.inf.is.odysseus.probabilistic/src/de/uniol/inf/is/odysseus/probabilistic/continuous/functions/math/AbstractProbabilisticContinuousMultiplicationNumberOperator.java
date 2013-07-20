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

import java.util.Map;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;

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
public abstract class AbstractProbabilisticContinuousMultiplicationNumberOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 395701154091726221L;

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IOperator#getPrecedence()
	 */
	@Override
	public final int getPrecedence() {
		return 5;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "*";
	}

	/**
	 * Multiplies the given distribution by the given value.
	 * 
	 * @param a
	 *            The distribution
	 * @param b
	 *            The value
	 * @return The distribution of a*b
	 */
	protected final NormalDistributionMixture getValueInternal(final NormalDistributionMixture a, final Double b) {
		final NormalDistributionMixture result = a.clone();
		result.getMixtures().clear();
		for (final Map.Entry<MultivariateNormalDistribution, Double> entry : a.getMixtures().entrySet()) {
			final double[] means = entry.getKey().getMeans();
			for (int i = 0; i < means.length; i++) {
				means[i] *= b;
			}
			final RealMatrix covariances = entry.getKey().getCovariances().scalarMultiply(b);
			result.getMixtures().put(new MultivariateNormalDistribution(means, covariances.getData()), entry.getValue());
		}
		final Interval[] support = new Interval[result.getSupport().length];
		for (int i = 0; i < result.getSupport().length; i++) {
			support[i] = result.getSupport(i).mul(b);
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
		return true;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IBinaryOperator#isAssociative()
	 */
	@Override
	public final boolean isAssociative() {
		return true;
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
