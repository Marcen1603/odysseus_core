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
 * Incremental learning of Gaussian Mixture Models
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
@SuppressWarnings("unused")
public class IncrementalEMTISweepArea extends JoinTISweepArea<ProbabilisticTuple<? extends ITimeInterval>> {

	private static Logger LOG = LoggerFactory.getLogger(IncrementalEMTISweepArea.class);
	private final RandomData randomDataGenerator = new RandomDataImpl(new Well19937c());
	private int mixtures;
	private final int dimension;
	private final double[] weights;
	private final RealMatrix[] means;
	private final RealMatrix[] covarianceMatrices;
	private final int[] attributes;

	public IncrementalEMTISweepArea(final int[] attributes) {
		this.attributes = attributes;
		this.dimension = attributes.length;
		this.weights = new double[this.getMixtures()];
		this.covarianceMatrices = new RealMatrix[this.getMixtures()];
		this.means = new RealMatrix[this.getMixtures()];
		this.initWeights();
	}

	@Override
	public void insert(final ProbabilisticTuple<? extends ITimeInterval> s) {
		super.insert(s.restrict(this.attributes, false));
		if (this.size() == 1) {
			for (int m = 0; m < this.getMixtures(); m++) {
				this.means[m] = MatrixUtils.createColumnRealMatrix(new double[this.getDimension()]);
				this.covarianceMatrices[m] = MatrixUtils.createRealIdentityMatrix(this.getDimension());
			}
		} else {
			final double[][] data = this.doExpectation();
			this.doMaximization(data);
		}
	}

	@Override
	public void insertAll(final List<ProbabilisticTuple<? extends ITimeInterval>> toBeInserted) {
		super.insertAll(toBeInserted);
	}

	public int getDimension() {
		return this.dimension;
	}

	public int getMixtures() {
		return this.mixtures;
	}

	public double[] getWeights() {
		return this.weights;
	}

	public double getWeight(final int mixture) {
		return this.weights[mixture];
	}

	private void setWeight(final int mixture, final double weight) {
		this.weights[mixture] = weight;
	}

	public RealMatrix[] getMeans() {
		return this.means;
	}

	public RealMatrix getMean(final int mixture) {
		return this.means[mixture];
	}

	private void setMean(final int mixture, final RealMatrix mean) {
		this.means[mixture] = mean;
	}

	public RealMatrix[] getCovarianceMatrices() {
		return this.covarianceMatrices;
	}

	public RealMatrix getCovarianceMatrix(final int mixture) {
		return this.covarianceMatrices[mixture];
	}

	private void setCovarianceMatrix(final int mixture, final RealMatrix covarianceMatrix) {
		this.covarianceMatrices[mixture] = covarianceMatrix;
	}

	private double getLogLikelihood() {
		double loglikelihood = 0.0;
		for (int s = 0; s < this.size(); s++) {
			double sum = 0.0;
			for (int m = 0; m < this.getMixtures(); m++) {
				sum += this.eval(this.getWeight(m), this.getRowData(s), this.getMean(m), this.getCovarianceMatrix(m));
			}
			loglikelihood += FastMath.log(sum);
		}
		return loglikelihood / this.size();
	}

	private double[][] doExpectation() {
		final double[][] probabilities = new double[this.size()][this.getMixtures()];

		return probabilities;
	}

	@SuppressWarnings("null")
	private void doMaximization(final double[][] probabilities) {
		final int k = 1;// ??
		final double lambda = 0.0;
		final RealMatrix globalSigma = null;
		final RealMatrix means = this.getN().power(-1).multiply(this.getM());
		final RealMatrix sigma = this.getN().power(-1).multiply(this.getQ()).subtract(this.getN().power(2).power(-1).multiply(this.getM()).multiply(this.getM().transpose())).add(globalSigma.scalarMultiply(lambda));
		double sum = 0.0;
		for (int n = 0; n < k; n++) {
			sum += this.getN().getEntry(n, 0);
		}
		final RealMatrix weights = this.getN().scalarMultiply(1.0 / sum);
	}

	private RealMatrix getRowData(final int index) {
		final double[] data = new double[this.getDimension()];
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
				final double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
				Arrays.fill(diagonal, 10E-5);
				covarianceMatrix = covarianceMatrix.add(MatrixUtils.createRealDiagonalMatrix(diagonal));
				choleskyDecomposition = new CholeskyDecomposition(covarianceMatrix);
				determinant = choleskyDecomposition.getDeterminant();
			}
			inverse = choleskyDecomposition.getSolver().getInverse();
		} catch (final Exception e) {
			LUDecomposition luDecomposition = new LUDecomposition(covarianceMatrix);
			if (luDecomposition.getSolver().isNonSingular()) {
				determinant = luDecomposition.getDeterminant();
			} else {
				final double[] diagonal = new double[covarianceMatrix.getColumnDimension()];
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
		for (int m = 0; m < this.getMixtures(); m++) {
			this.setWeight(m, this.randomDataGenerator.nextUniform(Double.MIN_NORMAL, 1.0));
		}
		this.scaleWeights();
	}

	private void scaleWeights() {
		double sum = 0.0;
		for (int m = 0; m < this.getMixtures(); m++) {
			sum += this.getWeight(m);
		}
		for (int m = 0; m < this.getMixtures(); m++) {
			this.setWeight(m, this.getWeight(m) / sum);
		}
	}

	private RealMatrix mahalanobisDistance(final RealMatrix x, final RealMatrix mean, final RealMatrix sigma) {
		final RealMatrix distance = x.subtract(mean);
		final RealMatrix distanceTranspose = distance.transpose();
		DecompositionSolver solver;
		try {
			solver = new CholeskyDecomposition(sigma).getSolver();
		} catch (final Exception e) {
			solver = new LUDecomposition(sigma).getSolver();
		}
		final RealMatrix inverse = solver.getInverse();
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
	private double getLogLikelihood(final RealMatrix data, final RealMatrix weights, final RealMatrix means[], final RealMatrix covariances[]) {
		double loglikelihood = 0.0;
		for (int s = 0; s < data.getColumnDimension(); s++) {
			double sum = 0.0;
			for (int m = 0; m < weights.getColumnDimension(); m++) {
				sum += this.eval(weights.getEntry(0, m), data.getColumnMatrix(s), means[m], covariances[m]);
			}
			loglikelihood += FastMath.log(sum);
		}
		return loglikelihood / this.size();
	}

	/**
	 * 
	 * @return The k sums of points
	 */
	private RealMatrix getM() {
		final int k = 1;// ??
		final int d = 1;// ??
		return MatrixUtils.createRealMatrix(d, k);
	}

	/**
	 * 
	 * @return The k sums of square points
	 */
	private RealMatrix getQ() {
		final int k = 1;// ??
		final int d = 1;// ??
		return MatrixUtils.createRealMatrix(d, k);
	}

	/**
	 * 
	 * @return The number of points per component.
	 */
	private RealMatrix getN() {
		final int k = 1;// ??
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
	private double getBayesianInformationCriterion(final RealMatrix data, final RealMatrix weights, final RealMatrix[] means, final RealMatrix[] covariances) {
		double bic = 0.0;
		final int components = weights.getColumnDimension();
		final int size = data.getColumnDimension();
		final int dimension = data.getRowDimension();
		final double v = ((int) ((components * (dimension + 1.0) * (dimension + 2.0)) / 2.0)) - 1.0;
		bic = (-2 * FastMath.log(this.getLogLikelihood(data, weights, means, covariances))) + (v * FastMath.log(size));
		return bic;
	}
}
