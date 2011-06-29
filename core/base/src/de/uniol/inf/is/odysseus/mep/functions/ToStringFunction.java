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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ToStringFunction extends AbstractFunction<String> {

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "toString";
	}

	@Override
	public String getValue() {
		return Double.toString(getNumericalInputValue(0));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
	}
	
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
		else{
			SDFDatatype[] accTypes = new SDFDatatype[4];
			accTypes[0] = SDFDatatype.INTEGER;
			accTypes[1] = SDFDatatype.LONG;
			accTypes[2] = SDFDatatype.FLOAT;
			accTypes[3] = SDFDatatype.DOUBLE;
			return accTypes;
		}
	}

}
