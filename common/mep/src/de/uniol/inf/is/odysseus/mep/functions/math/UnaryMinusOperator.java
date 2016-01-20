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
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractUnaryOperator;

public class UnaryMinusOperator extends AbstractUnaryOperator<Double> {

	private static final long serialVersionUID = 5934608915212061886L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.FLOATING_NUMBERS};
	
	public UnaryMinusOperator() {
		super("UnaryMinus", accTypes, SDFDatatype.DOUBLE);
	}
	
	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public Double getValue() {
		Number a = getInputValue(0);
		if (a == null) {
			return null;
		}
		return -a.doubleValue();
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.RIGHT_TO_LEFT;
	}
	

}
