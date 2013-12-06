/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.rcp.chart;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class NormalDistributionFunctionND {
    /** The covariance matrix. */
    private final RealMatrix covariance;
    /** The means. */
    private final double[] means;

    /**
     * Creates a new {@link NormalDistributionFunctionND} with the given means
     * and covariance.
     * 
     * @param means
     *            The means
     * @param covariance
     *            The covariance
     */
    public NormalDistributionFunctionND(final double[] means, final RealMatrix covariance) {
        this.covariance = covariance.copy();
        this.means = means.clone();
    }

    /**
     * Evaluates the given multivariate distribution for the given value.
     * 
     * @param x
     *            The value
     * @return The density at the given value
     */
    public final double getValue(final double[] x) {
        final int k = x.length;
        final RealMatrix z = this.covariance;
        double zDet = 0.0;
        RealMatrix zInverse = null;
        try {
            final CholeskyDecomposition decomposition = new CholeskyDecomposition(z);
            zDet = decomposition.getDeterminant();
            zInverse = decomposition.getSolver().getInverse();
        }
        catch (NonSymmetricMatrixException | NonPositiveDefiniteMatrixException e) {
            final LUDecomposition decomposition = new LUDecomposition(z);
            zDet = decomposition.getDeterminant();
            zInverse = decomposition.getSolver().getInverse();
        }
        final RealMatrix xCol = MatrixUtils.createColumnRealMatrix(x);
        final RealMatrix meansCol = MatrixUtils.createColumnRealMatrix(this.means);
        final RealMatrix xSubM = xCol.subtract(meansCol);

        final double first = 1 / (FastMath.pow(2 * Math.PI, k / 2) * FastMath.pow(zDet, 1 / 2));
        final RealMatrix factor1 = xSubM.transpose();
        final RealMatrix factor2 = factor1.multiply(zInverse);
        final RealMatrix factor3 = xSubM;
        final RealMatrix f4 = factor2.multiply(factor3);
        final double f5 = f4.getEntry(0, 0);
        final double second = -0.5 * f5;
        return first * FastMath.exp(second);
    }
}
