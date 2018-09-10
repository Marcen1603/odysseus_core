/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package de.uniol.inf.is.odysseus.opcua.common.core;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * The SDFDatatype for OPC UA.
 */
public class SDFOPCUADatatype extends SDFDatatype {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2619246735313817401L;

	/** The standard OPC value. */
	public static final SDFDatatype OPCVALUE = new SDFOPCUADatatype("OPCValue", SDFDatatype.KindOfDatatype.GENERIC,
			SDFDatatype.DOUBLE);

	/** A collection of types. */
	public static final SDFDatatype[] types = new SDFDatatype[] { OPCVALUE };

	/**
	 * Instantiates a new SDFOPCUA data type.
	 *
	 * @param datatypeName
	 *            the data type name
	 * @param type
	 *            the type
	 * @param subType
	 *            the sub type
	 */
	public SDFOPCUADatatype(final String datatypeName, final KindOfDatatype type, final SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}
}