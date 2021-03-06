/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.dstring;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Returns the length of a string.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class LengthFunction extends AbstractUnaryDStringFunction<Integer> {

	private static final long serialVersionUID = 3128369768278903586L;

	public LengthFunction() {
		super("length", SDFDatatype.INTEGER);
	}
	
	@Override
	public Integer getValue() {
		return getInputValue(0).toString().length();
	}

}
