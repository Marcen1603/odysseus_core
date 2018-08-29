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

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;
import de.uniol.inf.is.odysseus.opcua.common.core.SDFOPCUADatatype;

/**
 * A function to get the value of an OPC value.
 *
 * @param <T>
 *            the generic type
 */
public class ValueFunction<T> extends AbstractFunction<T> implements IMepFunction<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -354603068556899719L;

	/** The Constant accTypes. */
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCUADatatype.types };

	/**
	 * Instantiates a new value function.
	 */
	public ValueFunction() {
		super("Value", 1, accTypes, SDFDatatype.DOUBLE);
	}

	@Override
	public T getValue() {
		OPCValue<T> value = getInputValue(0);
		return value.getValue();
	}

	@Override
	public boolean determineTypeFromInput() {
		return true;
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		if (args.length == 1)
			return args[0].getReturnType().getSubType();
		return null;
	}
}