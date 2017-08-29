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
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;
import de.uniol.inf.is.odysseus.opcua.common.core.SDFOPCUADatatype;

/**
 * This function creates an OPC value.
 */
public class ToOPCValueFunction extends AbstractFunction<OPCValue<Double>> implements IMepFunction<OPCValue<Double>> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4024328742545051468L;

	/** The Constant accTypes. */
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFDatatype.NUMBERS,
			SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

	/**
	 * Instantiates a new value-to-OPC function.
	 */
	public ToOPCValueFunction() {
		super("ToOPCValue", 4, accTypes, SDFOPCUADatatype.OPCVALUE);
	}

	@Override
	public OPCValue<Double> getValue() {
		long timestamp = getNumericalInputValue(0).longValue();
		double value = getNumericalInputValue(1).doubleValue();
		int quality = getNumericalInputValue(2).shortValue();
		long error = getNumericalInputValue(3).intValue();
		return new OPCValue<Double>(timestamp, value, quality, error);
	}
}