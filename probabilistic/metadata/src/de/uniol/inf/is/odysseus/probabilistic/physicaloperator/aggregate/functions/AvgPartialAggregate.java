package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * 
 * AVG Aggregation on probabilistic data stream according to T.S.Jayram et al.
 * "Estimating statistical aggregates on probabilistic data streams"
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AvgPartialAggregate<T> implements IPartialAggregate<T> {

	/** Upper probability bound */
	private final double theta;
	/** Approximation error < 1/2 */
	private final double epsilon;

	/** Storage for values */
	private final double[] valueStore;
	/** Storage for probabilities */
	private final double[] probabilityStore;

	/** Upper bound of COUNT */
	private double[] countUpperBound;
	/** Lower bound of COUNT */
	private double[] countLowerBound;
	/** Upper bound of SUM */
	private double[] sumUpperBound;
	/** Lower bound of SUM */
	private double[] sumLowerBound;
	/** Index of the current store */
	private int storeIndex;

	/** Constant factors */
	private final int k0;
	private final int k1;

	private double alpha;

	/**
	 * 
	 * @param epsilon
	 *            Approximation error
	 * @param theta
	 *            Upper probability bound
	 */
	public AvgPartialAggregate(double epsilon, double theta) {
		this.epsilon = epsilon;
		this.theta = theta;
		this.k0 = (int) (2 * Math.log(2 / this.epsilon) / Math
				.log(1 / this.theta));
		this.k1 = (int) (Math.log(2 / this.epsilon) / Math.log(1 / this.theta));
		int storeSize = (int) ((3 / Math.pow(theta, 2)) * Math.log(2 / epsilon) + 0.5);
		this.valueStore = new double[storeSize];
		this.probabilityStore = new double[storeSize];

		this.countUpperBound = new double[k0];
		this.countLowerBound = new double[k0];
		this.sumUpperBound = new double[k1];
		this.sumLowerBound = new double[k1];

		this.storeIndex = 0;

	}

	/**
	 * 
	 * @param avgPartialAggregate
	 */
	public AvgPartialAggregate(AvgPartialAggregate<T> avgPartialAggregate) {
		this.epsilon = avgPartialAggregate.epsilon;
		this.theta = avgPartialAggregate.theta;
		this.k0 = avgPartialAggregate.k0;
		this.k1 = avgPartialAggregate.k1;
		this.valueStore = avgPartialAggregate.valueStore.clone();
		this.probabilityStore = avgPartialAggregate.probabilityStore.clone();
		this.storeIndex = avgPartialAggregate.storeIndex;
		this.countLowerBound = avgPartialAggregate.countLowerBound.clone();
		this.countUpperBound = avgPartialAggregate.countUpperBound.clone();
		this.sumLowerBound = avgPartialAggregate.sumLowerBound.clone();
		this.sumUpperBound = avgPartialAggregate.sumUpperBound.clone();

	}

	public void update(double value, double probability) {
		this.alpha *= (1.0 - probability);
		for (int k = 1; k <= this.k0; k++) {
			this.countUpperBound[k - 1] += Math.pow(probability, k);
		}
		for (int k = 1; k <= this.k1; k++) {
			this.sumUpperBound[k - 1] += value * Math.pow(probability, k);
		}
		if (this.countUpperBound[1 - 1] < (3 / this.theta)
				* Math.log(2 / this.epsilon)) {
			if (probability > this.theta) {
				this.storeIndex++;
				this.valueStore[storeIndex - 1] = value;
				this.probabilityStore[storeIndex - 1] = probability;
			} else {
				for (int k = 1; k <= this.k0; k++) {
					this.countLowerBound[k - 1] += Math.pow(probability, k);
				}
				for (int k = 1; k <= this.k1; k++) {
					this.sumLowerBound[k - 1] += value
							* Math.pow(probability, k);
				}
			}
		}
	}

	public double reconstruct() {
		double rho = 1.0 / (1.0 - this.alpha);
		double z0 = getZ0(this.countUpperBound[1 - 1]);
		double smallestEvenInteger = getSmallestEvenInteger(this.countUpperBound[1 - 1]);
		if (isLongStream(this.countUpperBound[1 - 1])) {
			// Large Stream use SUM/COUNT
			return evaluateLongStreamAvg(rho);
		} else {
			// Short Stream
			return evaluateShortStreamAvg(rho, z0, smallestEvenInteger);
		}
	}

	/**
	 * 
	 * @param count
	 * @return true if this is a long stream, false if this is a short stream
	 */
	private boolean isLongStream(double count) {
		return (count >= 4.0 / this.epsilon * Math.log(2.0 / this.epsilon));
	}

	/**
	 * Evaluate AVG on long streams using the upper bound of SUM and COUNT
	 * 
	 * @param rho
	 * @return
	 */
	private double evaluateLongStreamAvg(double rho) {
		return rho * this.sumUpperBound[1 - 1] / this.countUpperBound[1 - 1];
	}

	/**
	 * Evaluate AVG on short streams
	 * 
	 * @param rho
	 * @return
	 */
	private double evaluateShortStreamAvg(double rho, double z0, double l) {
		double Q;
		double estimate = 0.0;
		for (int i = 0; i <= k0 ; i++) {
			double z = i;
			if (countUpperBound[1 - 1] >= 3.0 / theta * Math.log(2.0 / epsilon)) {
				Q = getQBig(z, rho, l);
			} else {
				Q = getQSmall(z, rho, l);
			}
			estimate += Q * Math.pow(z0, i + 1.0) / (i + 1.0);
		}
		return estimate;
	}

	/**
	 * 
	 * @param count
	 * @return the smallest even integer greater than or equal to
	 *         5ln(2P/epsilon)
	 */
	private int getSmallestEvenInteger(double count) {
		if (count >= 1) {
			return (int) (2.0 * Math.ceil((5.0 / 2.0)
					* Math.log(2.0 * count / epsilon)));
		} else {
			return (int) (2.0 * Math.ceil(Math.max(
					1.0 / 2.0 * Math.log(2.0 / epsilon), 7.0 / 2.0)));
		}
	}

	/**
	 * 
	 * @param rho
	 * @param count
	 * @return
	 */
	private double getZ0(double count) {
		if (count >= 1.0) {
			return Math.min(1.0, 1.0 / count * Math.log(2.0 * count / epsilon));
		} else {
			return 1.0;
		}
	}

	private double getQBig(double z, double p, double l) {
		double result;
		double product = 1.0;
		double sum = 0.0;
		for (int i = 1; i <= k0; i++) {

			sum = 0.0;
			for (int j = 0; j <= l; j++) {
				sum += Math.pow((-countUpperBound[i - 1] * Math.pow(z, i)), j)
						/ (factorial(j) * Math.pow(i, j));
			}
			product *= sum;
		}
		result = product;
		sum = 0.0;
		// FIXME paper says sum from 0 to k1, but SUMLowerBound has only k1
		// elements
		for (int i = 1; i <= k1; i++) {
			sum += sumUpperBound[i - 1] * Math.pow(z, i - 1);
		}
		result *= sum;

		result *= (p / (1.0 - epsilon));

		return result;
	}

	private double getQSmall(double z, double p, double l) {
		double result;
		double product = 1.0;
		double sum = 0.0;
		for (int i = 1; i <= k0; i++) {

			sum = 0.0;
			for (int j = 0; j <= l; j++) {
				sum += Math.pow((-countLowerBound[i - 1] * Math.pow(z, i)), j)
						/ (factorial(j) * Math.pow(i, j));
			}
			product *= sum;
		}
		result = product;

		sum = 0.0;
		for (int i = 1; i <= storeIndex; i++) {

			double productQZ = 1.0;
			for (int j = 1; j <= this.storeIndex; j++) {
				if (j != i) {
					productQZ *= (1.0 - probabilityStore[j - 1] * z);
				}
			}
			sum += valueStore[i - 1] * probabilityStore[i - 1] * productQZ;
		}

		product = 1.0;
		for (int j = 1; j <= this.storeIndex; j++) {

			double sumAsmall = 0;

			// FIXME paper says sum from 0 to k1, but SUMLowerBound has only k1
			// elements
			for (int i = 1; i <= k1; i++) {
				sumAsmall += sumLowerBound[i - 1] * Math.pow(z, i - 1);
			}
			product *= (1 - probabilityStore[j - 1] * z) * sumAsmall;
		}

		result *= (sum + product);

		result *= (p / (1.0 - epsilon));

		return result;
	}

	// TODO look for factorial in apache commons math
	private long factorial(int n) {
		long multi = 1;
		for (int i = 1; i <= n; i++) {
			multi = multi * i;
		}
		return multi;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("AvgPartialAggregate (").append(
				hashCode()).append(")");
		return ret.toString();
	}

	@Override
	public AvgPartialAggregate<T> clone() {
		return new AvgPartialAggregate<T>(this);
	}

	/**
	 * Only for testing purpose
	 * @param args
	 */
	public static void main(String[] args) {
		AvgPartialAggregate aggFunc = new AvgPartialAggregate(0.001, 0.01);

		aggFunc.update(1, 1.0);
		aggFunc.update(10, 0.1);

		double result = aggFunc.reconstruct();
		System.out.println("Result: " + result + " = 1.45");

	}
}
