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
package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SDFProbabilisticDatatype extends SDFDatatype {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2867228296513432602L;

	public SDFProbabilisticDatatype(final String URI) {
		super(URI);
	}

	public SDFProbabilisticDatatype(final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	public SDFProbabilisticDatatype(final String datatypeName,
			final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema);
	}

	public SDFProbabilisticDatatype(final String datatypeName,
			final KindOfDatatype type, final SDFDatatype subType) {
		super(datatypeName, type, subType);
	}

	public static final SDFDatatype COVARIANCE_MATRIX = new SDFProbabilisticDatatype(
			"CovarianceMatrix");
	public static final SDFDatatype MULTIVARIATE_COVARIANCE_MATRIX = new SDFProbabilisticDatatype(
			"MultivariateCovarianceMatrix");

	public static final SDFDatatype PROBABILISTIC_DOUBLE = new SDFProbabilisticDatatype(
			"ProbabilisticDouble");

	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_DOUBLE = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousDouble");

	public boolean isProbabilistic() {
		return this.isContinuous() || this.isDiscrete();
	}

	public boolean isContinuous() {
		return this.getURI().equals(
				SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE
						.getURI());
	}

	public boolean isDiscrete() {
		return this.getURI().equals(
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI());
	}

	public boolean isCovarianceMatrix() {
		return this.getURI().equals(SDFProbabilisticDatatype.COVARIANCE_MATRIX);
	}

	public boolean isMultivariateCovarianceMatrix() {
		return this.getURI().equals(
				SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX);
	}

	@Override
	public boolean compatibleTo(final SDFDatatype other) {
		if (other instanceof SDFProbabilisticDatatype) {
			final SDFProbabilisticDatatype otherProbabilistic = (SDFProbabilisticDatatype) other;
			if ((this.isDiscrete() && (otherProbabilistic.isDiscrete()))
					|| (this.isContinuous() && (otherProbabilistic
							.isContinuous()))
					|| (this.isCovarianceMatrix() && (otherProbabilistic
							.isCovarianceMatrix()))
					|| (this.isMultivariateCovarianceMatrix() && (otherProbabilistic
							.isMultivariateCovarianceMatrix()))) {
				return true;
			}
		}
		return super.compatibleTo(other);
	}
}
