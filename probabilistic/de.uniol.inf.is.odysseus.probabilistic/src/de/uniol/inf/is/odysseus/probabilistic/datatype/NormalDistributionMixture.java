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
package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NormalDistributionMixture implements Serializable, Cloneable {
	/**
     * 
     */
	private static final long serialVersionUID = 8771213908605100590L;
	private int[] attributes;
	private double scale;
	private Interval[] support;
	private final Map<NormalDistribution, Double> mixtures = new HashMap<NormalDistribution, Double>();

	public NormalDistributionMixture(final double mean,
			final CovarianceMatrix covarianceMatrix) {
		this(new double[] { mean }, covarianceMatrix);
	}

	public NormalDistributionMixture(final double[] mean,
			final CovarianceMatrix covarianceMatrix) {
		final int dimension = mean.length;
		this.attributes = new int[dimension];
		this.mixtures.put(new NormalDistribution(mean, covarianceMatrix), 1.0);
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(
			final Map<NormalDistribution, Double> mixtures) {
		int dimension = 0;
		for (final Entry<NormalDistribution, Double> mixture : mixtures
				.entrySet()) {
			dimension = mixture.getKey().getMean().length;
			this.mixtures.put(mixture.getKey(), mixture.getValue());
		}
		this.attributes = new int[dimension];
		this.scale = 1.0;
		this.support = new Interval[dimension];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}
	}

	public NormalDistributionMixture(
			final NormalDistributionMixture normalDistributionMixture) {
		this.attributes = normalDistributionMixture.attributes.clone();
		this.scale = normalDistributionMixture.scale;
		for (final NormalDistribution distribution : normalDistributionMixture.mixtures
				.keySet()) {
			this.mixtures.put(distribution.clone(),
					normalDistributionMixture.mixtures.get(distribution));
		}
		this.support = new Interval[this.attributes.length];
		for (int i = 0; i < this.support.length; i++) {
			this.support[i] = new Interval(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
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

	public Map<NormalDistribution, Double> getMixtures() {
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
		for (final Entry<NormalDistribution, Double> mixture : this.mixtures
				.entrySet()) {
			if (sb.length() > 1) {
				sb.append(";");
			}
			sb.append(mixture.getKey().toString()).append(":")
					.append(mixture.getValue());
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public NormalDistributionMixture clone() {
		return new NormalDistributionMixture(this);
	}

}
