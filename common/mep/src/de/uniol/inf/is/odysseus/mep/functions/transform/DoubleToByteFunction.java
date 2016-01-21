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
 * Returns the value of the specified number as a <code>byte</code>.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @deprecated Use {@link ToByteFromNumberFunction}
 */
@Deprecated
public class DoubleToByteFunction extends AbstractFunction<Byte> {

	private static final long serialVersionUID = -3472340389470448274L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.DOUBLE } };

	public DoubleToByteFunction() {
		super("doubleToByte", 1, accTypes, SDFDatatype.BYTE);
	}

	@Override
	public Byte getValue() {
		return getNumericalInputValue(0).byteValue();
	}
}
