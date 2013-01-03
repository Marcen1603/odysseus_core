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
		int size = matrix.getColumnDimension();
		int left = 0;
		int right = size;
		double[] entries = new double[size];
		for (int i = 0; i < size; i++) {
			final double[] row = matrix.getRow(i);
			System.arraycopy(row, i, entries, left, right);
			left += right;
			right--;
		}
		CovarianceMatrix covarianceMatrix = new CovarianceMatrix(entries);
		return covarianceMatrix;
	}

	private CovarianceMatrixUtils() {
	}
}
