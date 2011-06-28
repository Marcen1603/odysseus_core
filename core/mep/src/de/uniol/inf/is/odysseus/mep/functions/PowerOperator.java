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

public class PowerOperator extends AbstractBinaryOperator<Double> {

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
	public String getReturnType() {
		return "Double";
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
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return null;
	}

	public String[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()-1){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
		else{
			String[] accTypes = null;
			switch(argPos){
			case 0: 
				accTypes = new String[4];
				accTypes[0] = "Integer";
				accTypes[1] = "Long";
				accTypes[2] = "Float";
				accTypes[3] = "Double";
				break;
			case 1:
				accTypes = new String[1];
				accTypes[0] = "Integer";
				break;
			}
			return accTypes;
		}
	}
}
