/*******************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.opcda.sdf.schema;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SDFOPCDADatatype extends SDFDatatype {

	/**
     * 
     */
	private static final long serialVersionUID = 7850101896218344675L;
	public static final SDFDatatype OPCVALUE = new SDFOPCDADatatype("OPCValue",
			SDFDatatype.KindOfDatatype.GENERIC, SDFDatatype.DOUBLE);
	public static final SDFDatatype OPCVALUE_STRING = new SDFOPCDADatatype(
			"OPCValue_String", SDFDatatype.KindOfDatatype.GENERIC,
			SDFDatatype.STRING);
	public static final SDFDatatype OPCVALUE_NUMERIC = new SDFOPCDADatatype(
			"OPCValue_Double", SDFDatatype.KindOfDatatype.GENERIC,
			SDFDatatype.DOUBLE);
	public static final SDFDatatype OPCVALUE_FLOAT = new SDFOPCDADatatype(
			"OPCValue_Float", SDFDatatype.KindOfDatatype.GENERIC,
			SDFDatatype.FLOAT);

	public static final SDFDatatype[] types = new SDFDatatype[] { OPCVALUE,
			OPCVALUE_FLOAT, OPCVALUE_STRING, OPCVALUE_NUMERIC };

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param URI
	 */
	public SDFOPCDADatatype(final String URI) {
		super(URI, true);
	}

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param sdfDatatype
	 */
	public SDFOPCDADatatype(final SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param datatypeName
	 * @param type
	 * @param schema
	 */
	public SDFOPCDADatatype(final String datatypeName,
			final KindOfDatatype type, final SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	/**
	 * 
	 * Class constructor.
	 * 
	 * @param datatypeName
	 * @param type
	 * @param subType
	 */
	public SDFOPCDADatatype(final String datatypeName,
			final KindOfDatatype type, final SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumeric() {
		return false;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean compatibleTo(final SDFDatatype other) {
		if (other instanceof SDFOPCDADatatype) {
			return true;
		}
		return super.compatibleTo(other);
	}

	public static boolean isOPCType(SDFDatatype datatype) {
		return datatype.getURIWithoutQualName().toLowerCase().startsWith("opcvalue");
	}

}
