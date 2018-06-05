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

import de.uniol.inf.is.odysseus.core.datatype.DString;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns a new string that is a substring of the value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SubStringFunction extends AbstractFunction<DString> {

	private static final long serialVersionUID = 2270358376473789092L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DSTRING}, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

	public SubStringFunction() {
		super("substring", 3, accTypes, SDFDatatype.STRING);
	}

	@Override
	public DString getValue() {
		return ((DString) getInputValue(0)).substring(getNumericalInputValue(1)
				.intValue(), getNumericalInputValue(2).intValue());
	}

}
