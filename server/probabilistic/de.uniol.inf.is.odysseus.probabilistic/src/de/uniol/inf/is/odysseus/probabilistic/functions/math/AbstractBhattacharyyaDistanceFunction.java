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
package de.uniol.inf.is.odysseus.probabilistic.functions.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
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
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractBhattacharyyaDistanceFunction extends AbstractProbabilisticFunction<Double> {

    private static final long serialVersionUID = -4978963210815853709L;

    public AbstractBhattacharyyaDistanceFunction(final SDFDatatype[][] accTypes) {
        super("similarity", 2, accTypes, SDFDatatype.DOUBLE);
    }

    /**
     * 
     * @param a
     *            The normal distribution mixture
     * @param b
     *            The other distribution
     * @return The distance measure
     */
    protected final double getValueInternal(final MultivariateMixtureDistribution a, final MultivariateMixtureDistribution b) {
        double weightedBhattacharyyaDistance = 0.0;
        for (final Pair<Double, IMultivariateDistribution> aEntry : a.getComponents()) {
            final RealMatrix aMean = MatrixUtils.createColumnRealMatrix(aEntry.getValue().getMean());
            final RealMatrix aCovariance = new Array2DRowRealMatrix(aEntry.getValue().getVariance());
            final double aDeterminant = this.getDeterminant(aCovariance);
            for (final Pair<Double, IMultivariateDistribution> bEntry : b.getComponents()) {
                final RealMatrix bMean = MatrixUtils.createColumnRealMatrix(bEntry.getValue().getMean());
                final RealMatrix bCovariance = new Array2DRowRealMatrix(bEntry.getValue().getVariance());
                final double bDeterminant = this.getDeterminant(aCovariance);

                final RealMatrix avgCovariance = aCovariance.add(bCovariance).scalarMultiply(0.5);
                final double avgDeterminant = this.getDeterminant(avgCovariance);
                DecompositionSolver solver;
                try {
                    solver = new CholeskyDecomposition(avgCovariance).getSolver();
                }
                catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
                    solver = new LUDecomposition(avgCovariance).getSolver();
                }
                final RealMatrix avgCovarianceInverse = solver.getInverse();

                final RealMatrix bhattacharyyaDistanceTerm1 = aMean.subtract(bMean).transpose().multiply(avgCovarianceInverse).multiply(aMean.subtract(bMean)).scalarMultiply(1.0 / 8.0);
                final double bhattacharyyaDistanceTerm2 = 0.5 * FastMath.log(avgDeterminant / FastMath.sqrt(aDeterminant * bDeterminant));
                final RealMatrix bhattacharyyaDistance = bhattacharyyaDistanceTerm1.scalarAdd(bhattacharyyaDistanceTerm2);
                // FIXME 20140319 christian@kuka.cc IS this correct?
                final double weight = aEntry.getKey() * bEntry.getKey();
                weightedBhattacharyyaDistance += bhattacharyyaDistance.getEntry(0, 0) * weight;

            }
        }
        return weightedBhattacharyyaDistance;
    }

    /**
     * Calculates the determinat of the given matrix using Cholesky
     * decomposition first and fall back on LU decomposition on error.
     * 
     * @param matrix
     *            The matrix
     * @return The determinant
     */
    private double getDeterminant(final RealMatrix matrix) {
        double determinant;
        try {
            determinant = new CholeskyDecomposition(matrix).getDeterminant();
        }
        catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
            determinant = new LUDecomposition(matrix).getDeterminant();
        }
        return determinant;
    }
}
