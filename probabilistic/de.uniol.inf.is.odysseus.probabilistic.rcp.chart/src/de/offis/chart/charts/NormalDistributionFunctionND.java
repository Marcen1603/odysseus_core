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
package de.offis.chart.charts;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.NonSymmetricMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

public class NormalDistributionFunctionND {

	private final RealMatrix covariances;
	private final double[] means;

	public NormalDistributionFunctionND(final double[] means,
			final RealMatrix covariances) {
		this.covariances = covariances;
		this.means = means;
	}

	public double getValue(final double[] x) {
		final int k = x.length;
		final RealMatrix z = this.covariances;
		double z_det = 0.0;
		RealMatrix z_inverse = null;
		try {
			final CholeskyDecomposition decomposition = new CholeskyDecomposition(
					z);
			z_det = decomposition.getDeterminant();
			z_inverse = decomposition.getSolver().getInverse();
		} catch (NonSymmetricMatrixException
				| NonPositiveDefiniteMatrixException e) {
			final LUDecomposition decomposition = new LUDecomposition(z);
			z_det = decomposition.getDeterminant();
			z_inverse = decomposition.getSolver().getInverse();
		}
		final RealMatrix x_col = MatrixUtils.createColumnRealMatrix(x);
		final RealMatrix means_col = MatrixUtils
				.createColumnRealMatrix(this.means);
		final RealMatrix x_sub_m = x_col.subtract(means_col);

		final double first = 1 / (FastMath.pow(2 * Math.PI, k / 2) * FastMath
				.pow(z_det, 1 / 2));
		final RealMatrix factor1 = x_sub_m.transpose();
		final RealMatrix factor2 = factor1.multiply(z_inverse);
		final RealMatrix factor3 = x_sub_m;
		final RealMatrix f4 = factor2.multiply(factor3);
		final double f5 = f4.getEntry(0, 0);
		final double second = -0.5 * f5;
		return first * FastMath.exp(second);
	}
}
