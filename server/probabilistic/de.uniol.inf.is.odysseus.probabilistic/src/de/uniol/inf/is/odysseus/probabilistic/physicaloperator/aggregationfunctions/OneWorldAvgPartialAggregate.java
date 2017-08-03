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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.Polynomial;

/**
 * AVG Aggregation on probabilistic data stream according to T.S.Jayram et al.
 * "Estimating statistical aggregates on probabilistic data streams"
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class OneWorldAvgPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = -3979148035308294036L;

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(OneWorldAvgPartialAggregate.class);

	/** Upper probability bound. */
	private final double theta;
	/** Approximation error (e) \f$ e < 1/2 \f$. */
	private final double epsilon;

	/** Storage for values. */
	private final double[] valueStore;
	/** Storage for probabilities. */
	private final double[] probabilityStore;

	/** Upper bound of COUNT. */
	private double[] pBig;
	/** Lower bound of COUNT. */
	private double[] pSmall;
	/** Upper bound of SUM. */
	private double[] aBig;
	/** Lower bound of SUM. */
	private double[] aSmall;
	/** Index of the current store. */
	private int storeIndex;

	/** Constant factors k0. */
	private final int k0;
	/** Constant factors k1. */
	private final int k1;
	/** Alpha. */
	private double alpha;
	/** The result data type. */
	private final String datatype;

	/**
	 * Default constructor with approximation error (\f$ \epsilon \f$) and upper
	 * probability bound (\f$ \theta \f$).
	 * 
	 * @param epsilon
	 *            Approximation error
	 * @param theta
	 *            Upper probability bound
	 * @param datatype
	 *            The result datatype
	 */
	public OneWorldAvgPartialAggregate(final double epsilon, final double theta, final String datatype) {
		if (epsilon >= 0.5) {
			throw new IllegalArgumentException("Invalid Argument: Epsilon (" + epsilon + ") not in [0,1/2)");
		}
		if ((theta > 0.5) || (theta < epsilon)) {
			throw new IllegalArgumentException("Invalid Argument: Theta (" + theta + ") not in [" + epsilon + ",1/2]");
		}
		this.epsilon = epsilon;
		this.theta = theta;
		this.k0 = (int) Math.ceil(((2.0 * Math.log(2.0 / this.epsilon)) / Math.log(1.0 / this.theta)));
		this.k1 = (int) Math.ceil((Math.log(2.0 / this.epsilon) / Math.log(1.0 / this.theta)));
		final int storeSize = (int) ((3.0 / Math.pow(this.theta, 2.0)) * Math.log(2.0 / this.epsilon));
		this.valueStore = new double[storeSize];
		this.probabilityStore = new double[storeSize];

		this.pBig = new double[this.k0];
		this.pSmall = new double[this.k0];
		this.aBig = new double[this.k1];
		this.aSmall = new double[this.k1];

		this.storeIndex = 0;
		this.datatype = datatype;
	}

	/**
	 * Default constructor with approximation error (\f$ \epsilon \f$) and
	 * constant upper probability bound (\f$ \theta = 1/e \f$).
	 * 
	 * @param epsilon
	 *            Approximation error
	 * @param datatype
	 *            The result datatype
	 */
	public OneWorldAvgPartialAggregate(final double epsilon, final String datatype) {
		this(epsilon, 1.0 / Math.E, datatype);
	}

	/**
	 * Clone constructor.
	 * 
	 * @param avgPartialAggregate
	 *            The object to clone
	 */
	public OneWorldAvgPartialAggregate(final OneWorldAvgPartialAggregate<T> avgPartialAggregate) {
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
		this.datatype = avgPartialAggregate.datatype;
	}

	/**
	 * Updates the current average with the given value.
	 * 
	 * @param value
	 *            The value
	 * @param probability
	 *            The probability of the value
	 */
	public final void update(final double value, final double probability) {
		this.alpha *= (1.0 - probability);
		for (int k = 0; k < this.k0; k++) {
			this.pBig[k] += Math.pow(probability, k + 1);
		}
		for (int k = 0; k < this.k1; k++) {
			this.aBig[k] += value * Math.pow(probability, k + 1);
		}
		if (this.pBig[0] < ((3.0 / this.theta) * Math.log(2.0 / this.epsilon))) {
			if (probability > this.theta) {
				this.storeIndex++;
				this.valueStore[this.storeIndex - 1] = value;
				this.probabilityStore[this.storeIndex - 1] = probability;
			} else {
				for (int k = 0; k < this.k0; k++) {
					this.pSmall[k] += Math.pow(probability, k + 1);
				}
				for (int k = 0; k < this.k1; k++) {
					this.aSmall[k] += value * Math.pow(probability, k + 1);
				}
			}
		}
	}

	/**
	 * Gets the current average.
	 * 
	 * @return The average.
	 */
	public final double getAggregate() {
		return this.reconstruct();
	}

	/**
	 * Reconstruct the stream average.
	 * 
	 * Currently this function also checks the Corollary 2.4 and the Claim 2.11
	 * for k0 and k1 if trace is enable.
	 * 
	 * @return The reconstructed average
	 */
	private double reconstruct() {
		final double rho = 1.0 / (1.0 - this.alpha);

		if (OneWorldAvgPartialAggregate.LOG.isTraceEnabled()) {
			Preconditions.checkState(this.checkCorollary24());
			Preconditions.checkState(this.checkClaim211(this.k0));
			Preconditions.checkState(this.checkClaim211(this.k1));
		}
		if (this.isLongStream(this.pBig[0])) {
			// Large Stream use SUM/COUNT
			return this.evaluateLongStreamAvg(rho);
		} else {
			// Short Stream
			return this.evaluateShortStreamAvg(rho);
		}
	}

	/**
	 * Checks whether the given stream is a long stream. More formally a stream
	 * is a long stream iff:
	 * 
	 * \f$ |stream| >= ((4.0 / \epsilon) * \log(2.0/ \epsilon)) \f$
	 * 
	 * @param count
	 *            The size of the stream
	 * @return true if this is a long stream, false if this is a short stream
	 */
	private boolean isLongStream(final double count) {
		return (count >= ((4.0 / this.epsilon) * Math.log(2.0 / this.epsilon)));
	}

	/**
	 * Evaluate AVG on long streams using the upper bound of SUM and COUNT.
	 * 
	 * @param rho
	 *            The value of \f$ \rho \f$
	 * @return The average
	 */
	private double evaluateLongStreamAvg(final double rho) {
		return ((rho * this.aBig[0]) / this.pBig[0]);
	}

	/**
	 * Evaluate AVG on short streams.
	 * 
	 * @param rho
	 *            The value of \f$ \rho \f$
	 * @return The average
	 */
	private double evaluateShortStreamAvg(final double rho) {
		Polynomial q;
		final double z0 = this.getZ0();

		final double smallestEvenInteger = this.getSmallestEvenInteger();

		if (this.pBig[0] >= ((3.0 / this.theta) * Math.log(2.0 / this.epsilon))) {
			if (OneWorldAvgPartialAggregate.LOG.isTraceEnabled()) {
				Preconditions.checkState(this.checkProof27(this.pBig, this.aBig, z0));
				Preconditions.checkState(this.checkClaim28(this.pBig, this.aBig, z0), z0);
				Preconditions.checkState(this.checkLemma210(this.pBig, smallestEvenInteger, z0));
			}
			q = this.getQBig(rho, smallestEvenInteger, z0);
		} else {
			if (OneWorldAvgPartialAggregate.LOG.isTraceEnabled()) {
				Preconditions.checkState(this.checkProof27(this.pSmall, this.aSmall, z0));
				Preconditions.checkState(this.checkClaim28(this.pSmall, this.aSmall, z0), z0);
				Preconditions.checkState(this.checkLemma210(this.pSmall, smallestEvenInteger, z0));
			}
			q = this.getQSmall(rho, smallestEvenInteger, z0);
		}
		final Polynomial integral = q.integrate();

		return integral.evaluate(z0);
	}

	/**
	 * Gets the smallest even integer that is greater or equal to \f$ 5 \ln(2
	 * P/\epsilon) \f$.
	 * 
	 * @return the smallest even integer
	 */
	private double getSmallestEvenInteger() {
		if (this.pBig[0] >= 1.0) {
			return (2.0 * Math.ceil((5.0 / 2.0) * Math.log((2.0 * this.pBig[0]) / this.epsilon)));
		} else {
			return (2.0 * Math.ceil(Math.max((1.0 / 2.0) * Math.log(2.0 / this.epsilon), 7.0 / 2.0)));
		}
	}

	/**
	 * Gets the value of \f$ z_{0} \f$.
	 * 
	 * @return The value of \f$ z_{0} \f$
	 */
	private double getZ0() {
		if (this.pBig[0] >= 1.0) {
			return Math.min(1.0, (1.0 / this.pBig[0]) * Math.log((2.0 * this.pBig[0]) / this.epsilon));
		} else {
			return 1.0;
		}
	}

	/**
	 * Get the value of \f$ q_{big} \f$.
	 * 
	 * @param rho
	 *            The value of \f$ \rho \f$
	 * @param l
	 *            The value of \f$ l \f$
	 * @param z0
	 *            The value of \f$ z_{0} \f$
	 * @return The value of \f$ q_{big} \f$
	 */
	private Polynomial getQBig(final double rho, final double l, final double z0) {
		final Polynomial polynomial = new Polynomial(rho / (1.0 - this.epsilon), 0);
		final Polynomial gTilde = this.gTilde(this.pBig, l);
		if (OneWorldAvgPartialAggregate.LOG.isTraceEnabled()) {
			Preconditions.checkState(this.checkLemma29(gTilde, z0), gTilde);
		}
		final Polynomial hTilde = this.hTilde(this.aBig);
		return polynomial.multiply(gTilde.multiply(hTilde));
	}

	/**
	 * Get the value of \f$ q_{small} \f$.
	 * 
	 * @param rho
	 *            The value of \f$ \rho \f$
	 * @param l
	 *            The value of \f$ l \f$
	 * @param z0
	 *            The value of \f$ z_{0} \f$
	 * @return The value of \f$ q_{small} \f$
	 */
	private Polynomial getQSmall(final double rho, final double l, final double z0) {
		final Polynomial polynomial = new Polynomial(rho / (1.0 - this.epsilon), 0);
		final Polynomial gTilde = this.gTilde(this.pSmall, l);
		if (OneWorldAvgPartialAggregate.LOG.isTraceEnabled()) {
			Preconditions.checkState(this.checkLemma29(gTilde, z0), gTilde);
		}

		Polynomial fJ = new Polynomial(0.0, 0);
		for (int i = 1; i <= this.storeIndex; i++) {
			Polynomial product = null;
			for (int j = 1; j <= this.storeIndex; j++) {
				if (j != i) {
					if (product == null) {
						product = (new Polynomial(1.0, 0)).substract(new Polynomial(this.probabilityStore[j - 1], 1));
					} else {
						product = product.multiply(
								(new Polynomial(1.0, 0)).substract(new Polynomial(this.probabilityStore[j - 1], 1)));
					}
				} else {
					product = new Polynomial(1.0, 0);
				}
			}
			if (product != null) {
				fJ = fJ.add(product.multiply(new Polynomial(this.valueStore[i - 1] * this.probabilityStore[i - 1], 0)));
			}
		}

		Polynomial gJ = null;
		for (int j = 1; j <= this.storeIndex; j++) {
			if (gJ == null) {
				gJ = (new Polynomial(1.0, 0)).substract(new Polynomial(this.probabilityStore[j - 1], 1));
			} else {
				gJ = gJ.multiply((new Polynomial(1.0, 0)).substract(new Polynomial(this.probabilityStore[j - 1], 1)));
			}

		}

		final Polynomial hTilde = this.hTilde(this.aSmall);
		if (gJ == null) {
			return null;
		}
		return polynomial.multiply(gTilde.multiply(fJ.add(gJ.multiply(hTilde))));
	}

	/**
	 * Gets the value of \f$ \~{g} \f$.
	 * 
	 * @param p
	 *            The value of \f$ P \f$
	 * @param l
	 *            The value of \f$ l \f$
	 * @return The value of \f$ \~{g} \f$
	 */
	@SuppressWarnings("deprecation")
	private Polynomial gTilde(final double[] p, final double l) {
		Polynomial gTilde = null;
		for (int i = 1; i <= this.k0; i++) {
			Polynomial sum = new Polynomial(0.0, 0);
			for (int j = 0; j <= l; j++) {
				sum = sum.add(new Polynomial(
						Math.pow(-p[i - 1], j) / (ArithmeticUtils.factorialDouble(j) * Math.pow(i, j)), i * j));
			}
			if (gTilde == null) {
				gTilde = sum;
			} else {
				gTilde = gTilde.multiply(sum);
			}
		}
		return gTilde;
	}

	/**
	 * Gets the value of \f$ \~{h} \f$.
	 * 
	 * @param a
	 *            The value of \f$ A \f$
	 * @return The value of \f$ \~{h} \f$
	 */
	private Polynomial hTilde(final double[] a) {
		Polynomial hTilde = new Polynomial(0.0, 0);
		for (int i = 0; i < this.k1; i++) {
			hTilde = hTilde.add(new Polynomial(a[i], i));
		}

		return hTilde;
	}

	/**
	 * Checks claim 2.11.
	 * 
	 * @param k
	 *            The value of \f$ k \f$
	 * @return <code>true</code> if claim passed
	 */
	private boolean checkClaim211(final double k) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Claim 2.11");
		boolean result = true;
		double product = 1;
		for (int i = 1; i <= k; i++) {
			product *= (1 + ((1 / (i * Math.pow(2, i))) * this.epsilon));
		}
		result = (product <= (1 + ((k / (k + 1)) * this.epsilon)));
		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Checks claim 2.8.
	 * 
	 * @param p
	 *            The value of \f$ P \f$
	 * @param a
	 *            The value of \f$ A \f$
	 * @param z0
	 *            The value of \f$ z_{0} \f$
	 * @return <code>true</code> if claim passed
	 */
	private boolean checkClaim28(final double[] p, final double[] a, final double z0) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Claim 2.8");
		boolean result = true;
		for (int i = 1; i <= this.k0; i++) {
			for (double z = 0; z <= z0; z += 0.01) {
				result &= (p[i - 1] * Math.pow(z, i)) <= ((p[0] * z) * Math.pow(this.theta, (i - 1)));
				if (!result) {
					OneWorldAvgPartialAggregate.LOG.error("Error at (i=" + i + ",z=" + z + "): "
							+ (p[i - 1] * Math.pow(z, i)) + " <= " + ((p[0] * z) * Math.pow(this.theta, (i - 1))));
					return result;
				}
			}
		}
		for (int i = 1; i < this.k1; i++) {
			for (int z = 0; z <= z0; z++) {
				result &= (a[i] * Math.pow(z, i)) <= (a[0] * Math.pow(this.theta, i));
				if (!result) {
					OneWorldAvgPartialAggregate.LOG.error("Error at (i=" + i + ",z=" + z + "): "
							+ (a[i] * Math.pow(z, i)) + " <= " + (a[0] * Math.pow(this.theta, i)));
					return result;
				}
			}
		}
		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Checks corollary 2.4.
	 * 
	 * @return <code>true</code> if corollary passed
	 */
	private boolean checkCorollary24() {
		OneWorldAvgPartialAggregate.LOG.trace("Check Corollary 2.4");
		boolean result = true;
		if (this.pBig[0] >= ((4 / this.epsilon) * Math.log(2 / this.epsilon))) {
			result = (((2 * Math.log(this.pBig[0])) / this.pBig[0]) <= ((2
					* (Math.log(2) + Math.log(2 / this.epsilon) + Math.log(Math.log(2 / this.epsilon))))
					/ ((4 / this.epsilon) * Math.log(2 / this.epsilon))))
					&& (((2 * (Math.log(2) + Math.log(2 / this.epsilon) + Math.log(Math.log(2 / this.epsilon))))
							/ ((4 / this.epsilon) * Math.log(2 / this.epsilon))) < this.epsilon);
		}
		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Checks theorem 2.3.
	 * 
	 * @param rho
	 *            The value of \f$ \rho \f$
	 * @param mean
	 *            The value of \f$ \mu \f$
	 * @return <code>true</code> if theorem passed
	 */
	private boolean checkTheorem23(final double rho, final double mean) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Theorem 2.3");
		boolean result = true;
		if (this.pBig[0] >= Math.E) {
			result = ((((rho * this.aBig[0]) / this.pBig[0])
					* (1 - ((2 * Math.log(this.pBig[0])) / this.pBig[0]))) <= mean)
					&& (mean <= (((rho * this.aBig[0]) / this.pBig[0]) * (1 + (1 / (this.pBig[0] - 1)))));

			if (!result) {
				OneWorldAvgPartialAggregate.LOG
						.error("Error at: "
								+ (((rho * this.aBig[0]) / this.pBig[0])
										* (1 - ((2 * Math.log(this.pBig[0])) / this.pBig[0])))
								+ " <= " + mean + " " + mean + " <= "
								+ (((rho * this.aBig[0]) / this.pBig[0]) * (1 + (1 / (this.pBig[0] - 1)))));
			}
		} else {
			for (double delta = 0.0001; delta < 1.0; delta += 0.0001) {
				final double lhs = ((rho * this.aBig[0]) / this.pBig[0]) * (delta / Math.log(1 / (1 - delta)))
						* (1 - Math.pow(1 - delta, this.pBig[0]));
				result &= (lhs <= mean);
			}
		}
		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Checks lemma 2.7.
	 * 
	 * @param value
	 *            The value
	 * @param mean
	 *            The value of \f$ \mu \f$
	 * @return <code>true</code> if lemma passed
	 */
	private boolean checkLemma27(final double value, final double mean) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Lemma 2.7");
		boolean result = true;
		result = ((value >= mean) && (value <= ((1 + (5 * this.epsilon)) * mean)));
		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Checks lemma 2.9.
	 * 
	 * @param gTilde
	 *            The value of \f$ \~{g} \f$
	 * @param z0
	 *            The value of \f$ z_{0} \f$
	 * @return <code>true</code> if lemma passed
	 */
	private boolean checkLemma29(final Polynomial gTilde, final double z0) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Lemma 2.9");
		boolean result = true;

		final Polynomial lhs = this.gI();
		System.out.println(lhs);
		final Polynomial rhs = this.gI().multiply(new Polynomial(1.0 + this.epsilon, 0));

		result &= ((lhs.evaluate(z0) <= gTilde.evaluate(z0)) && (gTilde.evaluate(z0) <= rhs.evaluate(z0)));
		if (!result) {
			OneWorldAvgPartialAggregate.LOG.error("Error at (z=" + z0 + "): " + (lhs.evaluate(z0)) + " <= "
					+ (gTilde.evaluate(z0)) + " && " + gTilde.evaluate(z0) + " <= " + rhs.evaluate(z0));
			return result;
		}

		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Checks lemma 2.10.
	 * 
	 * @param p
	 *            The value of \f$ P \f$
	 * @param l
	 *            The value of \f$ l \f$
	 * @param z0
	 *            The value of \f$ z_{0} \f$
	 * @return <code>true</code> if lemma passed
	 */
	private boolean checkLemma210(final double[] p, final double l, final double z0) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Lemma 2.10");
		boolean result = true;
		assert (this.epsilon < 0.5) : this.epsilon;
		if (p[0] >= 1.0) {
			assert (l > (5.0 * Math.log((2.0 * p[0]) / this.epsilon))) : l + " > "
					+ (5.0 * Math.log((2.0 * p[0]) / this.epsilon));
		} else {
			assert (l > Math.max(Math.log(2.0 / this.epsilon), 7.0)) : l + " > "
					+ Math.max(Math.log(2.0 / this.epsilon), 7.0);
		}
		assert (this.k0 >= ((2.0 * Math.log(2.0 / this.epsilon)) / Math.log(1.0 / this.theta))) : "k0=" + this.k0
				+ " should be " + ((2.0 * Math.log(2.0 / this.epsilon)) / Math.log(1.0 / this.theta));
		assert (this.k1 >= (Math.log(2.0 / this.epsilon) / Math.log(1.0 / this.theta))) : "k1=" + this.k1
				+ " shoudl be " + (Math.log(2.0 / this.epsilon) / Math.log(1.0 / this.theta));
		final double z = z0;
		final double lhs = this.gTilde(p, l).evaluate(z);
		double rhs = 1.0;
		for (int i = 1; i <= this.k0; i++) {
			rhs *= Math.exp((-p[i - 1] * Math.pow(z, i)) / i);
		}
		double rhs2 = 1.0;
		for (int i = 1; i <= this.k0; i++) {
			rhs2 *= (1.0 + ((1.0 / (i * Math.pow(2, i))) * this.epsilon));
		}
		rhs2 *= rhs;

		result &= (rhs <= lhs) || (Math.abs(rhs - lhs) <= 0.00001);
		if (!result) {
			OneWorldAvgPartialAggregate.LOG.error("Error at (l=" + l + ",z=" + z + "): gTildek0(z) (" + rhs
					+ ") <= product(sum(1/j! (-Pi z^i)^j/i^j)) (" + p[0] + "(" + z0 + ")=" + lhs + ")");
			return result;
		}

		result &= lhs <= ((1 + this.epsilon) * rhs);
		if (!result) {
			OneWorldAvgPartialAggregate.LOG.error("Error at (z=" + z + "): product(sum(1/j! (-Pi z^i)^j/i^j)) (" + lhs
					+ ") <= (1 + epsilon)gTildek0(z) (" + rhs + ")");
			return result;
		}

		result &= lhs <= rhs2;
		if (!result) {
			OneWorldAvgPartialAggregate.LOG.error("Error at (z=" + z + "): " + lhs + " <= " + rhs2);
			return result;
		}
		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuffer ret = new StringBuffer("AvgPartialAggregate (").append(this.hashCode()).append(")");
		return ret.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final OneWorldAvgPartialAggregate<T> clone() {
		return new OneWorldAvgPartialAggregate<T>(this);
	}

	/**
	 * Gets the polynomial \f$ g_{I} \f$.
	 * 
	 * @return The polynomial \f$ g_{I} \f$
	 */
	private Polynomial gI() {
		Polynomial gI = new Polynomial(1, 0);
		for (int i = 0; i <= this.storeIndex; i++) {
			gI = gI.multiply((new Polynomial(1, 0)).substract(new Polynomial(this.probabilityStore[i], 1)));
		}
		return gI;
	}

	/**
	 * Checks proof 2.7.
	 * 
	 * @param p
	 *            The value of \f$ P \f$
	 * @param a
	 *            The value of \f$ A \f$
	 * @param z0
	 *            The value of \f$ z_{0} \f$
	 * @return <code>true</code> if proof passed
	 */
	private boolean checkProof27(final double[] p, final double[] a, final double z0) {
		OneWorldAvgPartialAggregate.LOG.trace("Check Proof 2.7");
		boolean result = true;
		double product = 1.0;
		double lhs = 1.0;
		double sum = 0.0;

		for (int i = 0; i <= this.storeIndex; i++) {
			product *= (1 - (this.probabilityStore[i] * z0));
		}

		for (int i = 0; i <= this.storeIndex; i++) {
			if ((this.probabilityStore[i] * z0) != 1.0) {
				sum += (this.valueStore[i] * this.probabilityStore[i]) / (1 - (this.probabilityStore[i] * z0));
			}
		}
		lhs = product * sum * (1 - z0);

		sum = 0.0;
		for (int i = 0; i <= this.storeIndex; i++) {
			sum += this.valueStore[i] * this.probabilityStore[i];
		}

		final double rhs = sum * Math.exp(-p[0] * z0);

		result &= (Math.abs(rhs - (a[0] * Math.exp(-p[0] * z0))) < 0.000001);
		if (!result) {
			OneWorldAvgPartialAggregate.LOG.error("Error at: " + rhs + " == " + (p[0] * Math.exp(-p[0] * z0)) + " -> "
					+ (Math.abs(rhs - (p[0] * Math.exp(-p[0] * z0)))));
			return result;
		}
		result &= lhs <= rhs;
		if (!result) {
			OneWorldAvgPartialAggregate.LOG.error("Error at: " + lhs + " <= " + rhs);
			return result;
		}

		OneWorldAvgPartialAggregate.LOG.trace("Check pass " + result);
		return result;
	}

	/**
	 * Only for testing purpose.
	 * 
	 * @param args
	 *            Default args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(final String[] args) {
		final double epsilon = 0.004;
		final double result = 1.45;
		double avg;

		final OneWorldAvgPartialAggregate aggFunc = new OneWorldAvgPartialAggregate(epsilon, "Double");
		aggFunc.update(1.0, 1.0);
		aggFunc.update(10.0, 0.1);

		assert aggFunc.checkTheorem23(1.0 / (1.0 - aggFunc.alpha), result);
		avg = aggFunc.getAggregate();
		OneWorldAvgPartialAggregate.LOG.debug("AVG of 1.0(1.0),10(0.1): " + avg + " == " + result);
		assert aggFunc.checkLemma27(avg, result) : avg;

	}
}
