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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToLongFunction extends AbstractFunction<Long> {

	private static final long serialVersionUID = -6921898506120412818L;

	public ToLongFunction() {
		super("toLong", 1, SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {
		String s = getInputValue(0).toString();
		if (s.equalsIgnoreCase("true")) {
			return 1L;
		} else if (s.equalsIgnoreCase("false")) {
			return 0L;
		}
		Double val = Double.parseDouble(getInputValue(0).toString());
		return val.longValue();
	}

}
