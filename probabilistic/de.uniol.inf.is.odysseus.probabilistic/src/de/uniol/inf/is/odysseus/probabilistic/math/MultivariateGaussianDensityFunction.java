package de.uniol.inf.is.odysseus.probabilistic.math;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MultivariateGaussianDensityFunction implements
		MultivariateProbabilityDensityFunction {
	/** Mean of this distribution. */
	private RealVector mean;
	/** Covariance matrix of this distribution. */
	private RealMatrix covarianceMatrix;

	public MultivariateGaussianDensityFunction(double[] mean,
			double[][] covarianceMatrix) {
		this(MatrixUtils.createRealVector(mean), MatrixUtils
				.createRealMatrix(covarianceMatrix));
	}

	public MultivariateGaussianDensityFunction(RealVector mean,
			RealMatrix covarianceMatrix) {
		this.mean = mean;
		this.covarianceMatrix = covarianceMatrix;
	}

	@Override
	public double density(double[] x) {
		return density(MatrixUtils.createRealVector(x));
	}

	public double density(RealVector x) {
		RealMatrix inverseCovarianceMatrix = (new LUDecomposition(
				covarianceMatrix)).getSolver().getInverse();

		double det = (new LUDecomposition(covarianceMatrix)).getDeterminant();

		double result = (1 / (Math.pow(2 * Math.PI, x.getDimension()) / 2) * Math
				.pow(det, 0.5))
				* Math.exp(-0.5
						* inverseCovarianceMatrix.preMultiply(x.subtract(mean))
								.dotProduct(x.subtract(mean)));

		return result;
	}

	@Override
	public double cumulativeProbability(double[] x1, double[] x2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MultivariateGaussianDensityFunction clone() {
		return new MultivariateGaussianDensityFunction(mean, covarianceMatrix);
	}

}
