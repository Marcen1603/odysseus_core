/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base;

import java.io.Serializable;
import java.util.Arrays;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractDistributionMixture implements Serializable, Cloneable, IClone {
    /**
     * 
     */
    private static final long serialVersionUID = 4343555572985838291L;
    /** The attribute positions. */
    private int[] attributes;
    /** The scale. */
    private double scale;
    /** The support for each dimension. */
    private Interval[] support;

    /**
     * 
     * @param dimension
     *            The dimension
     */
    public AbstractDistributionMixture(final int dimension) {
        Preconditions.checkArgument(dimension > 0);
        this.attributes = new int[dimension];
        this.scale = 1.0;
        this.support = new Interval[dimension];
        for (int i = 0; i < this.support.length; i++) {
            this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
    }

    /**
     * @param copy
     */
    public AbstractDistributionMixture(AbstractDistributionMixture copy) {
        this.attributes = new int[copy.attributes.length];
        System.arraycopy(copy.attributes, 0, this.attributes, 0, copy.attributes.length);
        this.scale = copy.scale;
        this.support = new Interval[copy.attributes.length];
        for (int i = 0; i < copy.support.length; i++) {
            this.support[i] = copy.support[i].clone();
        }
    }

    /**
     * Gets the dimension of this distribution mixture.
     * 
     * @return The dimension
     */
    public final int getDimension() {
        return this.attributes.length;
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

    public abstract double probability(double[] x1, double[] x2);

    public abstract double probability(double[] x);

    public abstract double[] sample();

    public abstract AbstractDistributionMixture plus(AbstractDistributionMixture other);

    public abstract AbstractDistributionMixture minus(AbstractDistributionMixture other);

    public abstract AbstractDistributionMixture divide(AbstractDistributionMixture other);

    public abstract AbstractDistributionMixture multiply(AbstractDistributionMixture other);

    /*
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (final Interval sup : this.support) {
            sb.append(sup);
        }
        sb.append("],").append(this.scale);
        return sb.toString();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime;
        long temp;
        temp = Double.doubleToLongBits(this.scale);
        result = (prime * result) + (int) (temp ^ (temp >>> 32));
        result = (prime * result) + Arrays.hashCode(this.support);
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AbstractDistributionMixture other = (AbstractDistributionMixture) obj;
        if (Double.doubleToLongBits(this.scale) != Double.doubleToLongBits(other.scale)) {
            return false;
        }
        if (!Arrays.equals(this.support, other.support)) {
            return false;
        }
        return true;
    }

    /*
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public abstract AbstractDistributionMixture clone();
}
