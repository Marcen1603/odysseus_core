/*
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

package de.uniol.inf.is.odysseus.datatype.interval.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class SDFIntervalDatatype extends SDFDatatype {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8647938282128195112L;

	public SDFIntervalDatatype(final String URI) {
		super(URI, true);
	}

	public SDFIntervalDatatype(final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	public SDFIntervalDatatype(final String datatypeName,
			final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	public SDFIntervalDatatype(final String datatypeName,
			final KindOfDatatype type, final SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	public static final SDFDatatype INTERVAL_DOUBLE = new SDFIntervalDatatype(
			"IntervalDouble");
	public static final SDFDatatype INTERVAL_FLOAT = new SDFIntervalDatatype(
			"IntervalFloat");
	public static final SDFDatatype INTERVAL_LONG = new SDFIntervalDatatype(
			"IntervalLong");
	public static final SDFDatatype INTERVAL_INTEGER = new SDFIntervalDatatype(
			"IntervalInteger");
	public static final SDFDatatype INTERVAL_SHORT = new SDFIntervalDatatype(
			"IntervalShort");
	public static final SDFDatatype INTERVAL_BYTE = new SDFIntervalDatatype(
			"IntervalByte");

	@Override
	public boolean isNumeric() {
//		return (this.getURI().equals(INTERVAL_LONG.getURI())
//				|| this.getURI().equals(INTERVAL_INTEGER.getURI())
//				|| this.getURI().equals(INTERVAL_DOUBLE.getURI())
//				|| this.getURI().equals(INTERVAL_FLOAT.getURI())
//				|| this.getURI().equals(INTERVAL_SHORT.getURI()) || this
//				.getURI().equals(INTERVAL_BYTE.getURI()));
	    return false;
	}

	@Override
	public boolean isDouble() {
		return this.getURI().equals(INTERVAL_DOUBLE.getURI());
	}

	@Override
	public boolean isInteger() {
		return this.getURI().equals(INTERVAL_INTEGER.getURI());
	}

	@Override
	public boolean isLong() {
		return this.getURI().equals(INTERVAL_LONG.getURI());
	}

	@Override
	public boolean isFloat() {
		return this.getURI().equals(INTERVAL_FLOAT.getURI());
	}

	@Override
	public boolean isShort() {
		return this.getURI().equals(INTERVAL_SHORT.getURI());
	}

	@Override
	public boolean isByte() {
		return this.getURI().equals(INTERVAL_BYTE.getURI());
	}

	@Override
	public boolean compatibleTo(final SDFDatatype other) {
		if (other instanceof SDFIntervalDatatype) {
			return true;
		}
		return super.compatibleTo(other);
	}

}
