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
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RoundFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = 5571924782173674368L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {SDFDatatype.NUMBERS, {SDFDatatype.INTEGER, SDFDatatype.LONG}};

	public RoundFunction() {
		super("round",2,accTypes,SDFDatatype.DOUBLE);
	}
	
	@Override
	public Double getValue() {
		Number a = getInputValue(0);
		Number b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		return Math.round(a.doubleValue() * Math.pow(10, b.doubleValue())) / Math.pow(10, b.doubleValue());
	}

}
