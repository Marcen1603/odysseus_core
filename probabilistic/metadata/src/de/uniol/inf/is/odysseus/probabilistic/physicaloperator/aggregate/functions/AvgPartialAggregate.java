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
	private double countUpperBound;
	/** Lower bound of COUNT */
	private double countLowerBound;
	/** Upper bound of SUM */
	private double sumUpperBound;
	/** Lower bound of SUM */
	private double sumLowerBound;
	/** Index of the current store */
	private int storeIndex;

	/** Constant factors */
	private final int k0;
	private final int k1;

	private double probability;

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
		this.countLowerBound = avgPartialAggregate.countLowerBound;
		this.countUpperBound = avgPartialAggregate.countUpperBound;
		this.sumLowerBound = avgPartialAggregate.sumLowerBound;
		this.sumUpperBound = avgPartialAggregate.sumUpperBound;

	}

	public void update(double value, double probability) {
		this.probability *= (1 - probability);
		this.countUpperBound += probability;
		this.sumUpperBound = +value * probability;
		if (this.countUpperBound < (3 / this.theta)
				* Math.log(2 / this.epsilon)) {
			if (probability > this.theta) {
				this.storeIndex++;
				this.valueStore[storeIndex] = value;
				this.probabilityStore[storeIndex] = probability;
			} else {
				this.countLowerBound += probability;
				this.sumLowerBound += value * probability;
			}
		}
	}

	public double reconstruct() {
		double p = 1 / (1 - this.probability);
		double z0 = getZ0(p, this.countUpperBound);
		double smallestEvenInteger = getSmallestEvenInteger(this.countUpperBound);
		if (this.countUpperBound >= 4 / this.epsilon
				* Math.log(2 / this.epsilon)) {
			// Large Stream use SUM/COUNT
			return evaluateLongStreamAvg(p);
		} else {
			// Short Stream
			return evaluateShortStreamAvg(p, z0, smallestEvenInteger);
		}
	}

	/**
	 * Evaluate AVG on long streams using the upper bound of SUM and COUNT
	 * 
	 * @param p
	 * @return
	 */
	private double evaluateLongStreamAvg(double p) {
		return p * this.sumUpperBound / this.countUpperBound;
	}

	/**
	 * Evaluate AVG on short streams
	 * 
	 * @param p
	 * @return
	 */
	private double evaluateShortStreamAvg(double p, double z0, double l) {
		double Q;
		double estimate = 0;
		for (int i = 0; i <= z0; i++) {
			double z = i;
			if (countUpperBound >= 3 / theta * Math.log(2 / epsilon)) {
				Q = getQBig(z, p, k0, k1, l, countUpperBound, sumUpperBound);
			} else {
				Q = getQSmall(z, p, k0, k1, l, countUpperBound, sumLowerBound);
			}
			estimate += Q * z0 / (i + 1);
		}
		return estimate;
	}

	/**
	 * 
	 * @param count
	 * @return the smallest even integer greater than or equal to
	 *         5ln(2P/epsilon)
	 */
	private double getSmallestEvenInteger(double count) {
		if (count >= 1) {
			return 2 * (5 / 2) * Math.log(2 * count / epsilon);
		} else {
			return 2 * Math.max(1 / 2 * Math.log(2 / epsilon), 7 / 2);
		}
	}

	/**
	 * 
	 * @param p
	 * @param count
	 * @return
	 */
	private double getZ0(double p, double count) {
		if (count >= 1) {
			return Math.min(1, 1 / p * Math.log(2 * count / epsilon));
		} else {
			return 1;
		}
	}

	private double getQBig(double z, double p, double k0, double k1, double l,
			double Pbig, double Abig) {
		double product = 1;
		for (int i = 1; i <= k0; i++) {

			double sum = 0;
			for (int j = 0; j <= l; j++) {
				sum += (-Pbig * z) / (j * i);
			}
			product *= sum;
		}
		double sum = 0;
		for (int i = 1; i <= k1; i++) {
			sum += Abig * z;
		}
		product *= sum;

		product *= (p / (1 - epsilon));

		return product;
	}

	private double getQSmall(double z, double p, double k0, double k1,
			double l, double Psmall, double Asmall) {
		double product = 1;
		for (int i = 1; i <= k0; i++) {

			double sum = 0;
			for (int j = 0; j <= l; j++) {
				sum += (-Psmall * z) / (j * i);
			}
			product *= sum;
		}

		double sumBQ = 0;
		for (int i = 1; i <= storeIndex; i++) {

			double productQZ = 1;
			for (int j = 0; j <= this.storeIndex; j++) {
				if (j != i) {
					productQZ *= (1 - probabilityStore[j] * z);
				}
			}
			sumBQ += valueStore[i] * probabilityStore[i] * productQZ;
		}

		double productQZ = 1;
		for (int j = 0; j <= this.storeIndex; j++) {

			double sumAsmall = 0;
			for (int i = 0; i <= k1; i++) {
				sumAsmall += Asmall * z;
			}
			productQZ *= (1 - probabilityStore[j] * z) * sumAsmall;
		}

		product *= (sumBQ + productQZ);

		product *= (p / (1 - epsilon));

		return product;
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
}
