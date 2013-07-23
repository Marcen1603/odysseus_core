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

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticIntegrateMultivariateFunction extends AbstractProbabilisticFunction<Double> {

	/**
     * 
     */
	private static final long serialVersionUID = 144107943090837242L;
	/**
	 * Accepted data types.
	 */
	public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE }, { SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE }, 
		{ SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT, SDFDatatype.VECTOR_DOUBLE } };

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "int";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public Double getValue() {
		final ProbabilisticContinuousDouble continuousDouble = (ProbabilisticContinuousDouble) this.getInputValue(0);
		final RealVector lowerBound = MatrixUtils.createRealVector(((double[][]) this.getInputValue(1))[0]);
		final RealVector upperBound = MatrixUtils.createRealVector(((double[][]) this.getInputValue(2))[0]);
		return this.getValueInternal(continuousDouble, lowerBound, upperBound);
	}

	/**
	 * Integrates the given distribution from the lower to the upper bound.
	 * 
	 * @param function
	 *            The distribution
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability in the given bound
	 */
	protected final double getValueInternal(final ProbabilisticContinuousDouble function, final RealVector lowerBound, final RealVector upperBound) {
		return this.cumulativeProbability(function, lowerBound, upperBound);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
	 */
	@Override
	public final int getArity() {
		return 3;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument: A distribution and the lower and upper support.");
		}
		return ProbabilisticIntegrateMultivariateFunction.ACC_TYPES[argPos];
	}

	/**
	 * Calculates the cumulative probability of the given distribution between the lower and the upper bound.
	 * 
	 * @param distribution
	 *            The distribution
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability
	 */
	private double cumulativeProbability(final ProbabilisticContinuousDouble distribution, final RealVector lowerBound, final RealVector upperBound) {
		double probability = 0.0;
		final NormalDistributionMixture mixtures = this.getDistributions(distribution.getDistribution());
		final int dimension = mixtures.getDimension();
		if (dimension == 1) {
			probability = this.univariateCumulativeProbability(mixtures, lowerBound.getEntry(0), upperBound.getEntry(0));
		} else {
			probability = this.multivariateCumulativeProbability(mixtures, lowerBound, upperBound);
		}
		return probability;
	}

	/**
	 * Calculates the cumulative probability of the given univariate distribution between the lower and the upper bound.
	 * 
	 * @param distribution
	 *            The univariate distribution
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability
	 */
	private double univariateCumulativeProbability(final NormalDistributionMixture distribution, final double lowerBound, final double upperBound) {
		double probability = 0.0;
		for (final Pair<Double, MultivariateNormalDistribution> entry : distribution.getMixtures().getComponents()) {
			final MultivariateNormalDistribution normalDistribution = entry.getValue();
			final Double weight = entry.getKey();
			final NormalDistribution tmpDistribution = new NormalDistribution(normalDistribution.getMeans()[0], normalDistribution.getCovariances().getEntry(0, 0));
			probability += tmpDistribution.probability(lowerBound, upperBound) * weight;
		}
		return probability;
	}

	/**
	 * Calculates the cumulative probability of the given multivariate distribution between the lower and the upper bound.
	 * 
	 * @param distribution
	 *            The multivariate distribution
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability
	 */
	private double multivariateCumulativeProbability(final NormalDistributionMixture distribution, final RealVector lowerBound, final RealVector upperBound) {
		double probability = 0.0;
		for (final Pair<Double, MultivariateNormalDistribution> entry : distribution.getMixtures().getComponents()) {
			final MultivariateNormalDistribution normalDistribution = entry.getValue();
			final Double weight = entry.getKey();
			final Matrix covarianceMatrix = new Matrix(normalDistribution.getCovariances().getData());
			final Matrix lower = new Matrix(new double[][] { lowerBound.toArray() });
			final Matrix upper = new Matrix(new double[][] { upperBound.toArray() });
			probability += QSIMVN.cumulativeProbability(5000, covarianceMatrix, lower, upper).p * weight;
		}
		return probability;
	}

}
