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
package de.uniol.inf.is.odysseus.wrapper.opcua.func;

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.opcua.common.utilities.StatusMapping;
import de.uniol.inf.is.odysseus.opcua.common.utilities.StatusMapping.Kind;

/**
 * A function to retrieve the name of a status code.
 */
public class ToErrorStrFunction extends AbstractFunction<String> implements IMepFunction<String> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1144254816036709722L;

	/** The Constant accTypes. */
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.LONG } };

	/**
	 * Instantiates a new to error-to-string function.
	 */
	public ToErrorStrFunction() {
		super("ToErrorStr", 1, accTypes, SDFDatatype.STRING);
	}

	@Override
	public String getValue() {
		Number value = getInputValue(0);
		long code = value.longValue();
		Entry<Kind, String> e = StatusMapping.getLong2Name().get(code);
		return e.getKey() + "_" + e.getValue();
	}
}