package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomData;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * Incremental learning of Gaussian Mixture Models
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IncrementalEMTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(IncrementalEMTISweepArea.class);
    private RandomData randomDataGenerator = new RandomDataImpl(new Well19937c());
    private int mixtures;
    private final int dimension;
    private final double[] weights;
    private final RealMatrix[] means;
    private final RealMatrix[] covarianceMatrices;
    private int[] attributes;

    public IncrementalEMTISweepArea(int[] attributes) {
        this.attributes = attributes;
        this.dimension = attributes.length;
        this.weights = new double[getMixtures()];
        this.covarianceMatrices = new RealMatrix[getMixtures()];
        this.means = new RealMatrix[getMixtures()];
        initWeights();
    }

    @Override
    public void insert(ProbabilisticTuple<? extends ITimeInterval> s) {
        super.insert(s.restrict(attributes, false));
        if (size() == 1) {
            for (int m = 0; m < getMixtures(); m++) {
                this.means[m] = MatrixUtils.createColumnRealMatrix(new double[getDimension()]);
                this.covarianceMatrices[m] = MatrixUtils.createRealIdentityMatrix(getDimension());
            }
        } else {
            double[][] data = doExpectation();
            doMaximization(data);
        }
    }

    @Override
    public void insertAll(List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
        super.insertAll(toBeInserted);
    }

    public int getDimension() {
        return dimension;
    }

    public int getMixtures() {
        return mixtures;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getWeight(int mixture) {
        return weights[mixture];
    }

    private void setWeight(int mixture, double weight) {
        this.weights[mixture] = weight;
    }

    public RealMatrix[] getMeans() {
        return means;
    }

    public RealMatrix getMean(int mixture) {
        return means[mixture];
    }

    private void setMean(int mixture, RealMatrix mean) {
        means[mixture] = mean;
    }

    public RealMatrix[] getCovarianceMatrices() {
        return covarianceMatrices;
    }

    public RealMatrix getCovarianceMatrix(int mixture) {
        return covarianceMatrices[mixture];
    }

    private void setCovarianceMatrix(int mixture, RealMatrix covarianceMatrix) {
        covarianceMatrices[mixture] = covarianceMatrix;
    }

    @SuppressWarnings("unused")
    private double getLogLikelihood() {
        double loglikelihood = 0.0;
        for (int s = 0; s < size(); s++) {
            double sum = 0.0;
            for (int m = 0; m < getMixtures(); m++) {
                sum += this.eval(getWeight(m), this.getRowData(s), getMean(m), getCovarianceMatrix(m));
            }
            loglikelihood += FastMath.log(sum);
        }
        return loglikelihood / (double) size();
    }

    private double[][] doExpectation() {
        final double[][] probabilities = new double[this.size()][getMixtures()];

        return probabilities;
    }

    private void doMaximization(final double[][] probabilities) {
        int k = 1;// ??
        double lambda = 0.0;
        RealMatrix globalSigma = null;
        RealMatrix means = getN().power(-1).multiply(getM());
        RealMatrix sigma = getN().power(-1).multiply(getQ()).subtract(getN().power(2).power(-1).multiply(getM()).multiply(getM().transpose())).add(globalSigma.scalarMultiply(lambda));
        double sum = 0.0;
        for (int n = 0; n < k; n++) {
            sum += getN().getEntry(n, 0);
        }
        RealMatrix weights = getN().scalarMultiply(1.0 / sum);
    }

    private RealMatrix getRowData(final int index) {
        final double[] data = new double[getDimension()];
        for (int i = 0; i < data.length; i++) {
            data[i] = ((Number) this.getElements().get(index).getAttribute(i)).doubleValue();
        }
        return MatrixUtils.createColumnRealMatrix(data);
    }

    private double eval(final double weight, final RealMatrix data, final RealMatrix mean, RealMatrix covarianceMatrix) {
        final int dimension = mean.getColumnDimension();
        double determinant;
        RealMatrix inverse;
        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(covarianceMatrix);
            if (choleskyDecomposition.getSolver().isNonSingular()) {
                // FIXME use 2*sum(log(diag(A)))
                determinant = choleskyDecomposition.getDeterminant();
            } else {
                double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
                Arrays.fill(diagonal, 10E-5);
                covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
                choleskyDecomposition = new CholeskyDecomposition(covarianceMatrix);
                determinant = choleskyDecomposition.getDeterminant();
            }
            inverse = choleskyDecomposition.getSolver().getInverse();
        } catch (Exception e) {
            LUDecomposition luDecomposition = new LUDecomposition(covarianceMatrix);
            if (luDecomposition.getSolver().isNonSingular()) {
                determinant = luDecomposition.getDeterminant();
            } else {
                double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
                Arrays.fill(diagonal, 10E-5);
                covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
                luDecomposition = new LUDecomposition(covarianceMatrix);
                determinant = luDecomposition.getDeterminant();
            }
            inverse = luDecomposition.getSolver().getInverse();
        }
        final double density = 1.0 / (FastMath.pow(2. * Math.PI, dimension / 2.) * FastMath.sqrt(determinant));
        final RealMatrix variance = data.subtract(mean);
        return weight * density * FastMath.exp(variance.transpose().multiply(inverse).multiply(variance).scalarMultiply(-0.5).getEntry(0, 0));
    }

    private void initWeights() {
        for (int m = 0; m < getMixtures(); m++) {
            setWeight(m, randomDataGenerator.nextUniform(Double.MIN_NORMAL, 1.0));
        }
        scaleWeights();
    }

    private void scaleWeights() {
        double sum = 0.0;
        for (int m = 0; m < getMixtures(); m++) {
            sum += getWeight(m);
        }
        for (int m = 0; m < getMixtures(); m++) {
            setWeight(m, getWeight(m) / sum);
        }
    }

    private RealMatrix mahalanobisDistance(RealMatrix x, RealMatrix mean, RealMatrix sigma) {
        RealMatrix distance = x.subtract(mean);
        RealMatrix distanceTranspose = distance.transpose();
        DecompositionSolver solver;
        try {
            solver = new CholeskyDecomposition(sigma).getSolver();
        } catch (Exception e) {
            solver = new LUDecomposition(sigma).getSolver();
        }
        RealMatrix inverse = solver.getInverse();
        return distanceTranspose.multiply(inverse).multiply(distance);

    }

    /**
     * 
     * @param data
     * @param weights
     * @param means
     * @param covariances
     * @return
     */
    private double getLogLikelihood(RealMatrix data, RealMatrix weights, RealMatrix means[], RealMatrix covariances[]) {
        double loglikelihood = 0.0;
        for (int s = 0; s < data.getColumnDimension(); s++) {
            double sum = 0.0;
            for (int m = 0; m < weights.getColumnDimension(); m++) {
                sum += this.eval(weights.getEntry(0, m), data.getColumnMatrix(s), means[m], covariances[m]);
            }
            loglikelihood += FastMath.log(sum);
        }
        return loglikelihood / (double) size();
    }

    /**
     * 
     * @return The k sums of points
     */
    private RealMatrix getM() {
        int k = 1;// ??
        int d = 1;// ??
        return MatrixUtils.createRealMatrix(d, k);
    }

    /**
     * 
     * @return The k sums of square points
     */
    private RealMatrix getQ() {
        int k = 1;// ??
        int d = 1;// ??
        return MatrixUtils.createRealMatrix(d, k);
    }

    /**
     * 
     * @return The number of points per component.
     */
    private RealMatrix getN() {
        int k = 1;// ??
        return MatrixUtils.createRealMatrix(k, 1);
    }

    /**
     * 
     * @param data
     * @param weights
     * @param means
     * @param covariances
     * @return
     */
    private double getBayesianInformationCriterion(RealMatrix data, RealMatrix weights, RealMatrix[] means, RealMatrix[] covariances) {
        double bic = 0.0;
        int components = weights.getColumnDimension();
        int size = data.getColumnDimension();
        int dimension = data.getRowDimension();
        double v = ((int) (components * (dimension + 1.0) * (dimension + 2.0) / 2.0)) - 1.0;
        bic = -2 * FastMath.log(getLogLikelihood(data, weights, means, covariances)) + v * FastMath.log(size);
        return bic;
    }
}
