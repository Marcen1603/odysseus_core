/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MultivariateMixtureDistribution implements IMultivariateDistribution {
    /**
     * 
     */
    private static final long serialVersionUID = -7963504888038684566L;
    private final RandomGenerator random = new Well19937c();
    /** Normalized weight of each mixture component. */
    private final double[] weight;
    /** Mixture components. */
    private final IMultivariateDistribution[] distribution;
    /** The attribute positions. */
    private int[] attributes;
    /** The scale. */
    private double scale;
    /** The support for each dimension. */
    private Interval[] support;

    public MultivariateMixtureDistribution(final double weight, final IMultivariateDistribution component) {
        this.distribution = new IMultivariateDistribution[] { component };
        this.weight = new double[] { weight };
        this.scale = 1.0;
        this.support = new Interval[component.getDimension()];
        this.attributes = new int[component.getDimension()];
        for (int i = 0; i < component.getDimension(); i++) {
            this.support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(final double[] weights, final IMultivariateDistribution[] components) {
        this.distribution = components;
        this.weight = weights;
        this.scale = 1.0;
        this.support = new Interval[components[0].getDimension()];
        this.attributes = new int[components[0].getDimension()];
        for (int i = 0; i < this.distribution[0].getDimension(); i++) {
            this.support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(final double[] weights, final IMultivariateDistribution[] components, double scale) {
        this.distribution = components;
        this.weight = weights;
        this.scale = scale;
        this.support = new Interval[components[0].getDimension()];
        this.attributes = new int[components[0].getDimension()];
        for (int i = 0; i < this.distribution[0].getDimension(); i++) {
            this.support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(final double[] weights, final IMultivariateDistribution[] components, final Interval[] supports) {
        this.distribution = components;
        this.weight = weights;
        this.scale = 1.0;
        this.support = new Interval[components[0].getDimension()];
        this.attributes = new int[components[0].getDimension()];
        for (int i = 0; i < this.distribution[0].getDimension(); i++) {
            this.support[i] = supports[i];
        }
    }

    public MultivariateMixtureDistribution(final double[] weights, final IMultivariateDistribution[] components, final Interval[] supports, double scale) {
        this.distribution = components;
        this.weight = weights;
        this.scale = scale;
        this.support = new Interval[components[0].getDimension()];
        this.attributes = new int[components[0].getDimension()];
        for (int i = 0; i < this.distribution[0].getDimension(); i++) {
            this.support[i] = supports[i];
        }
    }

    public MultivariateMixtureDistribution(final double[] weights, final List<IMultivariateDistribution> components) {
        this.distribution = components.toArray(new IMultivariateDistribution[components.size()]);
        this.weight = weights;
        this.scale = 1.0;
        this.support = new Interval[components.get(0).getDimension()];
        this.attributes = new int[components.get(0).getDimension()];
        for (int i = 0; i < this.distribution[0].getDimension(); i++) {
            this.support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(final List<Pair<Double, IMultivariateDistribution>> components) {
        this.distribution = new IMultivariateDistribution[components.size()];
        this.weight = new double[components.size()];
        this.scale = 1.0;
        for (int i = 0; i < components.size(); i++) {
            final Pair<Double, IMultivariateDistribution> comp = components.get(i);
            this.weight[i] = comp.getFirst();
            this.distribution[i] = comp.getSecond();
        }
        this.support = new Interval[this.distribution[0].getDimension()];
        this.attributes = new int[this.distribution[0].getDimension()];
        for (int i = 0; i < this.distribution[0].getDimension(); i++) {
            this.support[i] = Interval.MAX;
        }
    }

    /**
     * @param extendedMixtureMultivariateRealDistribution
     */
    public MultivariateMixtureDistribution(final MultivariateMixtureDistribution copy) {
        this.weight = MathArrays.copyOf(copy.weight);
        this.scale = copy.scale;
        this.distribution = new IMultivariateDistribution[copy.distribution.length];
        for (int i = 0; i < copy.distribution.length; i++) {
            this.distribution[i] = copy.distribution[i].clone();
        }
        this.support = new Interval[copy.getDimension()];
        this.attributes = new int[copy.getDimension()];
        for (int i = 0; i < copy.getDimension(); i++) {
            this.support[i] = copy.support[i].clone();
            this.attributes[i] = copy.attributes[i];
        }

    }

    /** {@inheritDoc} */
    @Override
    public double density(final double[] a) {
        double p = 0.0;
        for (int i = 0; i < this.getDimension(); i++) {
            if (!this.support[i].contains(a[i])) {
                return 0.0;
            }
        }
        for (int i = 0; i < this.weight.length; i++) {
            p += this.weight[i] * this.distribution[i].density(a);
        }
        return p * this.scale;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final double[] a) {
        double p = 0;
        final double[] minA = MathArrays.copyOf(a);
        for (int i = 0; i < this.getDimension(); i++) {
            if (!this.support[i].contains(a[i])) {
                minA[i] = this.support[i].sup();
            }
        }
        for (int i = 0; i < this.weight.length; i++) {
            p += this.weight[i] * this.distribution[i].probability(a);
        }
        return p;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final double[] a, final double[] b) {
        double p = 0;
        final double[] maxA = MathArrays.copyOf(a);
        final double[] minB = MathArrays.copyOf(b);
        for (int i = 0; i < this.getDimension(); i++) {
            if (!this.support[i].contains(a[i])) {
                maxA[i] = this.support[i].inf();
            }
            if (!this.support[i].contains(b[i])) {
                minB[i] = this.support[i].sup();
            }
        }
        for (int i = 0; i < this.weight.length; i++) {
            p += this.weight[i] * this.distribution[i].probability(maxA, minB);
        }
        return p;
    }

    /** {@inheritDoc} */

    /**
     * Gets the distributions that make up the mixture model.
     * 
     * @return the component distributions and associated weights.
     */
    public List<Pair<Double, IMultivariateDistribution>> getComponents() {
        final List<Pair<Double, IMultivariateDistribution>> list = new ArrayList<Pair<Double, IMultivariateDistribution>>();

        for (int i = 0; i < this.weight.length; i++) {
            list.add(new Pair<Double, IMultivariateDistribution>(this.weight[i], this.distribution[i]));
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getMean() {
        final double[] mean = new double[this.getDimension()];
        for (int i = 0; i < this.getDimension(); i++) {
            for (int j = 0; j < this.weight.length; j++) {
                mean[i] += this.weight[j] * this.distribution[j].getMean()[i];
            }
        }
        return mean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[][] getVariance() {
        // FIXME 20140319 christian@kuka.cc I'm sure this is totally wrong
        RealMatrix variance = new Array2DRowRealMatrix(new double[this.getDimension()][this.getDimension()]);
        for (int i = 0; i < this.weight.length; i++) {
            variance = variance.add(new Array2DRowRealMatrix(this.distribution[i].getVariance()).scalarMultiply(this.weight[i]));
        }
        return variance.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        if (this.distribution.length > 0) {
            return this.distribution[0].getDimension();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(final RealMatrix restrict) {
        final double[] dimensions = new double[this.getDimension()];
        for (int i = 0; i < this.getDimension(); i++) {
            dimensions[i] = i;
        }
        for (final IMultivariateDistribution distribution : this.distribution) {
            distribution.restrict(restrict);
        }
        final RealMatrix dimensionsMatrix = restrict.multiply(MatrixUtils.createRealDiagonalMatrix(dimensions)).multiply(restrict.transpose());
        final int[] newDimensions = new int[dimensionsMatrix.getRowDimension()];
        for (int d = 0; d < dimensionsMatrix.getRowDimension(); d++) {
            newDimensions[d] = (int) dimensionsMatrix.getEntry(d, d);
        }
        final Interval[] newSupport = new Interval[dimensionsMatrix.getRowDimension()];
        final int[] newAttributes = new int[dimensionsMatrix.getRowDimension()];
        for (int i = 0; i < newDimensions.length; i++) {
            newSupport[i] = this.support[newDimensions[i]];
            newAttributes[i] = this.attributes[newDimensions[i]];
        }
        this.support = newSupport;
        this.attributes = newAttributes;
    }

    /**
     * Gets the value of the scale property.
     * 
     * @return the scale
     */
    public final double getScale() {
        return this.scale;
    }

    /**
     * Gets the value of the support property in the given dimension.
     * 
     * @param dimension
     *            the dimension
     * @return the support
     */
    public final Interval getSupport(final int dimension) {
        Preconditions.checkPositionIndex(dimension, this.support.length);
        return this.support[dimension];
    }

    /**
     * Gets the value of the support property.
     * 
     * @return the support
     */
    public final Interval[] getSupport() {
        return this.support;
    }

    /**
     * Gets the value of the attributes property in the given dimension.
     * 
     * @param dimension
     *            the dimension
     * @return the attributes
     */
    public final int getAttribute(final int dimension) {
        Preconditions.checkPositionIndex(dimension, this.attributes.length);
        return this.attributes[dimension];
    }

    /**
     * Gets the value of the attributes property.
     * 
     * @return the attributes
     */
    public final int[] getAttributes() {
        return this.attributes;
    }

    public final int getDimension(final int pos) {
        for (int i = 0; i < this.attributes.length; i++) {
            if (pos == this.attributes[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivariateMixtureDistribution clone() {
        return new MultivariateMixtureDistribution(this);
    }

    /**
     * @param newDistributionAttributes
     */
    public void setAttributes(final int[] attributes) {
        this.attributes = attributes.clone();

    }

    /**
     * @param support2
     */
    public void setSupport(final Interval[] support) {
        this.support = support.clone();

    }

    /**
     * @param d
     */
    public void setScale(final double scale) {
        this.scale = scale;
    }

    /**
     * @param i
     * @param attributeIndex
     */
    public void setAttribute(final int i, final int index) {
        this.attributes[i] = index;

    }

    /**
     * @param i
     * @param sub
     */
    public void setSupport(final int i, final Interval sub) {
        this.support[i] = sub;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return this.distribution.length;
    }

    @Override
    public MultivariateMixtureDistribution add(final Double number) {
        final MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].add(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].add(number);
        }
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution subtract(final Double number) {
        final MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].subtract(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].subtract(number);
        }
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution divide(final Double number) {
        final MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].divide(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].divide(number);
        }
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution multiply(final Double number) {
        final MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].multiply(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].multiply(number);
        }
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution add(final IMultivariateDistribution other) {
        final MultivariateMixtureDistribution o = (MultivariateMixtureDistribution) other;

        final IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * o.distribution.length];
        final double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < o.distribution.length; j++) {
                components[(i * this.distribution.length) + j] = this.distribution[i].add(o.distribution[i]);
                weights[(i * this.distribution.length) + j] = this.weight[i] * o.weight[j];
            }
        }
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].add(o.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * o.scale;
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution subtract(final IMultivariateDistribution other) {
        final MultivariateMixtureDistribution o = (MultivariateMixtureDistribution) other;

        final IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * o.distribution.length];
        final double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < o.distribution.length; j++) {
                components[(i * this.distribution.length) + j] = this.distribution[i].subtract(o.distribution[i]);
                weights[(i * this.distribution.length) + j] = this.weight[i] * o.weight[j];
            }
        }
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].subtract(o.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * o.scale;
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution divide(final IMultivariateDistribution other) {
        final MultivariateMixtureDistribution o = (MultivariateMixtureDistribution) other;

        final IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * o.distribution.length];
        final double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < o.distribution.length; j++) {
                components[(i * this.distribution.length) + j] = this.distribution[i].divide(o.distribution[i]);
                weights[(i * this.distribution.length) + j] = this.weight[i] * o.weight[j];
            }
        }
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            Interval[] s = this.support[i].divide(o.support[i]);
            if (s.length==1) {
            mixture.support[i] = s[0];
            }else {
                // FIXME we are loosing interval information here. cku 20171231
                mixture.support[i] = s[0].union(s[1]);
            }
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * o.scale;
        return mixture;
    }

    @Override
    public MultivariateMixtureDistribution multiply(final IMultivariateDistribution other) {
        final MultivariateMixtureDistribution otherMixture = (MultivariateMixtureDistribution) other;

        final IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * otherMixture.distribution.length];
        final double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < otherMixture.distribution.length; j++) {
                components[(i * this.distribution.length) + j] = this.distribution[i].multiply(otherMixture.distribution[i]);
                weights[(i * this.distribution.length) + j] = this.weight[i] * otherMixture.weight[j];
            }
        }
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].multiply(otherMixture.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * otherMixture.scale;
        return mixture;
    }

    @Override
    public double[] sample() {
        double[] point = new double[getDimension()];
        final double randomValue = random.nextDouble();
        double sum = 0.0;

        for (int d = 0; d < distribution.length; d++) {
            sum += weight[d];
            if (randomValue <= sum) {
                point = distribution[d].sample();
                break;
            }
        }
        if (point == null) {
            point = distribution[weight.length - 1].sample();
        }
        return point;
    }

    public double[][] sample(int n) {
        double[][] points = new double[n][getDimension()];
        for (int i = 0; i < n; i++) {
            points[i] = sample();
        }
        return points;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.distribution.length; i++) {
            final IMultivariateDistribution distribution = this.distribution[i];
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(distribution.toString()).append(":").append(this.weight[i]);
        }
        sb.append(Arrays.toString(this.support)).insert(0, this.scale);
        sb.append("->").append(Arrays.toString(this.attributes));
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + Arrays.hashCode(this.distribution);
        long temp;
        temp = Double.doubleToLongBits(this.scale);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        result = (prime * result) + Arrays.hashCode(this.support);
        result = (prime * result) + Arrays.hashCode(this.weight);
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
        final MultivariateMixtureDistribution other = (MultivariateMixtureDistribution) obj;
        if (Double.doubleToLongBits(this.scale) != Double.doubleToLongBits(other.scale)) {
            return false;
        }
        if (!Arrays.equals(this.support, other.support)) {
            return false;
        }
        if (!Arrays.equals(this.weight, other.weight)) {
            return false;
        }
        return (new HashSet<>(Arrays.asList(this.distribution)).equals(new HashSet<>(Arrays.asList(other.distribution))));
    }

    public static void main(final String[] args) {
        final double[] means1 = new double[] { 1.0, 2.0, 3.0 };
        final double[][] covariance1 = new double[][] { { 1.0, 0.5, 0.5 }, { 0.5, 1.0, 0.5 }, { 0.5, 0.5, 1.0 } };
        final double[] means2 = new double[] { 4.0, 5.0, 6.0 };
        final double[][] covariance2 = new double[][] { { 1.0, 0.25, 0.25 }, { 0.25, 1.0, 0.25 }, { 0.25, 0.25, 1.0 } };

        final double[][] singletons = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        final double[] probabilities = new double[] { 0.75, 0.25 };

        final MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(means1, covariance1);
        final MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(means2, covariance2);
        final MultivariateEnumeratedDistribution distribution3 = new MultivariateEnumeratedDistribution(singletons, probabilities);

        final List<IMultivariateDistribution> distributions = new ArrayList<>();
        distributions.add(distribution1);
        distributions.add(distribution2);
        distributions.add(distribution3);

        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(new double[] { 0.55, 0.25, 0.2 }, distributions);
        mixture.setSupport(1, new Interval(-3.0, 5.0));
        mixture.setAttributes(new int[] { 2, 0, 3 });

        final Array2DRowRealMatrix restrictMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 } });
        System.out.println("Distribution: " + mixture);
        System.out.println("Restrict to: " + restrictMatrix);
        mixture.restrict(restrictMatrix);
        System.out.println("Result: " + mixture);
    }

}
