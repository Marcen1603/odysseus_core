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
package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns a new string that is a substring of the value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class SubStringFunction2 extends AbstractFunction<String> {

	private static final long serialVersionUID = 3177285577975351278L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING, SDFDatatype.OBJECT }, SDFDatatype.NUMBERS };

	public SubStringFunction2() {
		super("substring", 2, accTypes, SDFDatatype.STRING);
	}

	@Override
	public String getValue() {
		String a = getInputValue(0);
		Integer b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		return a.substring(b);
	}

}
