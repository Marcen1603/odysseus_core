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
 * Probabilistic SDF data types.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SDFProbabilisticDatatype extends SDFDatatype {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2867228296513432602L;

	/**
	 * Constructs a new probabilistic data type from the given URI.
	 * 
	 * @param uri
	 *            The URI
	 */
	public SDFProbabilisticDatatype(final String uri) {
		super(uri, true);
	}

	/**
	 * Constructs a new probabilistic data type from the given data type.
	 * 
	 * @param sdfDatatype
	 *            The data type
	 */
	public SDFProbabilisticDatatype(final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	/**
	 * Constructs a new probabilistic data type with the given name, type, and schema.
	 * 
	 * @param name
	 *            The data type name
	 * @param type
	 *            The type
	 * @param schema
	 *            The schema
	 */
	public SDFProbabilisticDatatype(final String name, final KindOfDatatype type, final SDFSchema schema) {
		super(name, type, schema, true);
	}

	/**
	 * Constructs a new probabilistic data type with the given name, type, and sub type.
	 * 
	 * @param name
	 *            The data type name
	 * @param type
	 *            The type
	 * @param subType
	 *            The sub type
	 */
	public SDFProbabilisticDatatype(final String name, final KindOfDatatype type, final SDFDatatype subType) {
		super(name, type, subType, true);
	}

	/** Probabilistic tuple. */
	public static final SDFDatatype PROBABILISTIC_TUPLE = new SDFProbabilisticDatatype("ProbabilisticTuple");
	/** Probabilistic result used in probabilistic continuous predicates. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_PREDICATE_RESULT = new SDFProbabilisticDatatype("ProbabilisticContinuousPredicateResult");
	/** Probabilistic discrete double datatype. */
	public static final SDFDatatype PROBABILISTIC_DOUBLE = new SDFProbabilisticDatatype("ProbabilisticDouble");
	/** Probabilistic discrete float datatype. */
	public static final SDFDatatype PROBABILISTIC_FLOAT = new SDFProbabilisticDatatype("ProbabilisticFloat");
	/** Probabilistic discrete long datatype. */
	public static final SDFDatatype PROBABILISTIC_LONG = new SDFProbabilisticDatatype("ProbabilisticLong");
	/** Probabilistic discrete integer datatype. */
	public static final SDFDatatype PROBABILISTIC_INTEGER = new SDFProbabilisticDatatype("ProbabilisticInteger");
	/** Probabilistic discrete short datatype. */
	public static final SDFDatatype PROBABILISTIC_SHORT = new SDFProbabilisticDatatype("ProbabilisticShort");
	/** Probabilistic discrete byte datatype. */
	public static final SDFDatatype PROBABILISTIC_BYTE = new SDFProbabilisticDatatype("ProbabilisticByte");
	/** Probabilistic discrete string datatype. */
	public static final SDFDatatype PROBABILISTIC_STRING = new SDFProbabilisticDatatype("ProbabilisticString");
	/** Probabilistic continuous double datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_DOUBLE = new SDFProbabilisticDatatype("ProbabilisticContinuousDouble");
	/** Probabilistic continuous float datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_FLOAT = new SDFProbabilisticDatatype("ProbabilisticContinuousFloat");
	/** Probabilistic continuous long datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_LONG = new SDFProbabilisticDatatype("ProbabilisticContinuousLong");
	/** Probabilistic continuous integer datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_INTEGER = new SDFProbabilisticDatatype("ProbabilisticContinuousInteger");
	/** Probabilistic continuous short datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_SHORT = new SDFProbabilisticDatatype("ProbabilisticContinuousShort");
	/** Probabilistic continuous byte datatype. */
	public static final SDFDatatype PROBABILISTIC_CONTINUOUS_BYTE = new SDFProbabilisticDatatype("ProbabilisticContinuousByte");

	/**
	 * Checks whether the data type is a probabilistic data type.
	 * 
	 * @return <code>true</code> iff the data type is either discrete probabilistic or continuous probabilistic
	 */
	public final boolean isProbabilistic() {
		return this.isContinuous() || this.isDiscrete();
	}

	/**
	 * Checks whether the data type is a continuous probabilistic data type.
	 * 
	 * @return <code>true</code> if the data type is continuous probabilistic
	 */
	public final boolean isContinuous() {
		return (this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG.getURI())
				|| this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE.getURI()));
	}

	/**
	 * Checks whether the data type is a discrete probabilistic data type.
	 * 
	 * @return <code>true</code> if the data type is discrete probabilistic
	 */
	public final boolean isDiscrete() {
		return (this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_FLOAT.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_LONG.getURI())
				|| this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_INTEGER.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_SHORT.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_BYTE.getURI()) || this.getURI().equals(
				SDFProbabilisticDatatype.PROBABILISTIC_STRING.getURI()));
	}

	/**
	 * Checks whether the data type is a numeric data type.
	 * 
	 * @return <code>true</code> if the data type is numeric
	 */
	@Override
	public final boolean isNumeric() {
		return false;
		// return (super.isNumeric() || this.getURI().equals(PROBABILISTIC_LONG.getURI()) || this.getURI().equals(PROBABILISTIC_INTEGER.getURI()) || this.getURI().equals(PROBABILISTIC_DOUBLE.getURI()) || this.getURI().equals(PROBABILISTIC_FLOAT.getURI())
		// || this.getURI().equals(PROBABILISTIC_SHORT.getURI()) || this.getURI().equals(PROBABILISTIC_BYTE.getURI()) || this.getURI().equals(PROBABILISTIC_CONTINUOUS_LONG.getURI()) || this.getURI().equals(PROBABILISTIC_CONTINUOUS_INTEGER.getURI())
		// || this.getURI().equals(PROBABILISTIC_CONTINUOUS_DOUBLE.getURI()) || this.getURI().equals(PROBABILISTIC_CONTINUOUS_FLOAT.getURI()) || this.getURI().equals(PROBABILISTIC_CONTINUOUS_SHORT.getURI()) || this.getURI().equals(PROBABILISTIC_CONTINUOUS_BYTE.getURI()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isDouble()
	 */
	@Override
	public final boolean isDouble() {
		return super.isDouble() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isInteger()
	 */
	@Override
	public final boolean isInteger() {
		return super.isInteger() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_INTEGER.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_INTEGER.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isLong()
	 */
	@Override
	public final boolean isLong() {
		return super.isLong() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_LONG.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_LONG.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isFloat()
	 */
	@Override
	public final boolean isFloat() {
		return super.isFloat() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_FLOAT.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_FLOAT.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isShort()
	 */
	@Override
	public final boolean isShort() {
		return super.isShort() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_SHORT.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_SHORT.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isByte()
	 */
	@Override
	public final boolean isByte() {
		return super.isByte() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_BYTE.getURI()) || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_BYTE.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#isString()
	 */
	@Override
	public final boolean isString() {
		return super.isString() || this.getURI().equals(SDFProbabilisticDatatype.PROBABILISTIC_STRING.getURI());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype#compatibleTo(de. uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype)
	 */
	@Override
	public final boolean compatibleTo(final SDFDatatype other) {
		if (super.compatibleTo(other)) {
			return true;
		}
		if (other instanceof SDFProbabilisticDatatype) {
			final SDFProbabilisticDatatype otherProbabilistic = (SDFProbabilisticDatatype) other;
			if ((this.isDiscrete() && (otherProbabilistic.isDiscrete())) || (this.isContinuous() && (otherProbabilistic.isContinuous()))) {
				return true;
			}
		}
		return false;
	}
}
