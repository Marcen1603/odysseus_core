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
import de.uniol.inf.is.odysseus.mep.AbstractUnaryStringInputFunction;

/**
 * Converts a {@link SDFDatatype} STRING value into a {@link SDFDatatype} CHAR
 * value by returning the first character of the string or <code>null</code> if
 * the string is null or empty.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ToCharFromStringFunction extends AbstractUnaryStringInputFunction<Character> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1956450848515723544L;

	/**
	 * 
	 */
	public ToCharFromStringFunction() {
		super("toChar", SDFDatatype.CHAR);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Character getValue() {
        Object oIn = getInputValue(0);
        if (oIn == null) {
            return null;
        }
        String input = oIn.toString();
        if (Strings.isNullOrEmpty(input)) {
			return null;
		}
		return new Character(input.charAt(0));
	}

}
