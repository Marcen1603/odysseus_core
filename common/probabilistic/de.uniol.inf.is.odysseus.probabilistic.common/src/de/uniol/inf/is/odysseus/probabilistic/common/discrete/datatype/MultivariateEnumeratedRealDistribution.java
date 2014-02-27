/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.AbstractMultivariateRealDistribution;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.probabilistic.common.base.IMultivariateRealDistribution;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MultivariateEnumeratedRealDistribution extends AbstractMultivariateRealDistribution implements IMultivariateRealDistribution {
    protected final EnumeratedDistribution<double[]> innerDistribution;

    /**
     * 
     * @param singletons
     * @param probabilities
     */
    public MultivariateEnumeratedRealDistribution(final double[][] singletons, final double[] probabilities) {
        this(new Well19937c(), singletons, probabilities);
    }

    /**
     * 
     * @param rng
     * @param singletons
     * @param probabilities
     */
    public MultivariateEnumeratedRealDistribution(RandomGenerator rng, final double[][] singletons, final double[] probabilities) {
        super(rng, singletons.length);
        if (singletons[0].length != probabilities.length) {
            // throw new DimensionMismatchException(probabilities.length,
            // singletons.length);
        }

        List<Pair<double[], Double>> samples = new ArrayList<Pair<double[], Double>>(singletons.length);

        for (int i = 0; i < singletons.length; i++) {
            samples.add(new Pair<double[], Double>(singletons[i], probabilities[i]));
        }

        innerDistribution = new EnumeratedDistribution<double[]>(rng, samples);
    }

    @Override
	public double probability(final double[] a) {
        double probability = 0;

        for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
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
            for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
                if ((a == null && sample.getKey() == null) || (a != null && a.equals(sample.getKey()))) {
                    probability += sample.getValue();
                }
            }
        }
        else {
            for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
                boolean inBound = true;
                for (int d = 0; d < a.length; d++) {
                    if ((sample.getKey()[d] <= a[d]) && (sample.getKey()[d] > b[d])) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] sample() {
        return innerDistribution.sample();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double density(double[] x) {
        // Call PMF of the discrete distribution
        return probability(x, x);
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@code sum(singletons[i] * probabilities[i])}
     */
    @Override
	public double[] getNumericalMean() {
        double[] mean = new double[getDimension()];

        for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
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
	public double[][] getNumericalVariance() {
        double[] mean = new double[getDimension()];
        double[] meanOfSquares = new double[getDimension()];
        double[][] result = new double[getDimension()][getDimension()];
        for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
            for (int i = 0; i < sample.getKey().length; i++) {
                mean[i] += sample.getValue() * sample.getKey()[i];
                meanOfSquares[i] += sample.getValue() * sample.getKey()[i] * sample.getKey()[i];
            }
        }
        for (int i = 0; i < getDimension(); i++) {
            result[i][i] = meanOfSquares[i] - mean[i] * mean[i];
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
        double[] min = new double[getDimension()];
        Arrays.fill(min, Double.POSITIVE_INFINITY);
        for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
            for (int i = 0; i < sample.getKey().length; i++) {
                if (sample.getKey()[i] < min[i] && sample.getValue() > 0) {
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
        double[] max = new double[getDimension()];
        Arrays.fill(max, Double.NEGATIVE_INFINITY);
        for (final Pair<double[], Double> sample : innerDistribution.getPmf()) {
            for (int i = 0; i < sample.getKey().length; i++) {
                if (sample.getKey()[i] > max[i] && sample.getValue() > 0) {
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

}
