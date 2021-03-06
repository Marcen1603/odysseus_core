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
package de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class DolToEur extends AbstractFunction<Double> {

	
	private static final long serialVersionUID = -8220565259943514844L;
	private static double EXCHANGERATE = 1d / 1.55d;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {SDFDatatype.NUMBERS};
	

	public DolToEur() {
		super("DolToEur",1,accTypes, SDFDatatype.DOUBLE);
	}
	
	public static void setExchangeRate(double value) {
		EXCHANGERATE = value;
	}

	
	@Override
	public Double getValue() {
		double value = ((Number) getInputValue(0)).doubleValue();
		value *= EXCHANGERATE;
		return value;
	}

}
