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
package de.uniol.inf.is.odysseus.mep.matrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * 
 * @author Henrik Surm
 * 
 */
public class VectorFromStringRangeFunction extends AbstractReadFunction<double[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3550190318743371997L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, 		// Line
			{ SDFDatatype.STRING },			// Delimiter
			SDFDatatype.DISCRETE_NUMBERS,	// Start index
			SDFDatatype.DISCRETE_NUMBERS	// Number of values to read (0 = read all) 
		};

	public VectorFromStringRangeFunction() {
		super("vectorFromString", 4, accTypes, SDFDatatype.VECTOR_DOUBLE);
	}

	@Override
	public double[] getValue() {
		String line = getInputValue(0);
		String delimiter = getInputValue(1);
		int start = getNumericalInputValue(2).intValue();
		int count = getNumericalInputValue(3).intValue();
		return VectorFromStringFunction.vectorFromString(line, delimiter, start, count);
	}
}
