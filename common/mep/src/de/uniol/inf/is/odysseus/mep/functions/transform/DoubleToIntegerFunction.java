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

/**
 * Returns the value of the specified number as a <code>integer</code>.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @deprecated Use {@link ToIntegerFromNumberFunction}
 */
@Deprecated
public class DoubleToIntegerFunction extends AbstractFunction<Integer> {

	private static final long serialVersionUID = 903337299667941417L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DOUBLE } };

	public DoubleToIntegerFunction() {
		super("doubleToInteger", 1, accTypes, SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		return getNumericalInputValue(0).intValue();
	}
}
