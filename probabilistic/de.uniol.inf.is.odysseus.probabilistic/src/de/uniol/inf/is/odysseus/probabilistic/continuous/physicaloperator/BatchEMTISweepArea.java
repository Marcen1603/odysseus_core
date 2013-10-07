package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
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
	private static final Logger LOG = LoggerFactory.getLogger(BatchEMTISweepArea.class);
	/** Constant value for numeric stability. */
	private final int mixtures;
	/** The dimension of the Gaussian Mixture Model. */
	private final int[] attributes;
	/** The weights (w), with \f$ \sum_{i=0}^{|m|} w_{i} = 1 \f$. */
	private final double threshold;
	/** The means for each mixture. */
	private final int iterations;
	/** The covariance matrixes for each mixture. */
	private MixtureMultivariateNormalDistribution model;

	/**
	 * Creates a new sweep area for Expectation Maximization (EM) classification.
	 * 
	 * @param attributes
	 *            The attribute indexes to perform the classification on
	 * @param mixtures
	 *            The number of mixtures
	 * @param iterations
	 *            The maximum number of iterations allowed per fitting process
	 * @param threshold
	 *            The convergence threshold for fitting
	 */
	public BatchEMTISweepArea(final int[] attributes, final int mixtures, final int iterations, final double threshold) {
		this.attributes = attributes;
		this.mixtures = mixtures;
		this.iterations = iterations;
		this.threshold = threshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#insert(de.uniol .inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public final void insert(final ProbabilisticTuple<? extends ITimeInterval> s) {
		super.insert(s.restrict(this.attributes, false));
		try {
			if (getModel() == null) {
				setModel(MultivariateNormalMixtureExpectationMaximization.estimate(getData(), mixtures));
			}
			MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(getData());
			em.fit(getModel(), getIterations(), getThreshold());
			MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
			if (fittedModel != null) {
				setModel(fittedModel);
		} //else {
		} catch (MaxCountExceededException|ConvergenceException e) {
			setModel(null);
			throw e;
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
	 * Gets the fitted model.
	 * 
	 * @return The model
	 */
	public final MixtureMultivariateNormalDistribution getModel() {
		return this.model;
	}

	/**
	 * Sets the fitted model.
	 * 
	 * @param model
	 *            the model to set
	 */
	private void setModel(final MixtureMultivariateNormalDistribution model) {
		this.model = model;
	}

	/**
	 * Gets the maximum number of iterations allowed per fitting process.
	 * 
	 * @return the iterations
	 */
	public final int getIterations() {
		return this.iterations;
	}

	/**
	 * Gets the convergence threshold for fitting.
	 * 
	 * @return the threshold
	 */
	public final double getThreshold() {
		return this.threshold;
	}

	/**
	 * Gets the dimension of the Gaussian Mixture Model.
	 * 
	 * @return The dimension
	 */
	public final int getDimension() {
		return this.attributes.length;
	}

	/**
	 * Gets the number of mixtures of the Gaussian Mixture Model.
	 * 
	 * @return The number of mixtures
	 */
	public final int getMixtures() {
		return this.mixtures;
	}

	/**
	 * Gets the weights array for the mixtures of the Gaussian Mixture Model. The sum of mixture (m) weights (w) is 1:
	 * 
	 * \f$\sum_{i=1}^{|m|} w_{i} = 1\f$
	 * 
	 * @return The weights.
	 */
	public final double[] getWeights() {
		double[] weights = new double[getDimension()];
		for (int c = 0; c < this.model.getComponents().size(); c++) {
			Pair<Double, MultivariateNormalDistribution> component = this.model.getComponents().get(c);
			weights[c] = component.getFirst();
		}

		return weights;
	}

	/**
	 * Gets the weight for the given mixture. If the mixture index is lower than zero or greater than the number of mixtures the result is 0.
	 * 
	 * @param component
	 *            The index of the mixture
	 * @return The weight
	 */
	public final double getWeight(final int component) {
		if ((component < 0) || (component > this.getMixtures())) {
			return 0;
		}
		return this.model.getComponents().get(component).getFirst();
	}

	/**
	 * Gets the means vectors of all mixtures.
	 * 
	 * @return The means vectors
	 */
	public final RealMatrix[] getMeans() {
		RealMatrix[] means = new RealMatrix[getDimension()];
		for (int c = 0; c < this.model.getComponents().size(); c++) {
			Pair<Double, MultivariateNormalDistribution> component = this.model.getComponents().get(c);
			means[c] = MatrixUtils.createColumnRealMatrix(component.getValue().getMeans());
		}
		return means;
	}

	/**
	 * 
	 * Gets the mean vector for the given mixture or <code>null</code> if the given mixture does not exist.
	 * 
	 * @param component
	 *            The index of the mixture
	 * @return The means vector
	 */
	public final RealMatrix getMean(final int component) {
		if ((component < 0) || (component > this.getMixtures())) {
			return null;
		}
		return MatrixUtils.createColumnRealMatrix(this.model.getComponents().get(component).getValue().getMeans());
	}

	/**
	 * Gets the covariance matrixes of all mixtures.
	 * 
	 * @return The covariance matrixes
	 */
	public final RealMatrix[] getCovarianceMatrices() {
		RealMatrix[] covariances = new RealMatrix[getDimension()];
		for (int c = 0; c < this.model.getComponents().size(); c++) {
			Pair<Double, MultivariateNormalDistribution> component = this.model.getComponents().get(c);
			covariances[c] = component.getValue().getCovariances();
		}
		return covariances;
	}

	/**
	 * Gets the covariance matrix for the given mixture or <code>null</code> if the given mixture does not exist.
	 * 
	 * @param component
	 *            The index of the mixture
	 * @return The covariance matrix
	 */
	public final RealMatrix getCovarianceMatrix(final int component) {
		if ((component < 0) || (component > this.getMixtures())) {
			return null;
		}
		return this.model.getComponents().get(component).getValue().getCovariances();
	}

	/**
	 * Gets the data in the current sweep area.
	 * 
	 * @return The data.
	 */
	private double[][] getData() {
		final double[][] data = new double[this.getElements().size()][this.getDimension()];
		for (int i = 0; i < this.getElements().size(); i++) {
			for (int d = 0; d < this.getDimension(); d++) {
				data[i][d] = ((Number) this.getElements().get(i).getAttribute(d)).doubleValue();
		}
	}
		return data;
	}

	/**
	 * Do the expectation step of the EM algorithm.
	 * 
	 * @return The current probabilities
	 */
	private double[][] doExpectation() {
		final double[][] probabilities = new double[this.size()][this.getMixtures()];
		for (int i = 0; i < this.size(); i++) {
			final double[] density = new double[this.getMixtures()];
			double sum = 0.0;
			for (int m = 0; m < this.getMixtures(); m++) {
				density[m] = this.eval(this.getWeight(m), this.getRowData(i), this.getMean(m), this.getCovarianceMatrix(m));
				sum += density[m];
			}
			for (int m = 0; m < this.getMixtures(); m++) {
				probabilities[i][m] = density[m] / sum;
				if (Double.isNaN(probabilities[i][m])) {
					probabilities[i][m] = 1.0 / this.getMixtures();
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
		for (int m = 0; m < this.getMixtures(); m++) {
			final RealMatrix mean = this.getMean(m);
			RealMatrix sigmaSum = MatrixUtils.createRealMatrix(this.getCovarianceMatrix(m).getRowDimension(), this.getCovarianceMatrix(m).getColumnDimension());
			RealMatrix meanSum = MatrixUtils.createRealMatrix(this.getDimension(), 1);
			double probabilitySum = 0.0;
			for (int i = 0; i < this.size(); i++) {
				final RealMatrix data = this.getRowData(i);
				final RealMatrix variance = data.subtract(mean).multiply(data.subtract(mean).transpose()).scalarMultiply(probabilities[i][m]);
				sigmaSum = sigmaSum.add(variance);
				meanSum = meanSum.add(data.scalarMultiply(probabilities[i][m]));
				probabilitySum += probabilities[i][m];
			}
			this.setWeight(m, probabilitySum / this.size());
			this.setMean(m, meanSum.scalarMultiply(1.0 / probabilitySum));
			this.setCovarianceMatrix(m, sigmaSum.scalarMultiply(1.0 / probabilitySum));
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
		final double[] data = new double[this.getDimension()];
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
				final double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
				Arrays.fill(diagonal, BatchEMTISweepArea.NUMERIC_STABILITY_FACTOR);
				covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
				choleskyDecomposition = new CholeskyDecomposition(covarianceMatrix);
				solver = choleskyDecomposition.getSolver();
			}
			determinant = choleskyDecomposition.getDeterminant();
			inverse = solver.getInverse();
		} catch (final Exception e) {
			LUDecomposition luDecomposition = new LUDecomposition(covarianceMatrix);
			DecompositionSolver solver = luDecomposition.getSolver();
			if (!solver.isNonSingular()) {
				final double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
				Arrays.fill(diagonal, BatchEMTISweepArea.NUMERIC_STABILITY_FACTOR);
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
		for (int m = 0; m < this.getMixtures(); m++) {
			this.weights[m] = this.randomDataGenerator.nextUniform(Double.MIN_NORMAL, 1.0);
			sum += this.weights[m];
		}
		for (int m = 0; m < this.getMixtures(); m++) {
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
		final double v = this.getMixtures() + (this.getDimension() + ((this.getDimension() * (this.getDimension() + 1.0)) / 2.0)) + 1.0;
		return v < this.size();
	}
}
