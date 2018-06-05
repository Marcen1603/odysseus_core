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
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;


public class PlusOperator extends AbstractBinaryNumberInputOperator<Double> {

	private static final long serialVersionUID = -1830610182002870058L;

	public PlusOperator() {
		super("+", SDFDatatype.DOUBLE);
	}
	
	@Override
	public int getPrecedence() {
		return 6;
	}


	@Override
	public Double getValue() {
		Number a = getInputValue(0);
		Number b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		return a.doubleValue() + b.doubleValue();
	}

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Double> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Double> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}
	
}
