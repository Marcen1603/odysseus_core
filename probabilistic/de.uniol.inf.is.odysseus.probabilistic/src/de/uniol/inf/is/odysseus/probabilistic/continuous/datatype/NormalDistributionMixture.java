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
package de.uniol.inf.is.odysseus.probabilistic.continuous.datatype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NormalDistributionMixture implements Serializable, Cloneable, IClone {
	/**
     * 
     */
	private static final long serialVersionUID = 8771213908605100590L;
	private int[] attributes;
	private double scale;
	private Interval[] support;
	// FIXME Replace by List<Pair<>> ?
	private final Map<MultivariateNormalDistribution, Double> mixtures = new HashMap<MultivariateNormalDistribution, Double>();

	public NormalDistributionMixture(final double mean, final double[][] covariances) {
		this(new double[] { mean }, covariances);
	}

	public NormalDistributionMixture(final double mean, final double[] covariances) {
		this(new double[] { mean }, CovarianceMatrixUtils.toMatrix(covariances).getData());
	}

	public NormalDistributionMixture(final double[] means, final double[] covariances) {
		this(means, CovarianceMatrixUtils.toMatrix(covariances).getData());
	}

	public NormalDistributionMixture(final int dimension) {
		this.attributes = new int[dimension];
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(final double[] means, final double[][] covariances) {
		final int dimension = means.length;
		this.attributes = new int[dimension];
		this.mixtures.put(new MultivariateNormalDistribution(means, covariances), 1.0);
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(final Map<MultivariateNormalDistribution, Double> mixtures) {
		int dimension = 0;
		for (final Entry<MultivariateNormalDistribution, Double> mixture : mixtures.entrySet()) {
			dimension = mixture.getKey().getMeans().length;
			this.mixtures.put(mixture.getKey(), mixture.getValue());
		}
		this.attributes = new int[dimension];
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(final NormalDistributionMixture normalDistributionMixture) {
		this.attributes = normalDistributionMixture.attributes.clone();
		this.scale = normalDistributionMixture.scale;
		for (final MultivariateNormalDistribution distr : normalDistributionMixture.mixtures.keySet()) {
			final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(distr.getMeans().clone(), distr.getCovariances().copy().getData());
			this.mixtures.put(distribution, normalDistributionMixture.mixtures.get(distr));
		}
		this.support = new Interval[normalDistributionMixture.support.length];
		for (int i = 0; i < normalDistributionMixture.support.length; i++) {
			this.support[i] = normalDistributionMixture.support[i].clone();
		}
	}

	public int getDimension() {
		return this.attributes.length;
	}

	public double getScale() {
		return this.scale;
	}

	public void setScale(final double scale) {
		this.scale = scale;
	}

	public Interval getSupport(final int dimension) {
		return this.support[dimension];
	}

	public Interval[] getSupport() {
		return this.support;
	}

	public void setSupport(final int dimension, final Interval support) {
		this.support[dimension] = this.support[dimension].intersection(support);
	}

	public void setSupport(final Interval[] support) {
		this.support = support;
	}

	public Map<MultivariateNormalDistribution, Double> getMixtures() {
		return this.mixtures;
	}

	public int getAttribute(final int dimension) {
		return this.attributes[dimension];
	}

	public int[] getAttributes() {
		return this.attributes;
	}

	public void setAttributes(final int[] attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(final int dimension, final int attribute) {
		this.attributes[dimension] = attribute;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (final Entry<MultivariateNormalDistribution, Double> mixture : this.mixtures.entrySet()) {
			if (sb.length() > 1) {
				sb.append(";");
			}
			final MultivariateNormalDistribution distribution = mixture.getKey();
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
			sb.append(":").append(mixture.getValue()).append(")");
		}
		sb.append("),[");
		for (final Interval sup : this.support) {
			sb.append(sup);
		}
		sb.append("],").append(this.scale);
		return sb.toString();
	}

	@Override
	public NormalDistributionMixture clone() {
		return new NormalDistributionMixture(this);
	}

}
