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

/**
 * Utility class for covariance handling.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public final class CovarianceMatrixUtils {
    /** Constant number used for inverse sum. */
    private static final double INVERSE_SUM = 8.0;

    /**
     * Converts the given covariance matrix into a {@link RealMatrix}.
     * 
     * @param entries
     *            The triangle covariance matrix
     * @return The matrix
     */
    public static RealMatrix toMatrix(final double[] entries) {
        final int size = CovarianceMatrixUtils.getCovarianceDimensionFromTriangleSize(entries.length);
        final RealMatrix matrix = MatrixUtils.createRealMatrix(size, size);
        int left = 0;
        int right = size;
        for (int i = 0; i < size; i++) {
            final double[] row = matrix.getRow(i);
            System.arraycopy(entries, left, row, i, right);
            matrix.setRow(i, row);
            left += right;
            right--;
        }
        left = 0;
        right = size;
        for (int i = 0; i < size; i++) {
            final double[] column = matrix.getColumn(i);
            System.arraycopy(entries, left, column, i, right);
            matrix.setColumn(i, column);
            left += right;
            right--;
        }
        return matrix;
    }

    /**
     * Converts the given matrix into a triangle covariance matrix.
     * 
     * @param matrix
     *            The matrix
     * @return The triangle covariance matrix
     */
    public static double[] fromMatrix(final RealMatrix matrix) {
        final int dimension = matrix.getColumnDimension();
        int left = 0;
        int right = dimension;
        final double[] entries = new double[CovarianceMatrixUtils.getCovarianceTriangleSizeFromDimension(dimension)];
        for (int i = 0; i < dimension; i++) {
            final double[] row = matrix.getRow(i);
            System.arraycopy(row, i, entries, left, right);
            left += right;
            right--;
        }
        return entries;
    }

    /**
     * Returns the number of elements of the triangle of a covariance matrix
     * with the given dimension.
     * 
     * @param dimension
     *            The dimension
     * @return The number of elements in the triangle
     */
    public static int getCovarianceTriangleSizeFromDimension(final int dimension) {
        return (int) ((1.0 / 2.0) * dimension * (dimension + 1.0));
    }

    /**
     * Return the dimension for a covariance matrix build up with the given
     * triangle.
     * 
     * @param triangleSize
     *            The number of elements in the triangle
     * @return The dimension of the covariance matrix
     */
    public static int getCovarianceDimensionFromTriangleSize(final int triangleSize) {
        return (int) ((1.0 / 2.0) * (Math.sqrt((CovarianceMatrixUtils.INVERSE_SUM * triangleSize) + 1.0) - 1.0));
    }

    /**
     * Utility constructor.
     */
    private CovarianceMatrixUtils() {
        throw new UnsupportedOperationException();
    }

}
