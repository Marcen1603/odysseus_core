package de.uniol.inf.is.odysseus.probabilistic.math;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MultivariateGaussianDensityFunction implements MultivariateProbabilityDensityFunction {
    /** Mean of this distribution. */
    private final RealVector mean;
    /** Covariance matrix of this distribution. */
    private final RealMatrix covarianceMatrix;

    public MultivariateGaussianDensityFunction(final double[] mean, final double[][] covarianceMatrix) {
        this(MatrixUtils.createRealVector(mean), MatrixUtils.createRealMatrix(covarianceMatrix));
    }

    public MultivariateGaussianDensityFunction(final RealVector mean, final RealMatrix covarianceMatrix) {
        this.mean = mean;
        this.covarianceMatrix = covarianceMatrix;
    }

    @Override
    public double density(final double[] x) {
        return this.density(MatrixUtils.createRealVector(x));
    }

    public double density(final RealVector x) {
        final RealMatrix inverseCovarianceMatrix = (new LUDecomposition(this.covarianceMatrix)).getSolver()
                .getInverse();

        final double det = (new LUDecomposition(this.covarianceMatrix)).getDeterminant();

        final double result = ((1 / (Math.pow(2 * Math.PI, x.getDimension()) / 2)) * Math.pow(det, 0.5))
                * Math.exp(-0.5
                        * inverseCovarianceMatrix.preMultiply(x.subtract(this.mean)).dotProduct(x.subtract(this.mean)));

        return result;
    }

    @Override
    public double cumulativeProbability(final double[] x1, final double[] x2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MultivariateGaussianDensityFunction clone() {
        return new MultivariateGaussianDensityFunction(this.mean, this.covarianceMatrix);
    }

}
