/*
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

package de.uniol.inf.is.odysseus.probabilistic.common;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVNResult;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class ProbabilisticContinuousUtils {
	/**
	 * Calculates the cumulative probability of the given normal distribution mixture.
	 * 
	 * @param mixtures
	 *            The normal distribution mixture
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability of the given normal distribution mixture.
	 */
	public static double cumulativeProbability(final NormalDistributionMixture mixtures, final RealVector lowerBound, final RealVector upperBound) {

		double probability = 0.0;
		final RealVector maxLowerBound = lowerBound.copy();
		final RealVector minUpperBound = upperBound.copy();
		for (int i = 0; i < mixtures.getDimension(); i++) {
			maxLowerBound.setEntry(i, FastMath.max(lowerBound.getEntry(i), mixtures.getSupport(i).inf()));
			minUpperBound.setEntry(i, FastMath.min(upperBound.getEntry(i), mixtures.getSupport(i).sup()));
		}
		final int dimension = mixtures.getDimension();
		if (dimension == 1) {
			probability = ProbabilisticContinuousUtils.univariateCumulativeProbability(mixtures, maxLowerBound.getEntry(0), minUpperBound.getEntry(0));
		} else {
			probability = ProbabilisticContinuousUtils.multivariateCumulativeProbability(mixtures, maxLowerBound, minUpperBound);
		}
		return probability;
	}

	/**
	 * Calculates the cumulative probability of the given univariate normal distribution mixture.
	 * 
	 * @param distribution
	 *            The normal distribution mixture
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability of the given normal distribution mixture.
	 */
	private static double univariateCumulativeProbability(final NormalDistributionMixture distribution, final double lowerBound, final double upperBound) {
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
	 * Calculates the cumulative probability of the given multivariate normal distribution mixture.
	 * 
	 * @param distribution
	 *            The normal distribution mixture
	 * @param lowerBound
	 *            The lower bound
	 * @param upperBound
	 *            The upper bound
	 * @return The cumulative probability of the given normal distribution mixture.
	 */
	private static double multivariateCumulativeProbability(final NormalDistributionMixture distribution, final RealVector lowerBound, final RealVector upperBound) {
		double probability = 0.0;
		for (final Pair<Double, MultivariateNormalDistribution> entry : distribution.getMixtures().getComponents()) {
			final MultivariateNormalDistribution normalDistribution = entry.getValue();
			final Double weight = entry.getKey();
			final Matrix covarianceMatrix = new Matrix(normalDistribution.getCovariances().getData());
			final Matrix lower = new Matrix(new double[][] { lowerBound.subtract(MatrixUtils.createRealVector(normalDistribution.getMeans())).toArray() });
			final Matrix upper = new Matrix(new double[][] { upperBound.subtract(MatrixUtils.createRealVector(normalDistribution.getMeans())).toArray() });
			final QSIMVNResult result = QSIMVN.cumulativeProbability(ProbabilisticConstants.SAMPLES, covarianceMatrix, lower, upper);
			probability += result.p * weight;
		}
		return probability;
	}

	/**
	 * Private constructor.
	 */
	private ProbabilisticContinuousUtils() {
	}
}
