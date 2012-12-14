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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.math.Polynomial;

/**
 * AVG Aggregation on probabilistic data stream according to T.S.Jayram et al.
 * "Estimating statistical aggregates on probabilistic data streams"
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AvgPartialAggregate<T> implements IPartialAggregate<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AvgPartialAggregate.class);

    /** Upper probability bound */
    private final double        theta;
    /** Approximation error < 1/2 */
    private final double        epsilon;

    /** Storage for values */
    private final double[]      valueStore;
    /** Storage for probabilities */
    private final double[]      probabilityStore;

    /** Upper bound of COUNT */
    private double[]            pBig;
    /** Lower bound of COUNT */
    private double[]            pSmall;
    /** Upper bound of SUM */
    private double[]            aBig;
    /** Lower bound of SUM */
    private double[]            aSmall;
    /** Index of the current store */
    private int                 storeIndex;

    /** Constant factors */
    private final int           k0;
    private final int           k1;

    private double              alpha;

    /**
     * @param epsilon
     *            Approximation error
     * @param theta
     *            Upper probability bound
     */
    public AvgPartialAggregate(final double epsilon, final double theta) {
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
    }

    public AvgPartialAggregate(final double epsilon) {
        this(epsilon, 1 / Math.E);
    }

    /**
     * @param avgPartialAggregate
     */
    public AvgPartialAggregate(final AvgPartialAggregate<T> avgPartialAggregate) {
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

    public void update(final double value, final double probability) {
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
            }
            else {
                for (int k = 0; k < this.k0; k++) {
                    this.pSmall[k] += Math.pow(probability, k + 1);
                }
                for (int k = 0; k < this.k1; k++) {
                    this.aSmall[k] += value * Math.pow(probability, k + 1);
                }
            }
        }
    }

    public double getAvg() {
        return this.reconstruct();
    }

    private double reconstruct() {
        final double rho = 1.0 / (1.0 - this.alpha);

        if (AvgPartialAggregate.LOG.isDebugEnabled()) {
            assert this.checkCorollary24();
            assert this.checkClaim211(this.k0);
            assert this.checkClaim211(this.k1);
        }
        if (this.isLongStream(this.pBig[0])) {
            // Large Stream use SUM/COUNT
            return this.evaluateLongStreamAvg(rho);
        }
        else {
            // Short Stream
            return this.evaluateShortStreamAvg(rho);
        }
    }

    /**
     * @param count
     * @return true if this is a long stream, false if this is a short stream
     */
    private boolean isLongStream(final double count) {
        return (count >= ((4.0 / this.epsilon) * Math.log(2.0 / this.epsilon)));
    }

    /**
     * Evaluate AVG on long streams using the upper bound of SUM and COUNT
     * 
     * @param rho
     * @return
     */
    private double evaluateLongStreamAvg(final double rho) {
        return ((rho * this.aBig[0]) / this.pBig[0]);
    }

    /**
     * Evaluate AVG on short streams
     * 
     * @param rho
     * @return
     */
    private double evaluateShortStreamAvg(final double rho) {
        Polynomial Q;
        final double z0 = this.getZ0();

        final double smallestEvenInteger = this.getSmallestEvenInteger();

        if (this.pBig[0] >= ((3.0 / this.theta) * Math.log(2.0 / this.epsilon))) {
            if (AvgPartialAggregate.LOG.isDebugEnabled()) {
                assert this.checkProof27(this.pBig, this.aBig, z0);
                assert this.checkClaim28(this.pBig, this.aBig, z0) : z0;
                assert this.checkLemma210(this.pBig, smallestEvenInteger, z0);
            }
            Q = this.getQBig(rho, smallestEvenInteger, z0);
        }
        else {
            if (AvgPartialAggregate.LOG.isDebugEnabled()) {
                assert this.checkProof27(this.pSmall, this.aSmall, z0);
                assert this.checkClaim28(this.pSmall, this.aSmall, z0) : z0;
                assert this.checkLemma210(this.pSmall, smallestEvenInteger, z0);
            }
            Q = this.getQSmall(rho, smallestEvenInteger, z0);
        }
        final Polynomial integral = Q.integrate();

        return integral.evaluate(z0);
    }

    /**
     * @return the smallest even integer greater than or equal to
     *         5ln(2P/epsilon)
     */
    private double getSmallestEvenInteger() {
        if (this.pBig[0] >= 1.0) {
            return (2.0 * Math.ceil((5.0 / 2.0) * Math.log((2.0 * this.pBig[0]) / this.epsilon)));
        }
        else {
            return (2.0 * Math.ceil(Math.max((1.0 / 2.0) * Math.log(2.0 / this.epsilon), 7.0 / 2.0)));
        }
    }

    /**
     * @return z0
     */
    private double getZ0() {
        if (this.pBig[0] >= 1.0) {
            return Math.min(1.0, (1.0 / this.pBig[0]) * Math.log((2.0 * this.pBig[0]) / this.epsilon));
        }
        else {
            return 1.0;
        }
    }

    private Polynomial getQBig(final double rho, final double l, final double z0) {
        final Polynomial polynomial = new Polynomial(rho / (1.0 - this.epsilon), 0);

        final Polynomial gTilde = this.gTilde(this.pBig, l);

        // if (LOG.isDebugEnabled()) {
        // assert checkLemma29(gTilde, z0) : gTilde;
        // }

        final Polynomial hTilde = this.hTilde(this.aBig);

        return polynomial.multiply(gTilde.multiply(hTilde));
    }

    private Polynomial getQSmall(final double rho, final double l, final double z0) {
        final Polynomial polynomial = new Polynomial(rho / (1.0 - this.epsilon), 0);

        final Polynomial gTilde = this.gTilde(this.pSmall, l);

        // if (LOG.isDebugEnabled()) {
        // assert checkLemma29(gTilde, z0) : gTilde;
        // }

        Polynomial fJ = new Polynomial(0.0, 0);
        for (int i = 1; i <= this.storeIndex; i++) {
            Polynomial product = null;
            for (int j = 1; j <= this.storeIndex; j++) {
                if (j != i) {
                    if (product == null) {
                        product = (new Polynomial(1.0, 0)).substract(new Polynomial(this.probabilityStore[j - 1], 1));
                    }
                    else {
                        product = product.multiply((new Polynomial(1.0, 0)).substract(new Polynomial(
                                this.probabilityStore[j - 1], 1)));
                    }
                }
                else {
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
            }
            else {
                gJ = gJ.multiply((new Polynomial(1.0, 0)).substract(new Polynomial(this.probabilityStore[j - 1], 1)));
            }

        }

        final Polynomial hTilde = this.hTilde(this.aSmall);

        return polynomial.multiply(gTilde.multiply(fJ.add(gJ.multiply(hTilde))));
    }

    private Polynomial gTilde(final double[] P, final double l) {
        Polynomial gTilde = null;

        for (int i = 1; i <= this.k0; i++) {

            Polynomial sum = new Polynomial(0.0, 0);
            for (int j = 0; j <= l; j++) {

                sum = sum.add(new Polynomial(Math.pow(-P[i - 1], j) / (this.factorial(j) * Math.pow(i, j)), i * j));

            }

            if (gTilde == null) {
                gTilde = sum;
            }
            else {
                gTilde = gTilde.multiply(sum);
            }
        }

        return gTilde;
    }

    private Polynomial hTilde(final double[] A) {
        Polynomial hTilde = new Polynomial(0.0, 0);
        for (int i = 0; i < this.k1; i++) {
            hTilde = hTilde.add(new Polynomial(A[i], i));
        }

        return hTilde;
    }

    // FIXME look for factorial in apache commons math
    private double factorial(final double n) {
        double factorial = 1.0;
        for (int i = 1; i <= n; i++) {
            factorial = factorial * i;
        }
        return factorial;
    }

    private boolean checkClaim211(final double k) {
        AvgPartialAggregate.LOG.debug("Check Claim 2.11");
        boolean result = true;
        double product = 1;
        for (int i = 1; i <= k; i++) {
            product *= (1 + ((1 / (i * Math.pow(2, i))) * this.epsilon));
        }
        result = (product <= (1 + ((k / (k + 1)) * this.epsilon)));
        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    private boolean checkClaim28(final double[] P, final double[] A, final double z0) {
        AvgPartialAggregate.LOG.debug("Check Claim 2.8");
        boolean result = true;
        for (int i = 1; i <= this.k0; i++) {
            for (double z = 0; z <= z0; z += 0.01) {
                result &= (P[i - 1] * Math.pow(z, i)) <= ((P[0] * z) * Math.pow(this.theta, (i - 1)));
                if (!result) {
                    AvgPartialAggregate.LOG.error("Error at (i=" + i + ",z=" + z + "): " + (P[i - 1] * Math.pow(z, i))
                            + " <= " + ((P[0] * z) * Math.pow(this.theta, (i - 1))));
                    return result;
                }
            }
        }
        for (int i = 1; i < this.k1; i++) {
            for (int z = 0; z <= z0; z++) {
                result &= (A[i] * Math.pow(z, i)) <= (A[0] * Math.pow(this.theta, i));
                if (!result) {
                    AvgPartialAggregate.LOG.error("Error at (i=" + i + ",z=" + z + "): " + (A[i] * Math.pow(z, i))
                            + " <= " + (A[0] * Math.pow(this.theta, i)));
                    return result;
                }
            }
        }
        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    private boolean checkCorollary24() {
        AvgPartialAggregate.LOG.debug("Check Corollary 2.4");
        boolean result = true;
        if (this.pBig[0] >= ((4 / this.epsilon) * Math.log(2 / this.epsilon))) {
            result = (((2 * Math.log(this.pBig[0])) / this.pBig[0]) <= ((2 * (Math.log(2) + Math.log(2 / this.epsilon) + Math
                    .log(Math.log(2 / this.epsilon)))) / ((4 / this.epsilon) * Math.log(2 / this.epsilon))))
                    && (((2 * (Math.log(2) + Math.log(2 / this.epsilon) + Math.log(Math.log(2 / this.epsilon)))) / ((4 / this.epsilon) * Math
                            .log(2 / this.epsilon))) < this.epsilon);
        }
        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    private boolean checkTheorem23(final double rho, final double mean) {
        AvgPartialAggregate.LOG.debug("Check Theorem 2.3");
        boolean result = true;
        if (this.pBig[0] >= Math.E) {
            result = ((((rho * this.aBig[0]) / this.pBig[0]) * (1 - ((2 * Math.log(this.pBig[0])) / this.pBig[0]))) <= mean)
                    && (mean <= (((rho * this.aBig[0]) / this.pBig[0]) * (1 + (1 / (this.pBig[0] - 1)))));

            if (!result) {
                AvgPartialAggregate.LOG.error("Error at: "
                        + (((rho * this.aBig[0]) / this.pBig[0]) * (1 - ((2 * Math.log(this.pBig[0])) / this.pBig[0])))
                        + " <= " + mean + " " + mean + " <= "
                        + (((rho * this.aBig[0]) / this.pBig[0]) * (1 + (1 / (this.pBig[0] - 1)))));
            }
        }
        else {
            for (double delta = 0.0001; delta < 1.0; delta += 0.0001) {
                final double lhs = ((rho * this.aBig[0]) / this.pBig[0]) * (delta / Math.log(1 / (1 - delta)))
                        * (1 - Math.pow(1 - delta, this.pBig[0]));
                result &= (lhs <= mean);
            }
        }
        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    private boolean checkLemma27(final double value, final double mean) {
        AvgPartialAggregate.LOG.debug("Check Lemma 2.7");
        boolean result = true;
        result = ((value >= mean) && (value <= ((1 + (5 * this.epsilon)) * mean)));
        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    private boolean checkLemma29(final Polynomial gTilde, final double z0) {
        AvgPartialAggregate.LOG.debug("Check Lemma 2.9");
        boolean result = true;

        final Polynomial lhs = this.gI();
        System.out.println(lhs);
        final Polynomial rhs = this.gI().multiply(new Polynomial(1.0 + this.epsilon, 0));

        result &= ((lhs.evaluate(z0) <= gTilde.evaluate(z0)) && (gTilde.evaluate(z0) <= rhs.evaluate(z0)));
        if (!result) {
            AvgPartialAggregate.LOG.error("Error at (z=" + z0 + "): " + (lhs.evaluate(z0)) + " <= "
                    + (gTilde.evaluate(z0)) + " && " + gTilde.evaluate(z0) + " <= " + rhs.evaluate(z0));
            return result;
        }

        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    private boolean checkLemma210(final double P[], final double l, final double z0) {
        AvgPartialAggregate.LOG.debug("Check Lemma 2.10");
        boolean result = true;
        assert (this.epsilon < 0.5) : this.epsilon;
        if (P[0] >= 1.0) {
            assert (l > (5.0 * Math.log((2.0 * P[0]) / this.epsilon))) : l + " > "
                    + (5.0 * Math.log((2.0 * P[0]) / this.epsilon));
        }
        else {
            assert (l > Math.max(Math.log(2.0 / this.epsilon), 7.0)) : l + " > "
                    + Math.max(Math.log(2.0 / this.epsilon), 7.0);
        }
        assert (this.k0 >= ((2.0 * Math.log(2.0 / this.epsilon)) / Math.log(1.0 / this.theta))) : "k0=" + this.k0
                + " should be " + ((2.0 * Math.log(2.0 / this.epsilon)) / Math.log(1.0 / this.theta));
        assert (this.k1 >= (Math.log(2.0 / this.epsilon) / Math.log(1.0 / this.theta))) : "k1=" + this.k1
                + " shoudl be " + (Math.log(2.0 / this.epsilon) / Math.log(1.0 / this.theta));
        final double z = z0;
        final double lhs = this.gTilde(P, l).evaluate(z);
        double rhs = 1.0;
        for (int i = 1; i <= this.k0; i++) {
            rhs *= Math.exp((-P[i - 1] * Math.pow(z, i)) / i);
        }
        double rhs2 = 1.0;
        for (int i = 1; i <= this.k0; i++) {
            rhs2 *= (1.0 + ((1.0 / (i * Math.pow(2, i))) * this.epsilon));
        }
        rhs2 *= rhs;

        result &= (rhs <= lhs) || (Math.abs(rhs - lhs) <= 0.00001);
        if (!result) {
            AvgPartialAggregate.LOG.error("Error at (l=" + l + ",z=" + z + "): gTildek0(z) (" + rhs
                    + ") <= product(sum(1/j! (-Pi z^i)^j/i^j)) (" + P[0] + "(" + z0 + ")=" + lhs + ")");
            return result;
        }

        result &= lhs <= ((1 + this.epsilon) * rhs);
        if (!result) {
            AvgPartialAggregate.LOG.error("Error at (z=" + z + "): product(sum(1/j! (-Pi z^i)^j/i^j)) (" + lhs
                    + ") <= (1 + epsilon)gTildek0(z) (" + rhs + ")");
            return result;
        }

        result &= lhs <= rhs2;
        if (!result) {
            AvgPartialAggregate.LOG.error("Error at (z=" + z + "): " + lhs + " <= " + rhs2);
            return result;
        }
        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer ret = new StringBuffer("AvgPartialAggregate (").append(this.hashCode()).append(")");
        return ret.toString();
    }

    @Override
    public AvgPartialAggregate<T> clone() {
        return new AvgPartialAggregate<T>(this);
    }

    private Polynomial gI() {
        Polynomial gI = new Polynomial(1, 0);
        for (int i = 0; i <= this.storeIndex; i++) {
            gI = gI.multiply((new Polynomial(1, 0)).substract(new Polynomial(this.probabilityStore[i], 1)));
        }
        return gI;
    }

    private boolean checkProof27(final double[] P, final double[] A, final double z0) {
        AvgPartialAggregate.LOG.debug("Check Proof 2.7");
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

        final double rhs = sum * Math.exp(-P[0] * z0);

        result &= (Math.abs(rhs - (A[0] * Math.exp(-P[0] * z0))) < 0.000001);
        if (!result) {
            AvgPartialAggregate.LOG.error("Error at: " + rhs + " == " + (P[0] * Math.exp(-P[0] * z0)) + " -> "
                    + (Math.abs(rhs - (P[0] * Math.exp(-P[0] * z0)))));
            return result;
        }
        result &= lhs <= rhs;
        if (!result) {
            AvgPartialAggregate.LOG.error("Error at: " + lhs + " <= " + rhs);
            return result;
        }

        AvgPartialAggregate.LOG.debug("Check pass " + result);
        return result;
    }

    /**
     * Only for testing purpose
     * 
     * @param args
     */
    public static void main(final String[] args) {
        final double epsilon = 0.004;
        final double result = 1.45;
        double avg;

        final AvgPartialAggregate aggFunc = new AvgPartialAggregate(epsilon);
        aggFunc.update(1.0, 1.0);
        aggFunc.update(10.0, 0.1);

        assert aggFunc.checkTheorem23(1.0 / (1.0 - aggFunc.alpha), result);
        avg = aggFunc.getAvg();
        AvgPartialAggregate.LOG.debug("AVG of 1.0(1.0),10(0.1): " + avg + " == " + result);
        assert aggFunc.checkLemma27(avg, result) : avg;

    }
}
