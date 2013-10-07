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
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractBhattacharyyaDistanceFunction extends AbstractProbabilisticFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4978963210815853709L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int getArity() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getSymbol() {
		return "similarity";
	}

	/**
	 * 
	 * @param a
	 *            The normal distribution mixture
	 * @param b
	 *            The other distribution
	 * @return The distance measure
	 */
	protected final double getValueInternal(final NormalDistributionMixture a, final NormalDistributionMixture b) {
		double weightedBhattacharyyaDistance = 0.0;
		for (final Pair<Double, MultivariateNormalDistribution> aEntry : a.getMixtures().getComponents()) {
			final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMeans());
			final RealMatrix aCovariance = aEntry.getValue().getCovariances();
			final double aDeterminant = getDeterminant(aCovariance);
			for (final Pair<Double, MultivariateNormalDistribution> bEntry : b.getMixtures().getComponents()) {
				final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getValue().getMeans());
				final RealMatrix bCovariance = bEntry.getValue().getCovariances();
				final double bDeterminant = getDeterminant(aCovariance);

				RealMatrix avgCovariance = aCovariance.add(bCovariance).scalarMultiply(0.5);
				final double avgDeterminant = getDeterminant(avgCovariance);
				DecompositionSolver solver;
				try {
					solver = new CholeskyDecomposition(avgCovariance).getSolver();
				} catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
					solver = new LUDecomposition(avgCovariance).getSolver();
				}
				RealMatrix avgCovarianceInverse = solver.getInverse();

				RealMatrix bhattacharyyaDistanceTerm1 = aMean.subtract(bMean).transpose().multiply(avgCovarianceInverse).multiply(aMean.subtract(bMean)).scalarMultiply(1.0 / 8.0);
				double bhattacharyyaDistanceTerm2 = 0.5 * FastMath.log(avgDeterminant / FastMath.sqrt(aDeterminant * bDeterminant));
				RealMatrix bhattacharyyaDistance = bhattacharyyaDistanceTerm1.scalarAdd(bhattacharyyaDistanceTerm2);
				// FIXME IS this correct?
				double weight = aEntry.getKey() * bEntry.getKey();
				weightedBhattacharyyaDistance += bhattacharyyaDistance.getEntry(0, 0) * weight;

			}
		}
		return weightedBhattacharyyaDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	/**
	 * Calculates the determinat of the given matrix using Cholesky decomposition first and fall back on LU decomposition on error.
	 * 
	 * @param matrix
	 *            The matrix
	 * @return The determinant
	 */
	private double getDeterminant(final RealMatrix matrix) {
		double determinant;
		try {
			determinant = new CholeskyDecomposition(matrix).getDeterminant();
		} catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
			determinant = new LUDecomposition(matrix).getDeterminant();
		}
		return determinant;
	}
}
