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

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;
import de.uniol.inf.is.odysseus.opcua.common.core.SDFOPCUADatatype;

/**
 * A function to get the quality of an OPC value.
 */
public class QualityFunction extends AbstractFunction<Integer> implements IMepFunction<Integer> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5815276686533770744L;

	/** The Constant accTypes. */
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCUADatatype.types };

	/**
	 * Instantiates a new quality function.
	 */
	public QualityFunction() {
		super("Quality", 1, accTypes, SDFDatatype.SHORT);
	}

	@Override
	public Integer getValue() {
		OPCValue<?> value = getInputValue(0);
		return value.getQuality();
	}
}