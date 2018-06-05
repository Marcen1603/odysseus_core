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

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringObjectInputFunction;

/**
 * Converts a {@link SDFDatatype} STRING value into a {@link SDFDatatype}
 * INTEGER value.
 * 
 * @author Marco Grawunder
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ToIntegerFromStringFunction extends AbstractUnaryStringObjectInputFunction<Integer> {

	private static final long serialVersionUID = 2799997996073155068L;

	/**
	 * 
	 */
	public ToIntegerFromStringFunction() {
		super("toInteger", SDFDatatype.INTEGER);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Integer getValue() {
        Object oIn = getInputValue(0);
        if (oIn == null) {
            return null;
        }
        String input = oIn.toString();
        if (Strings.isNullOrEmpty(input)) {
			return null;
		}
		if (Boolean.TRUE.toString().equalsIgnoreCase(input)) {
			return new Integer(1);
		} else if (Boolean.FALSE.toString().equalsIgnoreCase(input)) {
			return new Integer(0);
		}
		return Integer.valueOf(Double.valueOf(input).intValue());
	}
}
