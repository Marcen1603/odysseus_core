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

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

public class StringEqualsOperator extends AbstractBinaryOperator<Boolean> implements IHasAlias {

	private static final long serialVersionUID = 6011800156304578501L;
	public static final SDFDatatype[][] accTypes =
				new SDFDatatype[][]{new SDFDatatype[] {SDFDatatype.STRING},new SDFDatatype[] { SDFDatatype.STRING}};
	
	public StringEqualsOperator() {
		this("=");
	}

	public StringEqualsOperator(String symbol){
		super(symbol,accTypes, SDFDatatype.BOOLEAN);
	}
	
	public StringEqualsOperator(String symbol, SDFDatatype[][] accTypes){
		super(symbol,accTypes, SDFDatatype.BOOLEAN);
	}
	
	
	@Override
	public int getPrecedence() {
		return 9;
	}


	@Override
	public Boolean getValue() {
		return getInputValue(0) != null && getInputValue(0).equals(getInputValue(1));
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

	@Override
	public String getAliasName() {
		return "==";
	}

}
