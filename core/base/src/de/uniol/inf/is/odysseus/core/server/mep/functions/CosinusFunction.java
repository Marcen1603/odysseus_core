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
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class CosinusFunction extends AbstractFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9054387962883836702L;

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "cos";
	}

	@Override
	public Double getValue() {
		return Math.cos(getNumericalInputValue(0));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT};
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException("cos has only 1 argument.");
		}
		else{
			return accTypes;
		}
	}


}
