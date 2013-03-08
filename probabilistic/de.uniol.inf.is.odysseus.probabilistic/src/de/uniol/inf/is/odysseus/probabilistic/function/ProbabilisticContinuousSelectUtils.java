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

package de.uniol.inf.is.odysseus.probabilistic.function;

import java.util.Map.Entry;

import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public final class ProbabilisticContinuousSelectUtils {

	public static double cumulativeProbability(
			final NormalDistributionMixture mixtures,
			final RealVector lowerBound, final RealVector upperBound) {

		double probability = 0.0;

		int dimension = mixtures.getDimension();
		if (dimension == 1) {
			probability = univariateCumulativeProbability(mixtures,
					lowerBound.getEntry(0), upperBound.getEntry(0));
		} else {
			probability = multivariateCumulativeProbability(mixtures,
					lowerBound, upperBound);
		}
		return probability;
	}

	private static double univariateCumulativeProbability(
			final NormalDistributionMixture distribution,
			final double lowerBound, final double upperBound) {
		double probability = 0.0;
		for (Entry<NormalDistribution, Double> mixture : distribution
				.getMixtures().entrySet()) {
			NormalDistribution normalDistribution = mixture.getKey();
			Double weight = mixture.getValue();
			org.apache.commons.math3.distribution.NormalDistribution tmpDistribution = new org.apache.commons.math3.distribution.NormalDistribution(
					normalDistribution.getMean()[0], normalDistribution
							.getCovarianceMatrix().getEntries()[0]);
			probability += tmpDistribution.cumulativeProbability(lowerBound,
					upperBound) * weight;
		}
		return probability;
	}

	private static double multivariateCumulativeProbability(
			final NormalDistributionMixture distribution,
			final RealVector lowerBound, final RealVector upperBound) {
		double probability = 0.0;
		for (Entry<NormalDistribution, Double> mixture : distribution
				.getMixtures().entrySet()) {
			NormalDistribution normalDistribution = mixture.getKey();
			Double weight = mixture.getValue();
			Matrix covarianceMatrix = new Matrix(normalDistribution
					.getCovarianceMatrix().getMatrix().getData());
			Matrix lower = new Matrix(new double[][] { lowerBound.toArray() });
			Matrix upper = new Matrix(new double[][] { upperBound.toArray() });
			probability += QSIMVN.cumulativeProbability(5000, covarianceMatrix,
					lower, upper).p * weight;
		}
		return probability;
	}

	private ProbabilisticContinuousSelectUtils() {
	}
}
