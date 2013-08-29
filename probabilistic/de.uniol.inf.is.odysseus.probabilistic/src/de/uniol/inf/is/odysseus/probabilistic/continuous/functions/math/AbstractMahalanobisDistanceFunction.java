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
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractMahalanobisDistanceFunction extends AbstractProbabilisticFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4636005230557634127L;

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
		return "distance";
	}

	/**
	 * Integrates the given distribution from the lower to the upper bound.
	 * 
	 * @param a
	 *            The distribution
	 * @param b
	 *            The distribution
	 * @return The cumulative probability in the given bound
	 */
	protected final double getValueInternal(final NormalDistributionMixture a, final RealMatrix b) {
		double weightedMahalanobisDistance = 0.0;
		for (final Pair<Double, MultivariateNormalDistribution> aEntry : a.getMixtures().getComponents()) {
			final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMeans());
			final RealMatrix aCovariance = aEntry.getValue().getCovariances();
			DecompositionSolver solver;
			try {
				solver = new CholeskyDecomposition(aCovariance).getSolver();
			} catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
				solver = new LUDecomposition(aCovariance).getSolver();
			}
			RealMatrix aCovarianceInverse = solver.getInverse();

			RealMatrix mahalanobisDistance = b.subtract(aMean).transpose().multiply(aCovarianceInverse).multiply(b.subtract(aMean));
			// FIXME IS this correct?
			weightedMahalanobisDistance += mahalanobisDistance.getEntry(0, 0) * aEntry.getKey();
		}
		return weightedMahalanobisDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

}
