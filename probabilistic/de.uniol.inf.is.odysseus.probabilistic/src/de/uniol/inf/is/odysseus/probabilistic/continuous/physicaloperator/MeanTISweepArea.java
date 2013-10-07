package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.List;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * Sweep area to perform the Expectation Maximization (EM) classifier.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MeanTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> implements Cloneable {

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MeanTISweepArea.class);
	/** The number of Gaussian mixtures. */
	private final int mixtures;
	/** The attribute positions. */
	private final int[] attributes;
	/** The convergence threshold for fitting. */
	private final double threshold;
	/** The maximum number of iterations allowed per fitting process. */
	private final int iterations;
	/** Incremental model fitting. */
	private boolean incremental = false;
	/** The predicate for model fitting. */
	private IPredicate<IStreamObject<?>> predicate;
	/** The fitted model. */
	private MixtureMultivariateNormalDistribution model;
	/** Using adaptive mode and model has to be normalized. */
	private boolean normalizeModel = false;

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
	 * @param incremental
	 *            Flag indicating incremental fitting
	 * @param predicate
	 *            The predicate for model fitting
	 */
	public MeanTISweepArea(final int[] attributes, final int mixtures, final int iterations, final double threshold, final boolean incremental, final IPredicate<IStreamObject<?>> predicate) {
		this.attributes = attributes;
		this.mixtures = mixtures;
		this.iterations = iterations;
		this.threshold = threshold;
		this.incremental = incremental;
		if (predicate != null) {
			this.predicate = predicate.clone();
			this.predicate.init();
		}
	}

	/**
	 * Class constructor.
	 * 
	 * @param batchEMTISweepArea
	 *            The clone
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public MeanTISweepArea(final MeanTISweepArea batchEMTISweepArea) throws InstantiationException, IllegalAccessException {
		super(batchEMTISweepArea);
		this.attributes = batchEMTISweepArea.attributes;
		this.mixtures = batchEMTISweepArea.mixtures;
		this.iterations = batchEMTISweepArea.iterations;
		this.threshold = batchEMTISweepArea.threshold;
		this.incremental = batchEMTISweepArea.incremental;
		if (batchEMTISweepArea.predicate != null) {
			this.predicate = batchEMTISweepArea.predicate.clone();
			this.predicate.init();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.intervalapproach.JoinTISweepArea#insert(de.uniol .inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public final void insert(final ProbabilisticTuple<? extends ITimeInterval> s) {
		ProbabilisticTuple<? extends ITimeInterval> restricted = s.restrict(this.attributes, true);
		super.insert(restricted);
		if (getModel() != null) {
			setModel(estimateModel(getModel()));
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
	public void setModel(final MixtureMultivariateNormalDistribution model) {
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
	 * Gets the model fitting predicate.
	 * 
	 * @return the predicate
	 */
	public final IPredicate<?> getPredicate() {
		return this.predicate;
	}

	/**
	 * @return the incremental
	 */
	public final boolean isIncremental() {
		return this.incremental;
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
	 * {@inheritDoc}
	 */
	@Override
	public final MeanTISweepArea clone() {
		try {
			return new MeanTISweepArea(this);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	 * Gets the average of each dimension.
	 * 
	 * @return The average of each dimension
	 */
	private double[] getAvg() {
		double[] avg = new double[this.getDimension()];
		for (int i = 0; i < this.getElements().size(); i++) {
			for (int d = 0; d < this.getDimension(); d++) {
				avg[d] += ((Number) this.getElements().get(i).getAttribute(d)).doubleValue();
			}
		}
		for (int d = 0; d < this.getDimension(); d++) {
			avg[d] /= this.getElements().size();
		}
		return avg;
	}

	private MixtureMultivariateNormalDistribution estimateModel(MixtureMultivariateNormalDistribution initialMixture) {
		double[][] data = getData();
		final int n = data.length;
		if (data.length < 1) {
			throw new NotStrictlyPositiveException(data.length);
		}
		// Number of data columns. Jagged data already rejected in constructor,
		// so we can assume the lengths of each row are equal.
		final int numCols = data[0].length;
		final int k = initialMixture.getComponents().size();

		final int numMeanColumns = initialMixture.getComponents().get(0).getSecond().getMeans().length;

		int numIterations = 0;
		double previousLogLikelihood = 0d;

		double logLikelihood = Double.NEGATIVE_INFINITY;

		// Initialize model to fit to initial mixture.
		MixtureMultivariateNormalDistribution fittedModel = new MixtureMultivariateNormalDistribution(initialMixture.getComponents());

		while (numIterations++ <= getIterations() && Math.abs(previousLogLikelihood - logLikelihood) > threshold) {
			previousLogLikelihood = logLikelihood;
			double sumLogLikelihood = 0d;

			// Mixture components
			final List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();

			// Weight and distribution of each component
			final double[] weights = new double[k];

			final MultivariateNormalDistribution[] mvns = new MultivariateNormalDistribution[k];

			for (int j = 0; j < k; j++) {
				weights[j] = components.get(j).getFirst();
				mvns[j] = components.get(j).getSecond();
			}

			// E-step: compute the data dependent parameters of the expectation
			// function.
			// The percentage of row's total density between a row and a
			// component
			final double[][] gamma = new double[n][k];

			// Sum of gamma for each component
			final double[] gammaSums = new double[k];

			// Sum of gamma times its row for each each component
			final double[][] gammaDataProdSums = new double[k][numCols];

			for (int i = 0; i < n; i++) {
				final double rowDensity = fittedModel.density(data[i]);
				sumLogLikelihood += Math.log(rowDensity);

				for (int j = 0; j < k; j++) {
					gamma[i][j] = weights[j] * mvns[j].density(data[i]) / rowDensity;
					gammaSums[j] += gamma[i][j];

					for (int col = 0; col < numCols; col++) {
						gammaDataProdSums[j][col] += gamma[i][j] * data[i][col];
					}
				}
			}

			logLikelihood = sumLogLikelihood / n;

			// M-step: compute the new parameters based on the expectation
			// function.
			final double[] newWeights = new double[k];
			final double[][] newMeans = new double[k][numCols];

			for (int j = 0; j < k; j++) {
				newWeights[j] = components.get(j).getKey();
				for (int col = 0; col < numCols; col++) {
					newMeans[j][col] = gammaDataProdSums[j][col] / gammaSums[j];
				}
			}

			// Compute new covariance matrices
			final double[][][] newCovMatArrays = new double[k][numCols][numCols];
			for (int j = 0; j < k; j++) {
				newCovMatArrays[j] = components.get(j).getValue().getCovariances().getData();
			}

			// Update current model
			fittedModel = new MixtureMultivariateNormalDistribution(newWeights, newMeans, newCovMatArrays);
		}
		return fittedModel;
	}

}
