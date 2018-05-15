/**
 *
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class MultivariateEnumeratedDistribution implements IMultivariateDistribution {

    /**
     *
     */
    private static final long serialVersionUID = -7901800746273849189L;
    private final RandomGenerator random = new Well19937c();
    private  List<Sample> sampleSpace;

    public MultivariateEnumeratedDistribution(final double[] singleton, final double probability) {
        this.sampleSpace = new ArrayList<>(1);
        this.sampleSpace.add(new Sample(singleton, probability));
    }

    /**
     *
     * @param rng
     * @param singletons
     * @param probabilities
     */
    public MultivariateEnumeratedDistribution(final double[][] singletons, final double[] probabilities) {
        this.sampleSpace = new ArrayList<>(singletons.length);
        for (int i = 0; i < singletons.length; i++) {
            this.sampleSpace.add(new Sample(singletons[i], probabilities[i]));
        }

    }

    public MultivariateEnumeratedDistribution(final List<Sample> sampleSpace) {
        this.sampleSpace = sampleSpace;
    }

    /**
     * @param extendedMultivariateEnumeratedRealDistribution
     */
    public MultivariateEnumeratedDistribution(final MultivariateEnumeratedDistribution copy) {
        this.sampleSpace = new ArrayList<>(copy.sampleSpace.size());
        for (final Sample sample : copy.sampleSpace) {
            this.sampleSpace.add(new Sample(MathArrays.copyOf(sample.getKey()), sample.getValue()));
        }

    }

    @Override
    public double probability(final double[] a) {
        double probability = 0;

        for (final Sample sample : this.sampleSpace) {
            boolean inBound = true;
            for (int d = 0; d < a.length; d++) {
                if (sample.getKey()[d] > a[d]) {
                    inBound = false;
                    break;
                }
            }
            if (inBound) {
                probability += sample.getValue();
            }
        }

        return probability;
    }

    @Override
    public double probability(final double[] a, final double[] b) {
        double probability = 0;
        if (Arrays.equals(a, b)) {
            // This is the PMF
            for (final Sample sample : this.sampleSpace) {
                if (((a == null) && (sample.getKey() == null)) || ((a != null) && Arrays.equals(a, sample.getKey()))) {
                    probability += sample.getValue();
                }
            }
        } else {
            for (final Sample sample : this.sampleSpace) {
                boolean inBound = true;
                for (int d = 0; d < a.length; d++) {
                    if ((sample.getKey()[d] < a[d]) || (sample.getKey()[d] > b[d])) {
                        inBound = false;
                        break;
                    }
                }
                if (inBound) {
                    probability += sample.getValue();
                }
            }
        }

        return probability;
    }

    @Override
    public int getDimension() {
        if (this.sampleSpace.size() > 0) {
            return this.sampleSpace.get(0).getKey().length;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double density(final double[] x) {
        // Call PMF of the discrete distribution
        return this.probability(x, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] sample() {
        final double randomValue = this.random.nextDouble();
        double sum = 0;

        for (int i = 0; i < this.sampleSpace.size(); i++) {
            sum += this.sampleSpace.get(i).getValue();
            if (randomValue < sum) {
                return this.sampleSpace.get(i).getKey();
            }
        }
        return this.sampleSpace.get(this.sampleSpace.size() - 1).getKey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code sum(singletons[i] * probabilities[i])}
     */
    @Override
    public double[] getMean() {
        final double[] mean = new double[getDimension()];

        for (final Sample sample : this.sampleSpace) {
            for (int i = 0; i < sample.getKey().length; i++) {
                mean[i] += sample.getValue() * sample.getKey()[i];
            }
        }

        return mean;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code sum((singletons[i] - mean) ^ 2 * probabilities[i])}
     */
    @Override
    public double[][] getVariance() {
        final double[] mean = new double[getDimension()];
        final double[] meanOfSquares = new double[getDimension()];
        final double[][] result = new double[getDimension()][getDimension()];
        for (final Sample sample : this.sampleSpace) {
            for (int i = 0; i < sample.getKey().length; i++) {
                mean[i] += sample.getValue() * sample.getKey()[i];
                meanOfSquares[i] += sample.getValue() * sample.getKey()[i] * sample.getKey()[i];
            }
        }
        for (int i = 0; i < getDimension(); i++) {
            result[i][i] = meanOfSquares[i] - (mean[i] * mean[i]);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * Returns the lowest value with non-zero probability.
     *
     * @return the lowest value with non-zero probability.
     */
    public double[] getSupportLowerBound() {
        final double[] min = new double[getDimension()];
        Arrays.fill(min, Double.POSITIVE_INFINITY);
        for (final Sample sample : this.sampleSpace) {
            for (int i = 0; i < sample.getKey().length; i++) {
                if ((sample.getKey()[i] < min[i]) && (sample.getValue() > 0)) {
                    min[i] = sample.getKey()[i];
                }
            }
        }

        return min;
    }

    /**
     * {@inheritDoc}
     *
     * Returns the highest value with non-zero probability.
     *
     * @return the highest value with non-zero probability.
     */
    public double[] getSupportUpperBound() {
        final double[] max = new double[getDimension()];
        Arrays.fill(max, Double.NEGATIVE_INFINITY);
        for (final Sample sample : this.sampleSpace) {
            for (int i = 0; i < sample.getKey().length; i++) {
                if ((sample.getKey()[i] > max[i]) && (sample.getValue() > 0)) {
                    max[i] = sample.getKey()[i];
                }
            }
        }

        return max;
    }

    /**
     * {@inheritDoc}
     *
     * The support of this distribution includes the lower bound.
     *
     * @return {@code true}
     */
    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * The support of this distribution includes the upper bound.
     *
     * @return {@code true}
     */
    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * The support of this distribution is connected.
     *
     * @return {@code true}
     */
    public boolean isSupportConnected() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(final RealMatrix restrict) {
        final ArrayList<Sample> newSampleSpace = new ArrayList<>(this.sampleSpace.size());
        for (final Sample sample : this.sampleSpace) {
            final RealMatrix singletonMatrix = restrict.multiply(MatrixUtils.createRealDiagonalMatrix(sample.getKey())).multiply(restrict.transpose());
            final double[] singleton = new double[singletonMatrix.getRowDimension()];

            for (int d = 0; d < singletonMatrix.getRowDimension(); d++) {
                singleton[d] = singletonMatrix.getEntry(d, d);
            }
            newSampleSpace.add(new Sample(singleton, sample.getValue()));
        }
        this.sampleSpace = newSampleSpace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution add(final Double number) {
        final List<Sample> thisSampleSpace = this.sampleSpace;

        final Map<RealMatrix, Double> singletons = new HashMap<>(thisSampleSpace.size());
        for (final Sample thisSample : thisSampleSpace) {
            final RealMatrix thisSingleton = new Array2DRowRealMatrix(thisSample.getKey());
            final RealMatrix value = thisSingleton.scalarAdd(number);
            if (singletons.containsKey(value)) {
                singletons.put(value, singletons.get(value) + (thisSample.getValue()));
            } else {
                singletons.put(value, thisSample.getValue());
            }
        }
        final List<Sample> newSampleSpace = new ArrayList<>(singletons.size());

        for (final Entry<RealMatrix, Double> entry : singletons.entrySet()) {
            newSampleSpace.add(new Sample(entry.getKey().getColumn(0), entry.getValue()));
        }
        return new MultivariateEnumeratedDistribution(newSampleSpace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution subtract(final Double number) {
        return this.add(-number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution multiply(final Double number) {
        final List<Sample> thisSampleSpace = this.sampleSpace;

        final Map<RealMatrix, Double> singletons = new HashMap<>(thisSampleSpace.size());
        for (final Sample thisSample : thisSampleSpace) {
            final RealMatrix thisSingleton = new Array2DRowRealMatrix(thisSample.getKey());
            final RealMatrix value = thisSingleton.scalarMultiply(number);
            if (singletons.containsKey(value)) {
                singletons.put(value, singletons.get(value) + (thisSample.getValue()));
            } else {
                singletons.put(value, thisSample.getValue());
            }
        }
        final List<Sample> newSampleSpace = new ArrayList<>(singletons.size());

        for (final Entry<RealMatrix, Double> entry : singletons.entrySet()) {
            newSampleSpace.add(new Sample(entry.getKey().getColumn(0), entry.getValue()));
        }
        return new MultivariateEnumeratedDistribution(newSampleSpace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution divide(final Double number) {
        return this.multiply(1.0 / number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution add(final IMultivariateDistribution other) {
        final List<Sample> thisSampleSpace = this.sampleSpace;
        final List<Sample> otherSampleSpace = ((MultivariateEnumeratedDistribution) other).sampleSpace;

        final Map<RealVector, Double> singletons = new HashMap<>(thisSampleSpace.size() * otherSampleSpace.size());
        for (final Sample thisSample : thisSampleSpace) {
            for (final Sample otherSample : otherSampleSpace) {
                final RealVector thisSingleton = new ArrayRealVector(thisSample.getKey());
                final RealVector otherSingleton = new ArrayRealVector(otherSample.getKey());
                final RealVector value = thisSingleton.add(otherSingleton);
                if (singletons.containsKey(value)) {
                    singletons.put(value, singletons.get(value) + (thisSample.getValue() * otherSample.getValue()));
                } else {
                    singletons.put(value, thisSample.getValue() * otherSample.getValue());
                }
            }
        }
        final List<Sample> newSampleSpace = new ArrayList<>(singletons.size());

        for (final Entry<RealVector, Double> entry : singletons.entrySet()) {
            newSampleSpace.add(new Sample(entry.getKey().toArray(), entry.getValue()));
        }
        return new MultivariateEnumeratedDistribution(newSampleSpace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution subtract(final IMultivariateDistribution other) {
        final List<Sample> thisSampleSpace = this.sampleSpace;
        final List<Sample> otherSampleSpace = ((MultivariateEnumeratedDistribution) other).sampleSpace;

        final Map<RealVector, Double> singletons = new HashMap<>(thisSampleSpace.size() * otherSampleSpace.size());
        for (final Sample thisSample : thisSampleSpace) {
            for (final Sample otherSample : otherSampleSpace) {
                final RealVector thisSingleton = new ArrayRealVector(thisSample.getKey());
                final RealVector otherSingleton = new ArrayRealVector(otherSample.getKey());
                final RealVector value = thisSingleton.subtract(otherSingleton);
                if (singletons.containsKey(value)) {
                    singletons.put(value, singletons.get(value) + (thisSample.getValue() * otherSample.getValue()));
                } else {
                    singletons.put(value, thisSample.getValue() * otherSample.getValue());
                }
            }
        }
        final List<Sample> newSampleSpace = new ArrayList<>(singletons.size());

        for (final Entry<RealVector, Double> entry : singletons.entrySet()) {
            newSampleSpace.add(new Sample(entry.getKey().toArray(), entry.getValue()));
        }
        return new MultivariateEnumeratedDistribution(newSampleSpace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution multiply(final IMultivariateDistribution other) {
        final List<Sample> thisSampleSpace = this.sampleSpace;
        final List<Sample> otherSampleSpace = ((MultivariateEnumeratedDistribution) other).sampleSpace;

        final Map<RealVector, Double> singletons = new HashMap<>(thisSampleSpace.size() * otherSampleSpace.size());
        for (final Sample thisSample : thisSampleSpace) {
            for (final Sample otherSample : otherSampleSpace) {
                final RealVector thisSingleton = new ArrayRealVector(thisSample.getKey());
                final RealVector otherSingleton = new ArrayRealVector(otherSample.getKey());
                final RealVector value = thisSingleton.ebeMultiply(otherSingleton);
                if (singletons.containsKey(value)) {
                    singletons.put(value, singletons.get(value) + (thisSample.getValue() * otherSample.getValue()));
                } else {
                    singletons.put(value, thisSample.getValue() * otherSample.getValue());
                }
            }
        }
        final List<Sample> newSampleSpace = new ArrayList<>(singletons.size());

        for (final Entry<RealVector, Double> entry : singletons.entrySet()) {
            newSampleSpace.add(new Sample(entry.getKey().toArray(), entry.getValue()));
        }
        return new MultivariateEnumeratedDistribution(newSampleSpace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution divide(final IMultivariateDistribution other) {
        final List<Sample> thisSampleSpace = this.sampleSpace;
        final List<Sample> otherSampleSpace = ((MultivariateEnumeratedDistribution) other).sampleSpace;

        final Map<RealVector, Double> singletons = new HashMap<>(thisSampleSpace.size() * otherSampleSpace.size());
        for (final Sample thisSample : thisSampleSpace) {
            for (final Sample otherSample : otherSampleSpace) {
                final RealVector thisSingleton = new ArrayRealVector(thisSample.getKey());
                final RealVector otherSingleton = new ArrayRealVector(otherSample.getKey());
                final RealVector value = thisSingleton.ebeDivide(otherSingleton);
                if (singletons.containsKey(value)) {
                    singletons.put(value, singletons.get(value) + (thisSample.getValue() * otherSample.getValue()));
                } else {
                    singletons.put(value, thisSample.getValue() * otherSample.getValue());
                }
            }
        }
        final List<Sample> newSampleSpace = new ArrayList<>(singletons.size());

        for (final Entry<RealVector, Double> entry : singletons.entrySet()) {
            newSampleSpace.add(new Sample(entry.getKey().toArray(), entry.getValue()));
        }
        return new MultivariateEnumeratedDistribution(newSampleSpace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (final Sample sample : this.sampleSpace) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(Arrays.toString(sample.getKey())).append(",").append(sample.getValue());
        }
        sb.append(")");
        sb.insert(0, "D(");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result);
        if (this.sampleSpace != null) {
            for (final Sample sample : this.sampleSpace) {
                result += Arrays.hashCode(sample.getKey()) + sample.getValue().hashCode();
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MultivariateEnumeratedDistribution other = (MultivariateEnumeratedDistribution) obj;
        if (this.sampleSpace == null) {
            if (other.sampleSpace != null) {
                return false;
            }
        } else if (!this.sampleSpace.equals(other.sampleSpace)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivariateEnumeratedDistribution clone() {
        return new MultivariateEnumeratedDistribution(this);
    }

    public static void main(final String[] args) {
        final double[][] singletons = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        final double[] probabilities = new double[] { 0.25, 0.5, 0.25 };

        final MultivariateEnumeratedDistribution distribution = new MultivariateEnumeratedDistribution(singletons, probabilities);
        final Array2DRowRealMatrix restrictMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 } });
        System.out.println("Distribution: " + distribution);
        System.out.println("Restrict to: " + restrictMatrix);
        distribution.restrict(restrictMatrix);
        System.out.println("Result: " + distribution);

        System.out.println("Add: X+3 -> " + distribution.add(3.0));
        System.out.println("Add: X-3 -> " + distribution.subtract(3.0));
        System.out.println("Add: X*3 -> " + distribution.multiply(3.0));
        System.out.println("Add: X/3 -> " + distribution.divide(3.0));

        System.out.println("Add: X+X -> " + distribution.add(distribution));
        System.out.println("Add: X-X -> " + distribution.subtract(distribution));
        System.out.println("Add: X*X -> " + distribution.multiply(distribution));
        System.out.println("Add: X/X -> " + distribution.divide(distribution));
    }

}
