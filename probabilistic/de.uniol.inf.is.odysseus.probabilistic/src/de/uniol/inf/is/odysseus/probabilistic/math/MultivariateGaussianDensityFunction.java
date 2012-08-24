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

    public MultivariateGaussianDensityFunction(final double[] mean, final double[] covarianceTriangleMatrix) {
        final int size = (int) (-0.5 + Math.sqrt(0.25 + (covarianceTriangleMatrix.length * 2)));
        final RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(size, size);
        int left = 0;
        int right = size;
        for (int i = 0; i < size; i++) {
            final double[] row = covarianceMatrix.getRow(i);
            System.arraycopy(covarianceTriangleMatrix, left, row, i, right);
            row[i] /= 2;
            covarianceMatrix.setRow(i, row);
            left += right;
            right--;
        }
        this.mean = MatrixUtils.createRealVector(mean);
        this.covarianceMatrix = covarianceMatrix.add(covarianceMatrix.transpose());
    }

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
        throw new RuntimeException("Not implemented");
    }

    @Override
    public MultivariateGaussianDensityFunction clone() {
        return new MultivariateGaussianDensityFunction(this.mean, this.covarianceMatrix.copy());
    }

    @Override
    public String toString() {
        return this.mean.toString() + " " + this.covarianceMatrix.toString();
    }

    public static void main(final String[] args) {
        final MultivariateGaussianDensityFunction test = new MultivariateGaussianDensityFunction(new double[] { 1.0,
                2.0 }, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 });
        System.out.println(test);
    }
}
