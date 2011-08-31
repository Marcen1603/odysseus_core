/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class AndOperator extends AbstractBinaryOperator<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3014698511548261911L;

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Boolean> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Boolean> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public int getPrecedence() {
		return 13;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public String getSymbol() {
		return "&&";
	}

	@Override
	public Boolean getValue() {
		return (Boolean) getInputValue(0) && (Boolean) getInputValue(1);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[]{ SDFDatatype.BOOLEAN };
	
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()-1){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
		else{
			return accTypes;
		}
	}
}
