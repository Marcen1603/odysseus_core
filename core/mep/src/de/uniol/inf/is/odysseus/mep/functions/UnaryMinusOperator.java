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

import de.uniol.inf.is.odysseus.mep.AbstractUnaryOperator;

public class UnaryMinusOperator extends AbstractUnaryOperator<Double> {

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "UnaryMinus";
	}

	@Override
	public Double getValue() {
		return -getNumericalInputValue(0);
	}

	@Override
	public Class<Double> getReturnType() {
		return Double.class;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.RIGHT_TO_LEFT;
	}
	
	public Class<?>[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
		else{
			Class<?>[] accTypes = new Class<?>[1];
			accTypes[0] = Number.class;
			return accTypes;
		}
	}

}
