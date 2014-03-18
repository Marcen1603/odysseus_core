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
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class SmallerThanOperator extends AbstractBinaryOperator<Boolean> {

	
	private static final long serialVersionUID = -6857126775649444062L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG,
				SDFDatatype.DOUBLE, SDFDatatype.FLOAT },
		new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG,
				SDFDatatype.DOUBLE, SDFDatatype.FLOAT } };

	
	public SmallerThanOperator() {
		super("<",accTypes, SDFDatatype.BOOLEAN);
	}
	
	protected SmallerThanOperator(SDFDatatype[][] accTypes) {
		super("<",accTypes, SDFDatatype.BOOLEAN);
	}

	@Override
	public int getPrecedence() {
		return 8;
	}


	@Override
	public Boolean getValue() {
		Double val0 = getNumericalInputValue(0);
		Double val1 = getNumericalInputValue(1);
//		System.out.println(val0 + " < " + val1);
		return val0 < val1;
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

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}
}
