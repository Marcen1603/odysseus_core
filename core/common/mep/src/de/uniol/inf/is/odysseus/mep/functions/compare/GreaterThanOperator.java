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
package de.uniol.inf.is.odysseus.mep.functions.compare;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class GreaterThanOperator extends AbstractBinaryNumberInputOperator<Boolean> {


	private static final long serialVersionUID = 3761097192507658786L;

	public GreaterThanOperator() {
		super(">", SDFDatatype.BOOLEAN);
	}
	
	protected GreaterThanOperator(SDFDatatype[][] accTypes) {
		super(">", SDFDatatype.BOOLEAN);
}

	@Override
	public int getPrecedence() {
		return 8;
	}

	@Override
	public Boolean getValue() {
		Number left = getInputValue(0);
		Number right = getInputValue(1);
		if ((left == null) || (right == null)) {
			return null;
		}
		return Boolean.valueOf(left.doubleValue() > right.doubleValue());
	}


	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public boolean isAssociative() {
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Boolean> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Boolean> operator) {
		return false;
	}
	
}
