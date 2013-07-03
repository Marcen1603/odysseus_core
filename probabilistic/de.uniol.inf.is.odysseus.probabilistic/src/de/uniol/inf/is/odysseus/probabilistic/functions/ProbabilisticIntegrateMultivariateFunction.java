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

package de.uniol.inf.is.odysseus.probabilistic.functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class ProbabilisticIntegrateMultivariateFunction extends
		AbstractProbabilisticFunction<Double> {

	/**
     * 
     */
	private static final long serialVersionUID = 144107943090837242L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE },
			{ SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT,
					SDFDatatype.VECTOR_DOUBLE },
			{ SDFDatatype.VECTOR_BYTE, SDFDatatype.VECTOR_FLOAT,
					SDFDatatype.VECTOR_DOUBLE } };

	@Override
	public String getSymbol() {
		return "int";
	}

	@Override
	public Double getValue() {
		ProbabilisticContinuousDouble continuousDouble = (ProbabilisticContinuousDouble) this
				.getInputValue(0);
		RealVector lowerBound = MatrixUtils.createRealVector(((double[][]) this
				.getInputValue(1))[0]);
		RealVector upperBound = MatrixUtils.createRealVector(((double[][]) this
				.getInputValue(2))[0]);
		return getValueInternal(continuousDouble, lowerBound, upperBound);
	}

	protected double getValueInternal(ProbabilisticContinuousDouble function,
			RealVector lowerBound, RealVector upperBound) {
		return cumulativeProbability(function, lowerBound, upperBound);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(
					this.getSymbol()
							+ " has only "
							+ this.getArity()
							+ " argument: A distribution and the lower and upper support.");
		}
		return accTypes[argPos];
	}

	private double cumulativeProbability(
			final ProbabilisticContinuousDouble distribution,
			final RealVector lowerBound, final RealVector upperBound) {

		double probability = 0.0;

		NormalDistributionMixture mixtures = getDistributions(distribution
				.getDistribution());
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

	private double univariateCumulativeProbability(
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

	private double multivariateCumulativeProbability(
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

	/**
	 * Test main
	 * 
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ProbabilisticIntegrateMultivariateFunction function = new ProbabilisticIntegrateMultivariateFunction();

		int variate = 4;
		int multi = 4;
		final List<CovarianceMatrix> covarianceMatrixStore = new ArrayList<CovarianceMatrix>(
				variate);
		for (int i = 0; i < variate; i++) {
			final int size = multi + 6;
			final double[] entries = new double[size];
			for (int j = 0; j < entries.length; j++) {
				entries[j] = j + 1;
			}
			covarianceMatrixStore.add(new CovarianceMatrix(entries));
		}
		ProbabilisticContinuousDouble mixtureDistribution = new ProbabilisticContinuousDouble(
				0);
		final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();
		final double[] mean = new double[multi];
		for (int i = 0; i < multi; i++) {
			mean[i] = i;
		}
		for (int i = 0; i < multi; i++) {
			final Integer dimension = i;

			final NormalDistribution distribution = new NormalDistribution(
					mean, covarianceMatrixStore.get(0));
			mixtures.put(distribution, 1.0 / multi);
		}

		function.getDistributions()
				.add(new NormalDistributionMixture(mixtures));

		Map<String, Serializable> content = new HashMap<String, Serializable>();
		// content.put("covariance_matrix_store", covarianceMatrixStore);
		// function.setAdditionalContent(content);
		function.setArguments(new Constant<ProbabilisticContinuousDouble>(
				mixtureDistribution,
				SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE),
				new Constant<double[][]>(
						new double[][] { { 1.0, 1.0, 1.0, 1.0 } },
						SDFDatatype.VECTOR_DOUBLE), new Constant<double[][]>(
						new double[][] { { 2.0, 2.0, 2.0, 2.0 } },
						SDFDatatype.VECTOR_DOUBLE));

		Double value = function.getValue();
		System.out.println("Mixture: " + mixtureDistribution);
		System.out.println("CoVariance: " + covarianceMatrixStore);
		for (CovarianceMatrix covarianceMatrix : covarianceMatrixStore) {
			System.out.println("CoVariance Matrix : "
					+ CovarianceMatrixUtils.toMatrix(covarianceMatrix)
					+ " Size: " + covarianceMatrix.size());
		}
		System.out.println(value);
		assert (value == 3.0);
	}
}
