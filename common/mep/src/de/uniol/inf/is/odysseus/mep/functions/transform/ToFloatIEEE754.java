/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Converts two unsigned integer to a float value. with IEEE 754 (single
 * precision)
 * 
 * 
 * @author Marco Grawunder
 */
public class ToFloatIEEE754 extends AbstractToFloatIEEE754 {

	private static final long serialVersionUID = 1201584883722391034L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.UNSIGNEDINT16 },
			new SDFDatatype[] { SDFDatatype.UNSIGNEDINT16 },
			new SDFDatatype[] { SDFDatatype.BOOLEAN } };

	public ToFloatIEEE754() {
		super( 3, accTypes);
	}

	@Override
	public Float getValue() {
		Integer v1 = getNumericalInputValue(0).intValue();
		Integer v2 = getNumericalInputValue(1).intValue();
		Boolean byteSwap = getInputValue(2);
		return calcValue(v1, v2, byteSwap);
	}

	public static void main(String[] args) {
		System.out.println(calcValue(16547, 54270, false));
		System.out.println(calcValue(16547, 54270, true));
	}
}
