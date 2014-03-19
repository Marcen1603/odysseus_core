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
    /** Normalized weight of each mixture component. */
    private double[] weight;
    /** Mixture components. */
    private final IMultivariateDistribution[] distribution;
    /** The attribute positions. */
    private int[] attributes;
    /** The scale. */
    private double scale;
    /** The support for each dimension. */
    private Interval[] support;

    public MultivariateMixtureDistribution(double weight, IMultivariateDistribution component) {
        distribution = new IMultivariateDistribution[] { component };
        this.weight = new double[] { weight };
        this.scale = 1.0;
        this.support = new Interval[component.getDimension()];
        attributes = new int[component.getDimension()];
        for (int i = 0; i < component.getDimension(); i++) {
            support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(double[] weights, IMultivariateDistribution[] components) {
        distribution = components;
        this.weight = weights;
        this.scale = 1.0;
        this.support = new Interval[components[0].getDimension()];
        attributes = new int[components[0].getDimension()];
        for (int i = 0; i < distribution[0].getDimension(); i++) {
            support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(double[] weights, List<IMultivariateDistribution> components) {
        distribution = components.toArray(new IMultivariateDistribution[components.size()]);
        this.weight = weights;
        this.scale = 1.0;
        this.support = new Interval[components.get(0).getDimension()];
        attributes = new int[components.get(0).getDimension()];
        for (int i = 0; i < distribution[0].getDimension(); i++) {
            support[i] = Interval.MAX;
        }
    }

    public MultivariateMixtureDistribution(List<Pair<Double, IMultivariateDistribution>> components) {
        distribution = new IMultivariateDistribution[components.size()];
        weight = new double[components.size()];
        this.scale = 1.0;
        for (int i = 0; i < components.size(); i++) {
            final Pair<Double, IMultivariateDistribution> comp = components.get(i);
            weight[i] = comp.getFirst();
            distribution[i] = comp.getSecond();
        }
        this.support = new Interval[distribution[0].getDimension()];
        attributes = new int[distribution[0].getDimension()];
        for (int i = 0; i < distribution[0].getDimension(); i++) {
            support[i] = Interval.MAX;
        }
    }

    /**
     * @param extendedMixtureMultivariateRealDistribution
     */
    public MultivariateMixtureDistribution(MultivariateMixtureDistribution copy) {
        this.weight = MathArrays.copyOf(copy.weight);
        this.scale = copy.scale;
        distribution = new IMultivariateDistribution[copy.distribution.length];
        for (int i = 0; i < copy.distribution.length; i++) {
            distribution[i] = (IMultivariateDistribution) copy.distribution[i].clone();
        }
        support = new Interval[copy.getDimension()];
        attributes = new int[copy.getDimension()];
        for (int i = 0; i < copy.getDimension(); i++) {
            support[i] = copy.support[i].clone();
            attributes[i] = copy.attributes[i];
        }

    }

    /** {@inheritDoc} */
    @Override
    public double density(final double[] a) {
        double p = 0;
        for (int i = 0; i < getDimension(); i++) {
            if (!this.support[i].contains(a[i])) {
                return 0.0;
            }
        }
        for (int i = 0; i < weight.length; i++) {
            p += weight[i] * distribution[i].density(a);
        }
        return p;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final double[] a) {
        double p = 0;
        double[] minA = MathArrays.copyOf(a);
        for (int i = 0; i < getDimension(); i++) {
            if (!this.support[i].contains(a[i])) {
                minA[i] = support[i].sup();
            }
        }
        for (int i = 0; i < weight.length; i++) {
            p += weight[i] * distribution[i].probability(a);
        }
        return p;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final double[] a, final double[] b) {
        double p = 0;
        double[] maxA = MathArrays.copyOf(a);
        double[] minB = MathArrays.copyOf(b);
        for (int i = 0; i < getDimension(); i++) {
            if (!this.support[i].contains(a[i])) {
                maxA[i] = support[i].inf();
            }
            if (!this.support[i].contains(b[i])) {
                minB[i] = support[i].sup();
            }
        }
        for (int i = 0; i < weight.length; i++) {
            p += weight[i] * distribution[i].probability(maxA, minB);
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

        for (int i = 0; i < weight.length; i++) {
            list.add(new Pair<Double, IMultivariateDistribution>(weight[i], distribution[i]));
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getMean() {
        double[] mean = new double[getDimension()];
        for (int i = 0; i < weight.length; i++) {
            mean[i] += weight[i] * distribution[i].getMean()[i];
        }
        return mean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[][] getVariance() {
        // FIXME I'm sure this is totally wrong
        RealMatrix variance = new Array2DRowRealMatrix(new double[getDimension()][getDimension()]);
        for (int i = 0; i < weight.length; i++) {
            variance = variance.add(new Array2DRowRealMatrix(distribution[i].getVariance()).scalarMultiply(weight[i]));
        }
        return variance.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        if (distribution.length > 0) {
            return distribution[0].getDimension();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(RealMatrix restrict) {
        double[] dimensions = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            dimensions[i] = i;
        }
        for (IMultivariateDistribution distribution : this.distribution) {
            distribution.restrict(restrict);
        }
        final RealMatrix dimensionsMatrix = restrict.multiply(MatrixUtils.createRealDiagonalMatrix(dimensions)).multiply(restrict.transpose());
        int[] newDimensions = new int[dimensionsMatrix.getRowDimension()];
        for (int d = 0; d < dimensionsMatrix.getRowDimension(); d++) {
            newDimensions[d] = (int) dimensionsMatrix.getEntry(d, d);
        }
        Interval[] newSupport = new Interval[dimensionsMatrix.getRowDimension()];
        int[] newAttributes = new int[dimensionsMatrix.getRowDimension()];
        for (int i = 0; i < newDimensions.length; i++) {
            newSupport[i] = support[newDimensions[i]];
            newAttributes[i] = attributes[newDimensions[i]];
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
        for (int i = 0; i < attributes.length; i++) {
            if (pos == attributes[i]) {
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
    public void setAttributes(int[] attributes) {
        this.attributes = attributes;

    }

    /**
     * @param support2
     */
    public void setSupport(Interval[] support) {
        this.support = support;

    }

    /**
     * @param d
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * @param i
     * @param attributeIndex
     */
    public void setAttribute(int i, int index) {
        this.attributes[i] = index;

    }

    /**
     * @param i
     * @param sub
     */
    public void setSupport(int i, Interval sub) {
        this.support[i] = sub;

    }

    public MultivariateMixtureDistribution add(Double number) {
        MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].add(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].add(number);
        }
        return mixture;
    }

    public MultivariateMixtureDistribution subtract(Double number) {
        MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].subtract(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].subtract(number);
        }
        return mixture;
    }

    public MultivariateMixtureDistribution divide(Double number) {
        MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].divide(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].divide(number);
        }
        return mixture;
    }

    public MultivariateMixtureDistribution multiply(Double number) {
        MultivariateMixtureDistribution mixture = this.clone();
        for (int i = 0; i < this.distribution.length; i++) {
            mixture.distribution[i] = this.distribution[i].multiply(number);
        }
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].multiply(number);
        }
        return mixture;
    }

    public MultivariateMixtureDistribution add(IMultivariateDistribution other) {
        MultivariateMixtureDistribution o = (MultivariateMixtureDistribution) other;

        IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * o.distribution.length];
        double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < o.distribution.length; j++) {
                components[i * this.distribution.length + j] = this.distribution[i].add(o.distribution[i]);
                weights[i * this.distribution.length + j] = this.weight[i] * o.weight[j];
            }
        }
        MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].add(o.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * o.scale;
        return mixture;
    }

    public MultivariateMixtureDistribution subtract(IMultivariateDistribution other) {
        MultivariateMixtureDistribution o = (MultivariateMixtureDistribution) other;

        IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * o.distribution.length];
        double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < o.distribution.length; j++) {
                components[i * this.distribution.length + j] = this.distribution[i].subtract(o.distribution[i]);
                weights[i * this.distribution.length + j] = this.weight[i] * o.weight[j];
            }
        }
        MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].subtract(o.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * o.scale;
        return mixture;
    }

    public MultivariateMixtureDistribution divide(IMultivariateDistribution other) {
        MultivariateMixtureDistribution o = (MultivariateMixtureDistribution) other;

        IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * o.distribution.length];
        double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < o.distribution.length; j++) {
                components[i * this.distribution.length + j] = this.distribution[i].divide(o.distribution[i]);
                weights[i * this.distribution.length + j] = this.weight[i] * o.weight[j];
            }
        }
        MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].divide(o.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * o.scale;
        return mixture;
    }

    public MultivariateMixtureDistribution multiply(IMultivariateDistribution other) {
        MultivariateMixtureDistribution otherMixture = (MultivariateMixtureDistribution) other;

        IMultivariateDistribution[] components = new IMultivariateDistribution[this.distribution.length * otherMixture.distribution.length];
        double[] weights = new double[components.length];
        for (int i = 0; i < this.distribution.length; i++) {
            for (int j = 0; j < otherMixture.distribution.length; j++) {
                components[i * this.distribution.length + j] = this.distribution[i].multiply(otherMixture.distribution[i]);
                weights[i * this.distribution.length + j] = this.weight[i] * otherMixture.weight[j];
            }
        }
        MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(weights, components);
        for (int i = 0; i < this.getDimension(); i++) {
            mixture.support[i] = this.support[i].multiply(otherMixture.support[i]);
            mixture.attributes[i] = this.attributes[i];
        }
        mixture.scale = this.scale * otherMixture.scale;
        return mixture;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.distribution.length; i++) {
            IMultivariateDistribution distribution = this.distribution[i];
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(distribution.toString()).append(":").append(weight[i]);
        }
        sb.append(Arrays.toString(support)).insert(0, scale);
        sb.append("->").append(Arrays.toString(attributes));
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.distribution);
        long temp;
        temp = Double.doubleToLongBits(this.scale);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(this.support);
        result = prime * result + Arrays.hashCode(this.weight);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MultivariateMixtureDistribution other = (MultivariateMixtureDistribution) obj;
        if (!Arrays.equals(this.distribution, other.distribution)) {
            return false;
        }
        if (Double.doubleToLongBits(this.scale) != Double.doubleToLongBits(other.scale)) {
            return false;
        }
        if (!Arrays.equals(this.support, other.support)) {
            return false;
        }
        if (!Arrays.equals(this.weight, other.weight)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        double[] means1 = new double[] { 1.0, 2.0, 3.0 };
        double[][] covariance1 = new double[][] { { 1.0, 0.5, 0.5 }, { 0.5, 1.0, 0.5 }, { 0.5, 0.5, 1.0 } };
        double[] means2 = new double[] { 4.0, 5.0, 6.0 };
        double[][] covariance2 = new double[][] { { 1.0, 0.25, 0.25 }, { 0.25, 1.0, 0.25 }, { 0.25, 0.25, 1.0 } };

        double[][] singletons = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        double[] probabilities = new double[] { 0.75, 0.25 };

        MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(means1, covariance1);
        MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(means2, covariance2);
        MultivariateEnumeratedDistribution distribution3 = new MultivariateEnumeratedDistribution(singletons, probabilities);

        List<IMultivariateDistribution> distributions = new ArrayList<>();
        distributions.add(distribution1);
        distributions.add(distribution2);
        distributions.add(distribution3);

        MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(new double[] { 0.55, 0.25, 0.2 }, distributions);
        mixture.setSupport(1, new Interval(-3.0, 5.0));
        mixture.setAttributes(new int[] { 2, 0, 3 });

        Array2DRowRealMatrix restrictMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 } });
        System.out.println("Distribution: " + mixture);
        System.out.println("Restrict to: " + restrictMatrix);
        mixture.restrict(restrictMatrix);
        System.out.println("Result: " + mixture);
    }

}
