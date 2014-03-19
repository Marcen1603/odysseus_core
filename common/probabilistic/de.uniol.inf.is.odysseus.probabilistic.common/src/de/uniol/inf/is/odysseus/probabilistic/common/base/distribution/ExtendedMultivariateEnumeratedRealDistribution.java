/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ExtendedMultivariateEnumeratedRealDistribution implements IMultivariateRealDistribution {

    private List<Pair<double[], Double>> sampleSpace;

    /**
     * 
     * @param rng
     * @param singletons
     * @param probabilities
     */
    public ExtendedMultivariateEnumeratedRealDistribution(final double[][] singletons, final double[] probabilities) {
        sampleSpace = new ArrayList<Pair<double[], Double>>(singletons.length);
        for (int i = 0; i < singletons.length; i++) {
            sampleSpace.add(new Pair<double[], Double>(singletons[i], probabilities[i]));
        }

    }

    /**
     * @param extendedMultivariateEnumeratedRealDistribution
     */
    public ExtendedMultivariateEnumeratedRealDistribution(ExtendedMultivariateEnumeratedRealDistribution copy) {
        sampleSpace = new ArrayList<Pair<double[], Double>>(copy.sampleSpace.size());
        for (Pair<double[], Double> sample : copy.sampleSpace) {
            sampleSpace.add(new Pair<double[], Double>(MathArrays.copyOf(sample.getKey()), sample.getValue()));
        }

    }

    @Override
    public double probability(final double[] a) {
        double probability = 0;

        for (final Pair<double[], Double> sample : sampleSpace) {
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
            for (final Pair<double[], Double> sample : sampleSpace) {
                if ((a == null && sample.getKey() == null) || (a != null && Arrays.equals(a, sample.getKey()))) {
                    probability += sample.getValue();
                }
            }
        }
        else {
            for (final Pair<double[], Double> sample : sampleSpace) {
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

    public int getDimension() {
        return 0;
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
    public double[] getMean() {
        double[] mean = new double[getDimension()];

        for (final Pair<double[], Double> sample : sampleSpace) {
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
        double[] mean = new double[getDimension()];
        double[] meanOfSquares = new double[getDimension()];
        double[][] result = new double[getDimension()][getDimension()];
        for (final Pair<double[], Double> sample : sampleSpace) {
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
        for (final Pair<double[], Double> sample : sampleSpace) {
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
        for (final Pair<double[], Double> sample : sampleSpace) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(RealMatrix restrict) {
        ArrayList<Pair<double[], Double>> newSampleSpace = new ArrayList<Pair<double[], Double>>(sampleSpace.size());
        for (Pair<double[], Double> sample : sampleSpace) {
            final RealMatrix singletonMatrix = restrict.multiply(MatrixUtils.createRealDiagonalMatrix(sample.getKey())).multiply(restrict.transpose());
            final double[] singleton = new double[singletonMatrix.getRowDimension()];

            for (int d = 0; d < singletonMatrix.getRowDimension(); d++) {
                singleton[d] = singletonMatrix.getEntry(d, d);
            }
            newSampleSpace.add(new Pair<double[], Double>(singleton, sample.getValue()));
        }
        this.sampleSpace = newSampleSpace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Pair<double[], Double> sample : sampleSpace) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(Arrays.toString(sample.getKey())).append(":").append(sample.getValue());
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMultivariateEnumeratedRealDistribution clone() {
        return new ExtendedMultivariateEnumeratedRealDistribution(this);
    }

    public static void main(String[] args) {
        double[][] singletons = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        double[] probabilities = new double[] { 0.25, 0.5, 0.25 };

        ExtendedMultivariateEnumeratedRealDistribution distribution = new ExtendedMultivariateEnumeratedRealDistribution(singletons, probabilities);
        Array2DRowRealMatrix restrictMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 } });
        System.out.println("Distribution: " + distribution);
        System.out.println("Restrict to: " + restrictMatrix);
        distribution.restrict(restrictMatrix);
        System.out.println("Result: " + distribution);
    }
}
