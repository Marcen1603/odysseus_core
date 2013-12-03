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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.ProbabilisticConstants;
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
		Preconditions.checkArgument(dimension > 0);
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
		Objects.requireNonNull(means);
		Objects.requireNonNull(covariance);
		Preconditions.checkArgument(means.length > 0);
		Preconditions.checkArgument(covariance.length > 0);
		Preconditions.checkArgument(covariance[0].length > 0);
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
		Objects.requireNonNull(mixtures);
		Preconditions.checkArgument(mixtures.size() > 0);
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
		Objects.requireNonNull(normalDistributionMixture);
		this.attributes = normalDistributionMixture.attributes.clone();
		this.scale = normalDistributionMixture.scale;
		final List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		for (final Pair<Double, MultivariateNormalDistribution> entry : normalDistributionMixture.mixtures.getComponents()) {
			final MultivariateNormalDistribution mixture = entry.getValue();
			final MultivariateNormalDistribution component = new MultivariateNormalDistribution(mixture.getMeans().clone(), mixture.getCovariances().copy().getData());
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
		Preconditions.checkArgument(scale >= 0.0);
		Preconditions.checkArgument(!Double.isNaN(scale));
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
		Preconditions.checkPositionIndex(dimension, this.support.length);
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
		Objects.requireNonNull(dimensionSupport);
		Preconditions.checkPositionIndex(dimension, this.support.length);
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
		Objects.requireNonNull(support);
		Preconditions.checkArgument(support.length > 0);
		Preconditions.checkArgument(support.length == this.attributes.length);
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
	public final void setMixtures(final MixtureMultivariateNormalDistribution mixtures) {
		Objects.requireNonNull(mixtures);
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
	 * Sets the value of the attributes property.
	 * 
	 * @param attributes
	 *            the attributes to set
	 */
	public final void setAttributes(final int[] attributes) {
		Objects.requireNonNull(attributes);
		Preconditions.checkArgument(attributes.length > 0);
		Preconditions.checkArgument(attributes.length == this.support.length);
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
		Preconditions.checkPositionIndex(dimension, this.attributes.length);
		Preconditions.checkArgument(attribute >= 0);
		this.attributes[dimension] = attribute;
	}

	/**
	 * Writes the object to the given output stream.
	 * 
	 * @param out
	 *            The object output stream
	 * @throws IOException
	 *             Exception during write
	 */
	private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
		out.writeInt(this.mixtures.getComponents().size());
		out.writeInt(this.mixtures.getDimension());

		for (final Pair<Double, MultivariateNormalDistribution> entry : this.mixtures.getComponents()) {
			out.writeDouble(entry.getKey());
			final double[] mean = entry.getValue().getMeans();
			for (final double element : mean) {
				out.writeDouble(element);
			}
			final double[] entries = CovarianceMatrixUtils.fromMatrix(entry.getValue().getCovariances());
			for (final double entrie : entries) {
				out.writeDouble(entrie);
			}
		}
		out.writeDouble(this.scale);

		for (final Interval element : this.support) {
			out.writeDouble(element.inf());
			out.writeDouble(element.sup());
		}
		for (final int pos : this.attributes) {
			out.write(pos);
		}
	}

	/**
	 * Reads the object from the given input stream.
	 * 
	 * @param in
	 *            The object input stream
	 * @throws IOException
	 *             Exception during read
	 */
	private void readObject(final java.io.ObjectInputStream in) throws IOException {
		final int size = in.readInt();
		final List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<Pair<Double, MultivariateNormalDistribution>>();
		final int dimension = in.readInt();
		for (int m = 0; m < size; m++) {
			final double weight = in.readDouble();
			final double[] mean = new double[dimension];
			for (int i = 0; i < mean.length; i++) {
				mean[i] = in.readDouble();
			}
			final double[] entries = new double[dimension];
			for (int i = 0; i < entries.length; i++) {
				entries[i] = in.readDouble();
			}

			final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, CovarianceMatrixUtils.toMatrix(entries).getData());
			components.add(new Pair<Double, MultivariateNormalDistribution>(weight, distribution));
		}
		this.scale = in.readDouble();
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(in.readDouble(), in.readDouble());
		}
		this.mixtures = new MixtureMultivariateNormalDistribution(components);
		this.attributes = new int[dimension];
		for (int i = 0; i < this.attributes.length; i++) {
			this.attributes[i] = in.readInt();
		}
	}

	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		int mixturePos = 0;
		for (final Pair<Double, MultivariateNormalDistribution> mixture : this.mixtures.getComponents()) {
			if (mixturePos > 1) {
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
			mixturePos++;
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

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (this.mixtures != null) {
			result += this.mixtures.hashCode();
		}
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
		final NormalDistributionMixture other = (NormalDistributionMixture) obj;
		if (Double.doubleToLongBits(this.scale) != Double.doubleToLongBits(other.scale)) {
			return false;
		}
		if (!Arrays.equals(this.support, other.support)) {
			return false;
		}
		if (this.mixtures == null) {
			if (other.mixtures != null) {
				return false;
			}
		} else {
			boolean foundCompoenent = false;
			for (final Pair<Double, MultivariateNormalDistribution> thisMixture : this.mixtures.getComponents()) {
				final Double thisWeights = thisMixture.getKey();
				final double[] thisMean = thisMixture.getValue().getMeans();
				final RealMatrix thisCovariance = thisMixture.getValue().getCovariances();
				for (final Pair<Double, MultivariateNormalDistribution> otherMixture : other.getMixtures().getComponents()) {
					final Double otherWeights = otherMixture.getKey();
					final double[] otherMean = otherMixture.getValue().getMeans();
					final RealMatrix otherCovariance = otherMixture.getValue().getCovariances();
					if ((Math.abs(thisWeights - otherWeights) <= ProbabilisticConstants.EPSILON) && (Arrays.equals(thisMean, otherMean)) && (Arrays.equals(CovarianceMatrixUtils.fromMatrix(thisCovariance), CovarianceMatrixUtils.fromMatrix(otherCovariance)))) {
						foundCompoenent = true;
					}
				}
				if (!foundCompoenent) {
					return false;
				}
			}
			for (final Pair<Double, MultivariateNormalDistribution> otherMixture : this.mixtures.getComponents()) {
				final Double otherWeights = otherMixture.getKey();
				final double[] otherMean = otherMixture.getValue().getMeans();
				final RealMatrix otherCovariance = otherMixture.getValue().getCovariances();
				for (final Pair<Double, MultivariateNormalDistribution> thisMixture : other.getMixtures().getComponents()) {
					final Double thisWeights = thisMixture.getKey();
					final double[] thisMean = thisMixture.getValue().getMeans();
					final RealMatrix thisCovariance = thisMixture.getValue().getCovariances();
					if ((Math.abs(thisWeights - otherWeights) <= ProbabilisticConstants.EPSILON) && (Arrays.equals(thisMean, otherMean)) && (Arrays.equals(CovarianceMatrixUtils.fromMatrix(thisCovariance), CovarianceMatrixUtils.fromMatrix(otherCovariance)))) {
						foundCompoenent = true;
					}
				}
				if (!foundCompoenent) {
					return false;
				}
			}
		}
		return true;
	}
}
