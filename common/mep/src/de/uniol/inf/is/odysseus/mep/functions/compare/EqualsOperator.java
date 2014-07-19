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

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class EqualsOperator extends AbstractBinaryOperator<Boolean> {

	private static final long serialVersionUID = 6011800156304578501L;
	public static final SDFDatatype[][] accTypes =
				new SDFDatatype[][]{new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.STRING},new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.STRING}};
	
	public EqualsOperator() {
		this("=");
	}

	public EqualsOperator(String symbol){
		super(symbol,accTypes, SDFDatatype.BOOLEAN);
	}
	
	public EqualsOperator(String symbol, SDFDatatype[][] accTypes){
		super(symbol,accTypes, SDFDatatype.BOOLEAN);
	}
	
	private boolean isNumeric = false;

	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public void setArgument(int argumentPosition, IExpression<?> argument) {
		if (argument instanceof Constant) {
			Constant<?> constArg = (Constant<?>) argument;
			if (constArg.getReturnType().isNumeric()) {
				isNumeric = true;
			}
		}

		super.setArgument(argumentPosition, argument);
	}

	@Override
	public Boolean getValue() {
		return isNumeric ? getNumericalInputValue(0).equals(getNumericalInputValue(1)) : getInputValue(0) != null && getInputValue(0).equals(getInputValue(1));
	}


	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public boolean isCommutative() {
		return true;
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
