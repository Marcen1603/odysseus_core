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

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class AbsoluteFunction extends AbstractFunction<Double> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "abs";
	}

	@Override
	public Double getValue() {
		return Math.abs(getNumericalInputValue(0));
	}

	@Override
	public String getReturnType() {
		return "Double";
	}
	
	public String[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException("abs has only 1 argument.");
		}
		else{
			String[] accTypes = new String[4];
			accTypes[0] = "Integer";
			accTypes[1] = "Long";
			accTypes[2] = "Float";
			accTypes[3] = "Double";
			return accTypes;
		}
	}

}
