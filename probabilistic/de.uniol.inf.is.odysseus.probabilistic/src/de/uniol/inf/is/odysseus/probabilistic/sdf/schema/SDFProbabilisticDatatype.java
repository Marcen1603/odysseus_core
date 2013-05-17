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
 * Probabilistic SDF datatypes.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SDFProbabilisticDatatype extends SDFDatatype {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2867228296513432602L;

	public SDFProbabilisticDatatype(final String URI) {
		super(URI, true);
	}

	public SDFProbabilisticDatatype(final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	public SDFProbabilisticDatatype(final String datatypeName,
			final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	public SDFProbabilisticDatatype(final String datatypeName,
			final KindOfDatatype type, final SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	public static final SDFDatatype COVARIANCE_MATRIX = new SDFProbabilisticDatatype(
			"CovarianceMatrix");
	public static final SDFDatatype MULTIVARIATE_COVARIANCE_MATRIX = new SDFProbabilisticDatatype(
			"MultivariateCovarianceMatrix");

	public static final SDFDatatype PROBABILISTIC_DOUBLE = new SDFProbabilisticDatatype(
			"ProbabilisticDouble");
	public static final SDFDatatype PROBABILISTIC_FLOAT = new SDFProbabilisticDatatype(
			"ProbabilisticFloat");
	public static final SDFDatatype PROBABILISTIC_LONG = new SDFProbabilisticDatatype(
			"ProbabilisticLong");
	public static final SDFDatatype PROBABILISTIC_INTEGER = new SDFProbabilisticDatatype(
			"ProbabilisticInteger");
	public static final SDFDatatype PROBABILISTIC_SHORT = new SDFProbabilisticDatatype(
			"ProbabilisticShort");
	public static final SDFDatatype PROBABILISTIC_BYTE = new SDFProbabilisticDatatype(
			"ProbabilisticByte");
	public static final SDFDatatype PROBABILISTIC_STRING = new SDFProbabilisticDatatype(
			"ProbabilisticString");
	/** Probabilistic continuous double datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_DOUBLE = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousDouble");
	/** Probabilistic continuous float datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_FLOAT = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousFloat");
	/** Probabilistic continuous long datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_LONG = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousLong");
	/** Probabilistic continuous integer datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_INTEGER = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousInteger");
	/** Probabilistic continuous short datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_SHORT = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousShort");
	/** Probabilistic continuous byte datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_BYTE = new SDFProbabilisticDatatype(
			"ProbabilisticContinuousByte");

	/**
	 * Checks whether the data type is a probabilistic data type.
	 * 
	 * @return <code>true</code> iff the data type is either discrete
	 *         probabilistic or continuous probabilistic
	 */
	public boolean isProbabilistic() {
		return this.isContinuous() || this.isDiscrete();
	}

	/**
	 * Checks whether the data type is a continuous probabilistic data type.
	 * 
	 * @return <code>true</code> if the data type is continuous probabilistic
	 */
	public boolean isContinuous() {
		return (this.getURI().equals(
				SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE
						.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT
								.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG
								.getURI())
				|| this.getURI()
						.equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER
								.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT
								.getURI()) || this.getURI()
				.equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE
						.getURI()));
	}

	/**
	 * Checks whether the data type is a discrete probabilistic data type.
	 * 
	 * @return <code>true</code> if the data type is discrete probabilistic
	 */
	public boolean isDiscrete() {
		return (this.getURI().equals(
				SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_FLOAT.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_LONG.getURI())
				|| this.getURI()
						.equals(SDFProbabilisticDatatype.PROBABILISTIC_INTEGER
								.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_SHORT.getURI())
				|| this.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_BYTE.getURI()) || this
				.getURI().equals(
						SDFProbabilisticDatatype.PROBABILISTIC_STRING.getURI()));
	}

	/**
	 * Checks whether the data type is a numeric data type.
	 * 
	 * @return <code>true</code> if the data type is numeric
	 */
	@Override
	public boolean isNumeric() {
		return (super.isNumeric()
				|| this.getURI().equals(PROBABILISTIC_LONG.getURI())
				|| this.getURI().equals(PROBABILISTIC_INTEGER.getURI())
				|| this.getURI().equals(PROBABILISTIC_DOUBLE.getURI())
				|| this.getURI().equals(PROBABILISTIC_FLOAT.getURI())
				|| this.getURI().equals(PROBABILISTIC_SHORT.getURI())
				|| this.getURI().equals(PROBABILISTIC_BYTE.getURI())
				|| this.getURI().equals(PROBABILISTIC_CONTINUOUS_LONG.getURI())
				|| this.getURI().equals(
						PROBABILISTIC_CONTINUOUS_INTEGER.getURI())
				|| this.getURI().equals(
						PROBABILISTIC_CONTINUOUS_DOUBLE.getURI())
				|| this.getURI()
						.equals(PROBABILISTIC_CONTINUOUS_FLOAT.getURI())
				|| this.getURI()
						.equals(PROBABILISTIC_CONTINUOUS_SHORT.getURI()) || this
				.getURI().equals(PROBABILISTIC_CONTINUOUS_BYTE.getURI()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isDouble()
	 */
	@Override
	public boolean isDouble() {
		return super.isDouble()
				|| this.getURI().equals(PROBABILISTIC_DOUBLE.getURI())
				|| this.getURI().equals(
						PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isInteger()
	 */
	@Override
	public boolean isInteger() {
		return super.isInteger()
				|| this.getURI().equals(PROBABILISTIC_INTEGER.getURI())
				|| this.getURI().equals(
						PROBABILISTIC_CONTINUOUS_INTEGER.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isLong()
	 */
	@Override
	public boolean isLong() {
		return super.isLong()
				|| this.getURI().equals(PROBABILISTIC_LONG.getURI())
				|| this.getURI().equals(PROBABILISTIC_CONTINUOUS_LONG.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isFloat()
	 */
	@Override
	public boolean isFloat() {
		return super.isFloat()
				|| this.getURI().equals(PROBABILISTIC_FLOAT.getURI())
				|| this.getURI()
						.equals(PROBABILISTIC_CONTINUOUS_FLOAT.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isShort()
	 */
	@Override
	public boolean isShort() {
		return super.isShort()
				|| this.getURI().equals(PROBABILISTIC_SHORT.getURI())
				|| this.getURI()
						.equals(PROBABILISTIC_CONTINUOUS_SHORT.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isByte()
	 */
	@Override
	public boolean isByte() {
		return super.isByte()
				|| this.getURI().equals(PROBABILISTIC_BYTE.getURI())
				|| this.getURI().equals(PROBABILISTIC_CONTINUOUS_BYTE.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isString()
	 */
	@Override
	public boolean isString() {
		return super.isString()
				|| this.getURI().equals(PROBABILISTIC_STRING.getURI());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCovarianceMatrix() {
		return this.getURI().equals(SDFProbabilisticDatatype.COVARIANCE_MATRIX);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMultivariateCovarianceMatrix() {
		return this.getURI().equals(
				SDFProbabilisticDatatype.MULTIVARIATE_COVARIANCE_MATRIX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#compatibleTo(de.
	 * uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype)
	 */
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
