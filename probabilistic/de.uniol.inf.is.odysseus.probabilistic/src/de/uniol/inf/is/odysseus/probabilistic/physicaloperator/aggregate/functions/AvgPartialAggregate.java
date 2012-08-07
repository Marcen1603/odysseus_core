/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.math.Polynomial;

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
	private double[] pBig;
	/** Lower bound of COUNT */
	private double[] pSmall;
	/** Upper bound of SUM */
	private double[] aBig;
	/** Lower bound of SUM */
	private double[] aSmall;
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
		assert ((epsilon >= 0) && (epsilon < 0.5)) : epsilon;
		if (epsilon >= 0.5) {
			throw new IllegalArgumentException("Invalid Argument: Epsilon ("
					+ epsilon + ") not in [0,1/2)");
		}
		assert ((theta <= 0.5) || (theta >= epsilon)) : theta;
		if ((theta > 0.5) || (theta < epsilon)) {
			throw new IllegalArgumentException("Invalid Argument: Theta ("
					+ theta + ") not in [" + epsilon + ",1/2]");
		}
		this.epsilon = epsilon;
		this.theta = theta;
		this.k0 = (int) (2.0 * Math.log(2.0 / this.epsilon) / Math
				.log(1.0 / this.theta));
		this.k1 = (int) (Math.log(2.0 / this.epsilon) / Math
				.log(1.0 / this.theta));
		int storeSize = (int) ((3.0 / Math.pow(this.theta, 2.0)) * Math
				.log(2.0 / this.epsilon));
		this.valueStore = new double[storeSize];
		this.probabilityStore = new double[storeSize];

		this.pBig = new double[k0];
		this.pSmall = new double[k0];
		this.aBig = new double[k1];
		this.aSmall = new double[k1];

		this.storeIndex = 0;
		assert checkClaim211(k0) : k0;
		assert checkClaim211(k1) : k1;

	}

	public AvgPartialAggregate(double epsilon) {
		// this(epsilon, Math.pow(epsilon, 0.5));
		this(epsilon, 1 / Math.E);
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
		this.pSmall = avgPartialAggregate.pSmall.clone();
		this.pBig = avgPartialAggregate.pBig.clone();
		this.aSmall = avgPartialAggregate.aSmall.clone();
		this.aBig = avgPartialAggregate.aBig.clone();

	}

	public void update(double value, double probability) {
		this.alpha *= (1.0 - probability);
		for (int k = 0; k < k0; k++) {
			this.pBig[k] += Math.pow(probability, k + 1);
		}
		for (int k = 0; k < k1; k++) {
			this.aBig[k] += value * Math.pow(probability, k + 1);
		}
		if (this.pBig[0] < ((3.0 / this.theta) * Math.log(2.0 / this.epsilon))) {
			if (probability > this.theta) {
				this.storeIndex++;
				this.valueStore[storeIndex - 1] = value;
				this.probabilityStore[storeIndex - 1] = probability;
			} else {
				for (int k = 0; k < k0; k++) {
					this.pSmall[k] += Math.pow(probability, k + 1);
				}
				for (int k = 0; k < k1; k++) {
					this.aSmall[k] += value * Math.pow(probability, k + 1);
				}
			}
		}
		assert checkCorollary24() : pBig[0];
	}

	public double getAvg() {
		return reconstruct();
	}

	private double reconstruct() {
		double rho = 1.0 / (1.0 - this.alpha);

		if (isLongStream(this.pBig[0])) {
			// Large Stream use SUM/COUNT
			System.out.println("Calc long stream");
			return evaluateLongStreamAvg(rho);
		} else {
			// Short Stream
			System.out.println("Calc short stream");
			return evaluateShortStreamAvg(rho);
		}
	}

	/**
	 * 
	 * @param count
	 * @return true if this is a long stream, false if this is a short stream
	 */
	private boolean isLongStream(double count) {
		return (count >= ((4.0 / this.epsilon) * Math.log(2.0 / this.epsilon)));
	}

	/**
	 * Evaluate AVG on long streams using the upper bound of SUM and COUNT
	 * 
	 * @param rho
	 * @return
	 */
	private double evaluateLongStreamAvg(double rho) {
		return ((rho * this.aBig[0]) / this.pBig[0]);
	}

	/**
	 * Evaluate AVG on short streams
	 * 
	 * @param rho
	 * @return
	 */
	private double evaluateShortStreamAvg(double rho) {
		Polynomial Q;
		double z0 = getZ0();
		assert checkProof27(z0);
		double smallestEvenInteger = getSmallestEvenInteger();

		if (pBig[0] >= ((3.0 / theta) * Math.log(2.0 / epsilon))) {
			assert checkClaim28(this.pBig, this.aBig, z0) : z0;
			System.out.println("z0=" + z0 + " l=" + smallestEvenInteger);
			assert checkLemma210(this.pBig, smallestEvenInteger, z0);
			Q = getQBig(rho, smallestEvenInteger, z0);
		} else {
			assert checkClaim28(this.pSmall, this.aSmall, z0) : z0;
			assert checkLemma210(this.pSmall, smallestEvenInteger, z0);
			System.out.println("z0=" + z0 + " l=" + smallestEvenInteger);
			Q = getQSmall(rho, smallestEvenInteger, z0);
		}
		System.out.println("Q(z) = " + Q);
		Polynomial integral = Q.integrate();
		System.out.println("int Q(z) = " + integral);

		return integral.evaluate(z0);
	}

	/**
	 * 
	 * @return the smallest even integer greater than or equal to
	 *         5ln(2P/epsilon)
	 */
	private double getSmallestEvenInteger() {
		if (pBig[0] >= 1.0) {
			return (2.0 * Math.ceil((5.0 / 2.0)
					* Math.log((2.0 * pBig[0]) / epsilon)));
		} else {
			return (2.0 * Math.ceil(Math.max(
					(1.0 / 2.0) * Math.log(2.0 / epsilon), 7.0 / 2.0)));
		}
	}

	/**
	 * 
	 * @return z0
	 */
	private double getZ0() {
		if (pBig[0] >= 1.0) {
			return Math.min(1.0,
					(1.0 / pBig[0]) * Math.log(2.0 * pBig[0] / epsilon));
		} else {
			return 1.0;
		}
	}

	private Polynomial getQBig(double rho, double l, double z0) {
		Polynomial polynomial = new Polynomial(rho / (1.0 - epsilon), 0);

		Polynomial gTilde = gTilde(pBig, l);
		assert checkLemma29(gTilde, z0) : gTilde;
		// FIXME paper says sum from 0 to k1, but SUMUpperBound has only k1
		// elements
		Polynomial hTilde = hTilde(aBig);

		return polynomial.multiply(gTilde.multiply(hTilde));
	}

	private Polynomial getQSmall(double rho, double l, double z0) {
		Polynomial polynomial = new Polynomial(rho / (1.0 - epsilon), 0);

		Polynomial gTilde = gTilde(pSmall, l);
		assert checkLemma29(gTilde, z0) : gTilde;
		Polynomial fJ = new Polynomial(0.0, 0);
		for (int i = 1; i <= this.storeIndex; i++) {
			Polynomial product = null;
			for (int j = 1; j <= this.storeIndex; j++) {
				if (j != i) {
					if (product == null) {
						product = (new Polynomial(1.0, 0))
								.substract(new Polynomial(
										probabilityStore[j - 1], 1));
					} else {
						product = product.multiply((new Polynomial(1.0, 0))
								.substract(new Polynomial(
										probabilityStore[j - 1], 1)));
					}
				} else {
					product = new Polynomial(1.0, 0);
				}
			}
			if (product != null) {
				fJ = fJ.add(product.multiply(new Polynomial(valueStore[i - 1]
						* probabilityStore[i - 1], 0)));
			}
		}
		System.out.println("fJ: " + fJ);

		Polynomial gJ = null;
		for (int j = 1; j <= this.storeIndex; j++) {
			if (gJ == null) {
				gJ = (new Polynomial(1.0, 0)).substract(new Polynomial(
						probabilityStore[j - 1], 1));
			} else {
				gJ = gJ.multiply((new Polynomial(1.0, 0))
						.substract(new Polynomial(probabilityStore[j - 1], 1)));
			}

		}
		System.out.println("gJ: " + gJ);

		Polynomial hTilde = hTilde(aSmall);

		return polynomial
				.multiply(gTilde.multiply(fJ.add(gJ.multiply(hTilde))));
	}

	private Polynomial gTilde(double[] P, double l) {
		Polynomial gTilde = null;

		for (int i = 1; i <= k0; i++) {

			Polynomial sum = new Polynomial(0.0, 0);
			for (int j = 0; j <= l; j++) {
				sum = sum.add(new Polynomial(Math.pow(P[i - 1] * i, j)
						/ factorial(j), i + j));
			}

			if (gTilde == null) {
				gTilde = sum;
			} else {
				gTilde = gTilde.multiply(sum);
			}
		}

		System.out.println("gTilde: " + gTilde);
		return gTilde;
	}

	private Polynomial hTilde(double[] A) {
		Polynomial hTilde = new Polynomial(0.0, 0);
		for (int i = 0; i < k1; i++) {
			hTilde = hTilde.add(new Polynomial(A[i], i));
		}

		System.out.println("hTilde: " + hTilde);
		return hTilde;
	}

	// TODO look for factorial in apache commons math
	private double factorial(double n) {
		double factorial = 1.0;
		for (int i = 1; i <= n; i++) {
			factorial = factorial * i;
		}
		return factorial;
	}

	private boolean checkClaim211(double k) {
		System.out.println("Check Claim 2.11");
		boolean result = true;
		double product = 1;
		for (int i = 1; i <= k; i++) {
			product *= (1 + (1 / (i * Math.pow(2, i))) * epsilon);
		}
		result = (product <= (1 + (k / (k + 1)) * epsilon));
		System.out.println("Check pass " + result);
		return result;
	}

	private boolean checkClaim28(double[] P, double[] A, double z0) {
		System.out.println("Check Claim 2.8");
		boolean result = true;
		for (int i = 1; i <= k0; i++) {
			for (double z = 0; z <= z0; z += 0.01) {
				result &= P[i - 1] * Math.pow(z, i) <= (P[0] * z)
						* Math.pow(theta, (i - 1));
				if (!result) {
					System.out.println("Error at (i=" + i + ",z=" + z + "): "
							+ (P[i - 1] * Math.pow(z, i)) + " <= " + (P[0] * z)
							* Math.pow(theta, (i - 1)));
					return result;
				}
			}
		}
		for (int i = 1; i < k1; i++) {
			for (int z = 0; z <= z0; z++) {
				result &= A[i] * Math.pow(z, i) <= (A[0] * Math.pow(theta, i));
				if (!result) {
					System.out.println("Error at (i=" + i + ",z=" + z + "): "
							+ (A[i] * Math.pow(z, i)) + " <= "
							+ (A[0] * Math.pow(theta, i)));
					return result;
				}
			}
		}
		System.out.println("Check pass " + result);
		return result;
	}

	private boolean checkCorollary24() {
		System.out.println("Check Corollary 2.4");
		boolean result = true;
		if (pBig[0] >= ((4 / epsilon) * Math.log(2 / epsilon))) {
			result = (2 * Math.log(pBig[0]) / pBig[0] <= (2 * (Math.log(2)
					+ Math.log(2 / epsilon) + Math.log(Math.log(2 / epsilon))) / (4 / epsilon * Math
					.log(2 / epsilon))))
					&& ((2 * (Math.log(2) + Math.log(2 / epsilon) + Math
							.log(Math.log(2 / epsilon))) / (4 / epsilon * Math
							.log(2 / epsilon))) < epsilon);
		}
		System.out.println("Check pass " + result);
		return result;
	}

	private boolean checkTheorem23(double rho, double mean) {
		System.out.println("Check Theorem 2.3");
		boolean result = true;
		if (pBig[0] >= Math.E) {
			result = (rho * aBig[0] / pBig[0]
					* (1 - (2 * Math.log(pBig[0]) / pBig[0])) <= mean)
					&& (mean <= rho * aBig[0] / pBig[0]
							* (1 + (1 / (pBig[0] - 1))));

			if (!result) {
				System.out
						.println("Error at: "
								+ (rho * aBig[0] / pBig[0] * (1 - (2 * Math
										.log(pBig[0]) / pBig[0])))
								+ " <= "
								+ mean
								+ " "
								+ mean
								+ " <= "
								+ (rho * aBig[0] / pBig[0] * (1 + (1 / (pBig[0] - 1)))));
			}
		} else {
			for (double delta = 0.0001; delta < 1.0; delta += 0.0001) {
				double lhs = rho * aBig[0] / pBig[0]
						* (delta / Math.log(1 / (1 - delta)))
						* (1 - Math.pow(1 - delta, pBig[0]));
				result &= (lhs <= mean);
			}
		}
		System.out.println("Check pass " + result);
		return result;
	}

	private boolean checkLemma27(double value, double mean) {
		System.out.println("Check Lemma 2.7");
		boolean result = true;
		result = ((value >= mean) && (value <= ((1 + 5 * epsilon) * mean)));
		System.out.println("Check pass " + result);
		return result;
	}

	private boolean checkLemma29(Polynomial gTilde, double z0) {
		System.out.println("Check Lemma 2.9");
		boolean result = true;

		Polynomial lhs = gI();
		Polynomial rhs = gI().multiply(new Polynomial(1 + epsilon, 0));

		System.out.println(lhs + " <= " + gTilde);
		System.out.println(gTilde + " <= " + rhs);
		double z = z0;
		result &= ((lhs.evaluate(z) <= gTilde.evaluate(z)) && (gTilde
				.evaluate(z) <= rhs.evaluate(z)));
		if (!result) {
			System.out.println("Error at (z=" + z + "): " + (lhs.evaluate(z))
					+ " <= " + (gTilde.evaluate(z)) + " && "
					+ gTilde.evaluate(z) + " <= " + rhs.evaluate(z));
			return result;
		}

		System.out.println("Check pass " + result);
		return result;
	}

	private boolean checkLemma210(double P[], double l, double z0) {
		System.out.println("Check Lemma 2.10");
		boolean result = true;
		assert (l >= Math.log(1 / epsilon)) : l;
		double z = z0;
		double lhs = gTilde(P, l).evaluate(z);
		double rhs = 1.0;
		for (int i = 1; i <= k0; i++) {
			rhs *= Math.exp(-P[i - 1] * Math.pow(z, i) / i);
		}
		double rhs2 = 1.0;
		for (int i = 1; i <= k0; i++) {
			rhs2 *= (1 + 1 / (i * Math.pow(2, i))) * epsilon;
		}
		rhs2 *= rhs;
		result &= lhs >= rhs;
		if (!result) {
			System.out.println("Error at (z=" + z + "): " + lhs + " >= " + rhs);
			return result;
		}
		result &= lhs <= rhs2;
		if (!result) {
			System.out
					.println("Error at (z=" + z + "): " + lhs + " <= " + rhs2);
			return result;
		}

		System.out.println("Check pass " + result);
		return result;
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

	private Polynomial gI() {
		Polynomial gI = new Polynomial(1, 0);
		for (int i = 0; i < probabilityStore.length; i++) {
			gI = gI.multiply((new Polynomial(1, 0)).substract(new Polynomial(
					probabilityStore[i], 1)));
		}
		return gI;
	}

	private boolean checkProof27(double z0) {
		System.out.println("Check Proof 2.7");
		boolean result = true;
		double product = 1.0;
		double lhs = 1.0;
		double sum = 0.0;
		if ((1 - z0) > 0) {
			for (int i = 0; i < probabilityStore.length; i++) {
				product *= (1 - probabilityStore[i] * z0);
			}

			for (int i = 0; i < probabilityStore.length; i++) {
				sum += (valueStore[i] * probabilityStore[i])
						/ (1 - probabilityStore[i] * z0);
			}
			lhs = product * sum * (1 - z0);
		}

		sum = 0.0;
		for (int i = 0; i < probabilityStore.length; i++) {
			sum += valueStore[i] * probabilityStore[i];
		}

		double rhs = sum * Math.exp(-pBig[0] * z0);
		result = rhs == aBig[0] * Math.exp(-pBig[0] * z0);
		result = lhs <= rhs;

		System.out.println("Check pass " + result);
		return result;
	}

	/**
	 * Only for testing purpose
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double epsilon = 0.04;
		double result = 1.45;
		double avg;

		Polynomial poly = (new Polynomial(3, 1)).add(new Polynomial(2, 2));

		System.out.println(poly);
		poly = poly.substract(new Polynomial(2, 2));
		System.out.println(poly);
		poly = (poly).multiply(new Polynomial(2, 2));
		System.out.println(poly);

		AvgPartialAggregate aggFunc = new AvgPartialAggregate(epsilon);
		// for (int i = 0; i <= 10000; i++) {
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		aggFunc.update(1.0, 1.0);
		// }
		assert aggFunc.checkTheorem23(1.0 / (1.0 - aggFunc.alpha), 1.0);
		avg = aggFunc.getAvg();
		System.out.println("AVG of 1.0(1.0): " + avg);
		assert aggFunc.checkLemma27(avg, 1.0) : avg;

		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		aggFunc.update(10.0, 0.1);
		assert aggFunc.checkTheorem23(1.0 / (1.0 - aggFunc.alpha), result);
		avg = aggFunc.getAvg();
		System.out.println("AVG of 1.0(1.0),10(0.1): " + avg + " == " + result);
		assert aggFunc.checkLemma27(avg, result) : avg;

	}
}
