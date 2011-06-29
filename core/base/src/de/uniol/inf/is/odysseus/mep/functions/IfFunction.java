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

public class IfFunction extends AbstractFunction<Object> {

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "eif";
	}

	@Override
	public Object getValue() {
		return getInputValue(0) ? getInputValue(1) : getInputValue(2);
	}

	@Override
	public String getReturnType() {
		// if then and else arguments have the same type, we are sure to return
		// a value of that type
		if (getArguments()[1].getReturnType() == getArguments()[2].getReturnType()) {
			return getArguments()[1].getReturnType();
		}
		// otherwise we make no guarantees
		return "Object";
	}
	
	public String[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException("abs has only 1 argument.");
		}
		else{
			String[] accTypes = new String[1];
			switch (argPos){
			case 0: accTypes[0] = "Boolean";
					break;
			case 1:	accTypes[0] = "Object";
					break;
			case 2: accTypes[0] = "Object";
					break;
			}
			
			return accTypes;
		}
	}

}
