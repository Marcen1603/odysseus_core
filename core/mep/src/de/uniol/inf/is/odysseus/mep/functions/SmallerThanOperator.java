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

public class SmallerThanOperator extends AbstractBinaryOperator<Boolean> {

	@Override
	public int getPrecedence() {
		return 8;
	}

	@Override
	public String getSymbol() {
		return "<";
	}

	@Override
	public Boolean getValue() {
		Double val0 = getNumericalInputValue(0);
		Double val1 = getNumericalInputValue(1);
//		System.out.println(val0 + " < " + val1);
		return val0 < val1;
	}

	@Override
	public Class<Boolean> getReturnType() {
		return Boolean.class;
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
	
	public Class<?>[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()-1){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
		else{
			Class<?>[] accTypes = new Class<?>[2];
			accTypes[0] = Number.class; // number comparison
			accTypes[1] = String.class; // alphabetical order
			return accTypes;
		}
	}
}
