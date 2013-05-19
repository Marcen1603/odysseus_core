package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
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
 * Sweep area to perform the Expectation Maximization (EM) classifier.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class BatchEMTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

	/** The logger. */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BatchEMTISweepArea.class);
	/** Constant value for numeric stability. */
	private static final double NUMERIC_STABILITY_FACTOR = 10E-5;
	/** Random data generator for initialization. */
	private RandomData randomDataGenerator = new RandomDataImpl(new Well19937c());
	/** The number of Gaussian mixtures. */
	private final int mixtures;
	/** The dimension of the Gaussian Mixture Model. */
	private final int dimension;
	/** The weights (w), with \f$ \sum_{i=0}^{|m|} w_{i} = 1 \f$. */
	private final double[] weights;
	/** The means for each mixture. */
	private final RealMatrix[] means;
	/** The covariance matrixes for each mixture. */
	private final RealMatrix[] covarianceMatrices;
	/** The attribute positions. */
	private int[] attributes;

	/**
	 * Creates a new sweep area for Expectation Maximization (EM) classification.
	 * 
	 * @param attributes
	 *            The attribute indexes to perform the classification on
	 * @param mixtures
	 *            The number of mixtures
	 */
	public BatchEMTISweepArea(final int[] attributes, final int mixtures) {
		this.attributes = attributes;
		this.dimension = attributes.length;
		this.mixtures = mixtures;
		this.weights = new double[getMixtures()];
		this.covarianceMatrices = new RealMatrix[getMixtures()];
		this.means = new RealMatrix[getMixtures()];
		try {
			initWeights();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#insert(de.uniol .inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public final void insert(final ProbabilisticTuple<? extends ITimeInterval> s) {
		super.insert(s.restrict(attributes, false));
		if (size() == 1) {
			for (int m = 0; m < getMixtures(); m++) {
				this.means[m] = MatrixUtils.createColumnRealMatrix(new double[getDimension()]);
				this.covarianceMatrices[m] = MatrixUtils.createRealIdentityMatrix(getDimension());
			}
		} else {
			if (isEstimateable()) {
				double[][] data = doExpectation();
				try {
					doMaximisation(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea #insertAll(java.util.List)
	 */
	@Override
	public final void insertAll(final List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
		super.insertAll(toBeInserted);
	}

	/**
	 * Gets the dimension of the Gaussian Mixture Model.
	 * 
	 * @return The dimension
	 */
	public final int getDimension() {
		return dimension;
	}

	/**
	 * Gets the number of mixtures of the Gaussian Mixture Model.
	 * 
	 * @return The number of mixtures
	 */
	public final int getMixtures() {
		return mixtures;
	}

	/**
	 * Gets the weights array for the mixtures of the Gaussian Mixture Model. The sum of mixture (m) weights (w) is 1:
	 * 
	 * \f$\sum_{i=1}^{|m|} w_{i} = 1\f$
	 * 
	 * @return The weights.
	 */
	public final double[] getWeights() {
		return weights;
	}

	/**
	 * Gets the weight for the given mixture. If the mixture index is lower than zero or greater than the number of mixtures the result is 0.
	 * 
	 * @param mixture
	 *            The index of the mixture
	 * @return The weight
	 */
	public final double getWeight(final int mixture) {
		if ((mixture < 0) || (mixture > this.getMixtures())) {
			return 0;
		}
		return weights[mixture];
	}

	/**
	 * Sets the weight of the given mixture.
	 * 
	 * @param mixture
	 *            The index of the mixture
	 * @param weight
	 *            The weight
	 */
	private void setWeight(final int mixture, final double weight) {
		if ((mixture < 0) || (mixture > this.getMixtures())) {
			return;
		}
		this.weights[mixture] = weight;
	}

	/**
	 * Gets the means vectors of all mixtures.
	 * 
	 * @return The means vectors
	 */
	public final RealMatrix[] getMeans() {
		return means;
	}

	/**
	 * 
	 * Gets the mean vector for the given mixture or <code>null</code> if the given mixture does not exist.
	 * 
	 * @param mixture
	 *            The index of the mixture
	 * @return The means vector
	 */
	public final RealMatrix getMean(final int mixture) {
		if ((mixture < 0) || (mixture > this.getMixtures())) {
			return null;
		}
		return means[mixture];
	}

	/**
	 * Sets the means vector for the given mixture.
	 * 
	 * @param mixture
	 *            The index of the mixture
	 * @param mean
	 *            The means vector
	 */
	private void setMean(final int mixture, final RealMatrix mean) {
		if ((mixture < 0) || (mixture > this.getMixtures())) {
			return;
		}
		means[mixture] = mean;
	}

	/**
	 * Gets the covariance matrixes of all mixtures.
	 * 
	 * @return The covariance matrixes
	 */
	public final RealMatrix[] getCovarianceMatrices() {
		return covarianceMatrices;
	}

	/**
	 * Gets the covariance matrix for the given mixture or <code>null</code> if the given mixture does not exist.
	 * 
	 * @param mixture
	 *            The index of the mixture
	 * @return The covariance matrix
	 */
	public final RealMatrix getCovarianceMatrix(final int mixture) {
		if ((mixture < 0) || (mixture > this.getMixtures())) {
			return null;
		}
		return covarianceMatrices[mixture];
	}

	/**
	 * Sets the covariance matrix for the given mixture.
	 * 
	 * @param mixture
	 *            The index of the mixture
	 * @param covarianceMatrix
	 *            The covariance matrix
	 */
	private void setCovarianceMatrix(final int mixture, final RealMatrix covarianceMatrix) {
		if ((mixture < 0) || (mixture > this.getMixtures())) {
			return;
		}
		covarianceMatrices[mixture] = covarianceMatrix;
	}

	/**
	 * Gets the current log likelihood.
	 * 
	 * @return The log likelyhood.
	 */
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

	/**
	 * Do the expectation step of the EM algorithm.
	 * 
	 * @return The current probabilities
	 */
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

	/**
	 * Do the maximization step of the EM algorithm.
	 * 
	 * @param probabilities
	 *            The probabilities
	 */
	private void doMaximisation(final double[][] probabilities) {
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

	/**
	 * Get the row at he given index.
	 * 
	 * @param index
	 *            The index
	 * @return The row
	 */
	private RealMatrix getRowData(final int index) {
		final double[] data = new double[getDimension()];
		for (int i = 0; i < data.length; i++) {
			data[i] = ((Number) this.getElements().get(index).getAttribute(i)).doubleValue();
		}
		return MatrixUtils.createColumnRealMatrix(data);
	}

	/**
	 * Evaluates the given Gaussian Mixture Model at the given data.
	 * 
	 * @param weight
	 *            The weight array
	 * @param data
	 *            The data
	 * @param mean
	 *            The mean vector
	 * @param covariance
	 *            The covariance matrix
	 * @return The probability of the data
	 */
	private double eval(final double weight, final RealMatrix data, final RealMatrix mean, final RealMatrix covariance) {
		if (mean.getRowDimension() != covariance.getColumnDimension()) {
			throw new DimensionMismatchException(mean.getRowDimension(), covariance.getRowDimension());
		}
		if (mean.getRowDimension() != data.getRowDimension()) {
			throw new DimensionMismatchException(mean.getRowDimension(), data.getRowDimension());
		}
		final int modelDimension = mean.getColumnDimension();
		RealMatrix covarianceMatrix = covariance.copy();

		// Try Cholesky decomposition first (faster).
		// Only when the decomposition fails fall back to the more stable LU decomposition
		double determinant;
		RealMatrix inverse;
		try {
			CholeskyDecomposition choleskyDecomposition = new CholeskyDecomposition(covarianceMatrix);
			DecompositionSolver solver = choleskyDecomposition.getSolver();
			// If the matrix is singular add some constant factor for numeric stability.
			if (!solver.isNonSingular()) {
				double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
				Arrays.fill(diagonal, NUMERIC_STABILITY_FACTOR);
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
				Arrays.fill(diagonal, NUMERIC_STABILITY_FACTOR);
				covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
				luDecomposition = new LUDecomposition(covarianceMatrix);
				solver = luDecomposition.getSolver();
			}
			determinant = luDecomposition.getDeterminant();
			inverse = solver.getInverse();
		}
		final double density = 1.0 / (FastMath.pow(2.0 * Math.PI, modelDimension / 2.0) * FastMath.sqrt(determinant));
		final RealMatrix variance = data.subtract(mean);
		return weight * density * FastMath.exp(variance.transpose().multiply(inverse).multiply(variance).scalarMultiply(-1.0 / 2.0).getEntry(0, 0));
	}

	/**
	 * Initialize the weights of the mixtures with random weights.
	 */
	private void initWeights() {
		double sum = 0.0;
		for (int m = 0; m < getMixtures(); m++) {
			this.weights[m] = randomDataGenerator.nextUniform(Double.MIN_NORMAL, 1.0);
			sum += this.weights[m];
		}
		for (int m = 0; m < getMixtures(); m++) {
			this.weights[m] = this.weights[m] / sum;
		}
	}

	/**
	 * Checks if we have enough data to estimate the resulting mixture components. The min. amount of data is:
	 * 
	 * \f$ |m| + (d + (d * (d + 1)) / 2 + 1 \f$
	 * 
	 * @return <code>true</code> iff enough data are collected to estimate the result
	 */
	private boolean isEstimateable() {
		double v = getMixtures() + (getDimension() + (getDimension() * (getDimension() + 1.0)) / 2.0) + 1.0;
		return v < size();
	}
}
