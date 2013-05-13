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
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class BatchEMTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(BatchEMTISweepArea.class);
    private RandomData randomDataGenerator = new RandomDataImpl(new Well19937c());
    private final int mixtures;
    private final int dimension;
    private final double[] weights;
    private final RealMatrix[] means;
    private final RealMatrix[] covarianceMatrices;
    private int[] attributes;

    public BatchEMTISweepArea(int[] attributes, int mixtures) {
        this.attributes = attributes;
        this.dimension = attributes.length;
        this.mixtures = mixtures;
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
            if (isEstimateable()) {
                double[][] data = doExpectation();
                doMaximization(data);
            }
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
        for (int i = 0; i < this.size(); i++) {
            final double[] density = new double[getMixtures()];
            double sum = 0.0;
            for (int m = 0; m < getMixtures(); m++) {
                density[m] = this.eval(getWeight(m), this.getRowData(i), getMean(m), getCovarianceMatrix(m));
                sum += density[m];
            }
            for (int m = 0; m < getMixtures(); m++) {
                probabilities[i][m] = density[m] / sum;
                if (Double.isNaN(probabilities[i][m])) {
                    probabilities[i][m] = 1.0 / (double) getMixtures();
                }
            }
        }
        return probabilities;
    }

    private void doMaximization(final double[][] probabilities) {
        for (int m = 0; m < getMixtures(); m++) {
            RealMatrix mean = getMean(m);
            RealMatrix sigmaSum = MatrixUtils.createRealMatrix(getCovarianceMatrix(m).getRowDimension(), getCovarianceMatrix(m).getColumnDimension());
            RealMatrix meanSum = MatrixUtils.createRealMatrix(getDimension(), 1);
            double probabilitySum = 0.0;
            for (int i = 0; i < this.size(); i++) {
                RealMatrix data = getRowData(i);
                RealMatrix variance = data.subtract(mean).multiply(data.subtract(mean).transpose()).scalarMultiply(probabilities[i][m]);
                sigmaSum = sigmaSum.add(variance);
                meanSum = meanSum.add(data.scalarMultiply(probabilities[i][m]));
                probabilitySum += probabilities[i][m];
            }
            setWeight(m, probabilitySum / (double) size());
            setMean(m, meanSum.scalarMultiply(1.0 / probabilitySum));
            setCovarianceMatrix(m, sigmaSum.scalarMultiply(1.0 / probabilitySum));
        }
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
            DecompositionSolver solver = choleskyDecomposition.getSolver();
            if (!solver.isNonSingular()) {
                double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
                Arrays.fill(diagonal, 10E-5);
                covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
                choleskyDecomposition = new CholeskyDecomposition(covarianceMatrix);
                solver = choleskyDecomposition.getSolver();
            }
            determinant = choleskyDecomposition.getDeterminant();
            inverse = solver.getInverse();
        } catch (Exception e) {
            LUDecomposition luDecomposition = new LUDecomposition(covarianceMatrix);
            DecompositionSolver solver = luDecomposition.getSolver();
            if (!solver.isNonSingular()) {
                double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
                Arrays.fill(diagonal, 10E-5);
                covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
                luDecomposition = new LUDecomposition(covarianceMatrix);
                solver = luDecomposition.getSolver();
            }
            determinant = luDecomposition.getDeterminant();
            inverse = solver.getInverse();
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

    private boolean isEstimateable() {
        double v = getMixtures() + (getDimension() + (getDimension() * (getDimension() + 1.0)) / 2.0) + 1.0;
        return v < size();
    }
}
