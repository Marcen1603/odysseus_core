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
package de.uniol.inf.is.odysseus.server.opcua.func;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.opcua.common.utilities.StatusMapping;
import de.uniol.inf.is.odysseus.opcua.common.utilities.StatusMapping.Kind;

/**
 * This function retrieves the code to a status.
 */
public class ToErrorValFunction extends AbstractFunction<Long> implements IMepFunction<Long> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 628688567636378307L;

	/** The Constant accTypes. */
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.STRING } };

	/**
	 * Instantiates a new error-to-code function.
	 */
	public ToErrorValFunction() {
		super("ToErrorVal", 1, accTypes, SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {
		String value = getInputValue(0);
		String[] parts = value.split("_", 2);
		Kind kind = Kind.valueOf(parts[0]);
		String name = parts[1];
		return StatusMapping.getName2Long().get(kind).get(name);
	}
}