/********************************************************************************** 
 * Copyright 2016 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryBooleanInputFunction;

/**
 * Converts a {@link SDFDatatype} BOOLEAN value into a {@link SDFDatatype} LONG
 * value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ToLongFromBooleanFunction extends AbstractUnaryBooleanInputFunction<Long> {

	/**
		 * 
		 */
	private static final long serialVersionUID = -4774121215386854278L;

	/**
	 * 
	 */
	public ToLongFromBooleanFunction() {
		super("toLong", SDFDatatype.LONG);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Long getValue() {
		Boolean input = getInputValue(0);
		if (input == null) {
			return null;
		}
		if (input.booleanValue()) {
			return new Long(1l);
		}
		return new Long(0l);
	}

}
