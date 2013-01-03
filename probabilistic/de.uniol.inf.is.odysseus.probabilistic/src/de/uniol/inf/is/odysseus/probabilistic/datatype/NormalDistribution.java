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

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NormalDistribution implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5858308006884394418L;
	/** The unique id of the covariance matrix for this distribution */
	private CovarianceMatrix covarianceMatrix;
	/** The mean of the distribution */
	private double[] mean;

	public NormalDistribution(final double mean,
			final CovarianceMatrix covarianceMatrix) {
		this(new double[] { mean }, covarianceMatrix);
	}

	public NormalDistribution(final double[] mean,
			final CovarianceMatrix covarianceMatrix) {
		this.mean = mean;
		this.covarianceMatrix = covarianceMatrix;
	}

	public NormalDistribution(final NormalDistribution distribution) {
		this.mean = distribution.mean;
		this.covarianceMatrix = distribution.covarianceMatrix.clone();
	}

	public CovarianceMatrix getCovarianceMatrix() {
		return this.covarianceMatrix;
	}

	public void setCovarianceMatrix(final CovarianceMatrix matrix) {
		this.covarianceMatrix = matrix;
	}

	public double[] getMean() {
		return this.mean;
	}

	public double getMean(final int dimension) {
		return this.mean[dimension];
	}

	public void setMean(final double[] mean) {
		this.mean = mean;
	}

	public void setMean(final int dimension, final double mean) {
		this.mean[dimension] = mean;
	}

	@Override
	public NormalDistribution clone() {
		return new NormalDistribution(this);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("𝒩({");
		for (int i = 0; i < this.mean.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(this.mean[i]);
		}
		sb.append("},").append(this.covarianceMatrix).append(")");
		return sb.toString();
	}

}
