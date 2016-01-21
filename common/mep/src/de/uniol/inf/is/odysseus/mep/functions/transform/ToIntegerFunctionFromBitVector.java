/*******************************************************************************
 * Copyright 2016 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryBitvectorInputFunction;

/**
 * Converts a {@link SDFDatatype} BITVECTOR value into a {@link SDFDatatype}
 * INTEGER value.
 * 
 * @author Marco Grawunder
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ToIntegerFunctionFromBitVector extends AbstractUnaryBitvectorInputFunction<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3104385272551767913L;

	/**
	 * 
	 */
	public ToIntegerFunctionFromBitVector() {
		super("toInteger", SDFDatatype.INTEGER);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Integer getValue() {
		BitVector input = getInputValue(0);
		if (input == null) {
			return null;
		}
		return input.asInteger();
	}
}
