/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.continuous.datatype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class NormalDistributionMixture implements Serializable, Cloneable, IClone {
	/**
     * 
     */
	private static final long serialVersionUID = 8771213908605100590L;
	/** The attribute positions. */
	private int[] attributes;
	/** The scale. */
	private double scale;
	/** The support for each dimension. */
	private Interval[] support;
	/** The weighted mixtures. */
	// FIXME Possible Bug, mixtures not serializable
	private MixtureMultivariateNormalDistribution mixtures;

	// private final Map<MultivariateNormalDistribution, Double> mixtures = new HashMap<MultivariateNormalDistribution, Double>();

	/**
	 * 
	 * @param mean
	 *            The mean
	 * @param covariance
	 *            The covariance matrix
	 */
	public NormalDistributionMixture(final double mean, final double[][] covariance) {
		this(new double[] { mean }, covariance);
	}

	/**
	 * 
	 * @param mean
	 *            The mean
	 * @param covariance
	 *            The triangle covariance matrix
	 */
	public NormalDistributionMixture(final double mean, final double[] covariance) {
		this(new double[] { mean }, CovarianceMatrixUtils.toMatrix(covariance).getData());
	}

	/**
	 * 
	 * @param means
	 *            The means
	 * @param covariance
	 *            The triangle covariance matrix
	 */
	public NormalDistributionMixture(final double[] means, final double[] covariance) {
		this(means, CovarianceMatrixUtils.toMatrix(covariance).getData());
	}

	/**
	 * 
	 * @param dimension
	 *            The dimension
	 */
	public NormalDistributionMixture(final int dimension) {
		this.attributes = new int[dimension];
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}

	/**
	 * 
	 * @param means
	 *            The means
	 * @param covariance
	 *            The covariance
	 */
	public NormalDistributionMixture(final double[] means, final double[][] covariance) {
		final int dimension = means.length;
		this.attributes = new int[dimension];
		// this.mixtures.put(new MultivariateNormalDistribution(means, covariance), 1.0);
		this.mixtures = new MixtureMultivariateNormalDistribution(new double[] { 1.0 }, new double[][] { means }, new double[][][] { covariance });
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}

	/**
	 * 
	 * @param mixtures
	 *            The mixtures
	 */
	public NormalDistributionMixture(final List<Pair<Double, MultivariateNormalDistribution>> mixtures) {
		int dimension = 0;
		for (final Pair<Double, MultivariateNormalDistribution> entry : mixtures) {
			dimension = entry.getValue().getMeans().length;
			// this.mixtures.put(mixture.getKey(), mixture.getValue());
		}
		this.mixtures = new MixtureMultivariateNormalDistribution(mixtures);
		this.attributes = new int[dimension];
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}

	/**
	 * Clone constructor.
	 * 
	 * @param normalDistributionMixture
	 *            The clone
	 */
	public NormalDistributionMixture(final NormalDistributionMixture normalDistributionMixture) {
		this.attributes = normalDistributionMixture.attributes.clone();
		this.scale = normalDistributionMixture.scale;
		final List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		for (final Pair<Double, MultivariateNormalDistribution> entry : normalDistributionMixture.mixtures.getComponents()) {
			final MultivariateNormalDistribution mixture = entry.getValue();
			MultivariateNormalDistribution component = new MultivariateNormalDistribution(mixture.getMeans().clone(), mixture.getCovariances().copy().getData());
			mvns.add(new Pair<Double, MultivariateNormalDistribution>(entry.getKey(), component));
		}
		this.mixtures = new MixtureMultivariateNormalDistribution(mvns);
		this.support = new Interval[normalDistributionMixture.support.length];
		for (int i = 0; i < normalDistributionMixture.support.length; i++) {
			this.support[i] = normalDistributionMixture.support[i].clone();
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
	 * Sets the value of the scale property.
	 * 
	 * @param scale
	 *            the scale to set
	 */
	public final void setScale(final double scale) {
		this.scale = scale;
	}

	/**
	 * Gets the value of the support property in the given dimension.
	 * 
	 * @param dimension
	 *            the dimension
	 * @return the support
	 */
	public final Interval getSupport(final int dimension) {
		return this.support[dimension];
	}

	/**
	 * Sets the value of the support in the given dimension.
	 * 
	 * @param dimension
	 *            the dimension
	 * @param dimensionSupport
	 *            the support to set
	 */
	public final void setSupport(final int dimension, final Interval dimensionSupport) {
		this.support[dimension] = this.support[dimension].intersection(dimensionSupport);
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
	 * Sets the value of the support property.
	 * 
	 * @param support
	 *            the support to set
	 */
	public final void setSupport(final Interval[] support) {
		this.support = support;
	}

	/**
	 * Gets the value of the mixtures property.
	 * 
	 * @return the mixtures
	 */
	public final MixtureMultivariateNormalDistribution getMixtures() {
		return this.mixtures;
	}

	/**
	 * Sets the value of the mixtures property.
	 * 
	 * @param mixtures
	 *            The normal distribution mixtures
	 */
	public void setMixtures(MixtureMultivariateNormalDistribution mixtures) {
		this.mixtures = mixtures;
	}

	/**
	 * Gets the value of the attributes property in the given dimension.
	 * 
	 * @param dimension
	 *            the dimension
	 * @return the attributes
	 */
	public final int getAttribute(final int dimension) {
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
	 * Sets the value of the attributes property.
	 * 
	 * @param attributes
	 *            the attributes to set
	 */
	public final void setAttributes(final int[] attributes) {
		this.attributes = attributes;
	}

	/**
	 * Sets the value of the attributes in the given dimension.
	 * 
	 * @param dimension
	 *            the dimension
	 * @param attribute
	 *            the attribute to set
	 */
	public final void setAttribute(final int dimension, final int attribute) {
		this.attributes[dimension] = attribute;
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (final Pair<Double, MultivariateNormalDistribution> mixture : this.mixtures.getComponents()) {
			if (sb.length() > 1) {
				sb.append(";");
			}
			final MultivariateNormalDistribution distribution = mixture.getValue();
			sb.append("𝒩({");
			for (int i = 0; i < distribution.getMeans().length; i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(distribution.getMeans()[i]);
			}
			sb.append("},{");
			final RealMatrix covariances = distribution.getCovariances();
			for (int i = 0; i < covariances.getColumnDimension(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append("{");
				for (int j = 0; j < covariances.getRowDimension(); j++) {
					if (j > 0) {
						sb.append(",");
					}
					sb.append(covariances.getEntry(j, i));
				}
				sb.append("}");
			}
			sb.append("}");
			sb.append(":").append(mixture.getKey()).append(")");
		}
		sb.append("),[");
		for (final Interval sup : this.support) {
			sb.append(sup);
		}
		sb.append("],").append(this.scale);
		return sb.toString();
	}

	/*
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final NormalDistributionMixture clone() {
		return new NormalDistributionMixture(this);
	}

}
