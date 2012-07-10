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
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;

public class PowerOperator extends AbstractBinaryOperator<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1875154948427727513L;

	@Override
	public int getPrecedence() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "^";
	}

	@Override
	public Double getValue() {
		return Math.pow(getNumericalInputValue(0), getNumericalInputValue(1));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
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
	public boolean isLeftDistributiveWith(IOperator<Double> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Double> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return null;
	}

	public static final SDFDatatype[] accTypes0 = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT};
	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] { SDFDatatype.INTEGER };
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()-1){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
        SDFDatatype[] accTypes = null;
        switch(argPos){
        case 0: 
        	return accTypes0;
        case 1:
        	return accTypes1;
        }
        return accTypes;
	}
}
