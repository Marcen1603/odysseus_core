/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ExtendedMixtureMultivariateRealDistribution implements IMultivariateRealDistribution {
    /** Normalized weight of each mixture component. */
    private double[] weight;
    /** Mixture components. */
    private final List<IMultivariateRealDistribution> distribution;
    /** The attribute positions. */
    private int[] attributes;
    /** The scale. */
    private double scale;
    /** The support for each dimension. */
    private Interval[] support;

    public ExtendedMixtureMultivariateRealDistribution(int dimension) {
        distribution = new ArrayList<IMultivariateRealDistribution>();
        this.weight = new double[dimension];
    }

    public ExtendedMixtureMultivariateRealDistribution(double weight, IMultivariateRealDistribution component) {
        distribution = new ArrayList<IMultivariateRealDistribution>(1);
        distribution.add(component);
        this.weight = new double[] { weight };
    }

    public ExtendedMixtureMultivariateRealDistribution(double[] weights, List<IMultivariateRealDistribution> components) {
        distribution = components;
        this.weight = weights;
    }

    public ExtendedMixtureMultivariateRealDistribution(List<Pair<Double, IMultivariateRealDistribution>> components) {
        distribution = new ArrayList<IMultivariateRealDistribution>(components.size());
        weight = new double[components.size()];
        for (int i = 0; i < components.size(); i++) {
            final Pair<Double, IMultivariateRealDistribution> comp = components.get(i);
            weight[i] = comp.getFirst();
            distribution.add(comp.getSecond());
        }
    }

    /**
     * @param extendedMixtureMultivariateRealDistribution
     */
    public ExtendedMixtureMultivariateRealDistribution(ExtendedMixtureMultivariateRealDistribution copy) {
        this.weight = MathArrays.copyOf(copy.weight);
        distribution = new ArrayList<IMultivariateRealDistribution>(copy.distribution.size());
        for (IMultivariateRealDistribution distr : copy.distribution) {
            distribution.add((IMultivariateRealDistribution) distr.clone());
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
            p += weight[i] * distribution.get(i).density(a);
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
            p += weight[i] * distribution.get(i).probability(a);
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
            p += weight[i] * distribution.get(i).probability(maxA, minB);
        }
        return p;
    }

    /** {@inheritDoc} */

    /**
     * Gets the distributions that make up the mixture model.
     * 
     * @return the component distributions and associated weights.
     */
    public List<Pair<Double, IMultivariateRealDistribution>> getComponents() {
        final List<Pair<Double, IMultivariateRealDistribution>> list = new ArrayList<Pair<Double, IMultivariateRealDistribution>>();

        for (int i = 0; i < weight.length; i++) {
            list.add(new Pair<Double, IMultivariateRealDistribution>(weight[i], distribution.get(i)));
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
            mean[i] += weight[i] * distribution.get(i).getMean()[i];
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
            variance = variance.add(new Array2DRowRealMatrix(distribution.get(i).getVariance()).scalarMultiply(weight[i]));
        }
        return variance.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        if (distribution.size() > 0) {
            return distribution.get(0).getDimension();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(RealMatrix restrict) {
        for (IMultivariateRealDistribution distribution : this.distribution) {
            distribution.restrict(restrict);
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMixtureMultivariateRealDistribution clone() {
        return new ExtendedMixtureMultivariateRealDistribution(this);
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

    public void setDistributions(ExtendedMixtureMultivariateRealDistribution distributions) {
        this.distribution.clear();
        for (IMultivariateRealDistribution distribution : distributions.distribution) {
            this.distribution.add((IMultivariateRealDistribution) distribution.clone());
        }
        this.weight = MathArrays.copyOf(distributions.weight);
    }
}
