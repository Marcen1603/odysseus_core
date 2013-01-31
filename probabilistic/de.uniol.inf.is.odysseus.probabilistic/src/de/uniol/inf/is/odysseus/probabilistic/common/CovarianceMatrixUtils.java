/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public final class CovarianceMatrixUtils {

	public static RealMatrix toMatrix(CovarianceMatrix covarianceTriangleMatrix) {
		int size = covarianceTriangleMatrix.size();
		final RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(size,
				size);
		int left = 0;
		int right = size;
		for (int i = 0; i < size; i++) {
			final double[] row = covarianceMatrix.getRow(i);
			System.arraycopy(covarianceTriangleMatrix.getEntries(), left, row,
					i, right);
			covarianceMatrix.setRow(i, row);
			left += right;
			right--;
		}
		return covarianceMatrix;
	}

	public static CovarianceMatrix fromMatrix(RealMatrix matrix) {
		int dimension = matrix.getColumnDimension();
		int left = 0;
		int right = dimension;
		double[] entries = new double[CovarianceMatrixUtils.getCovarianceTiangleSizeFromDimension(dimension)];
		for (int i = 0; i < dimension; i++) {
			final double[] row = matrix.getRow(i);
			System.arraycopy(row, i, entries, left, right);
			left += right;
			right--;
		}
		CovarianceMatrix covarianceMatrix = new CovarianceMatrix(entries);
		return covarianceMatrix;
	}

	/**
	 * Returns the number of elements of the triangle of a covariance matrix
	 * with the given dimension
	 * 
	 * @param dimension
	 *            The dimension
	 * @return The number of elements in the triangle
	 */
	public static int getCovarianceTiangleSizeFromDimension(int dimension) {
		return (int) (0.5 * dimension * (dimension + 1));
	}

	/**
	 * Return the dimension for a covariance matrix build up with the given
	 * triangle
	 * 
	 * @param triangleSize
	 *            The number of elements in the triangle
	 * @return The dimension of the covariance matrix
	 */
	public static int getCovarianceDimensionFromTriangleSize(int triangleSize) {
		return (int) (0.5 * (Math.sqrt(8 * triangleSize + 1) - 1));
	}

	private CovarianceMatrixUtils() {
	}
}
