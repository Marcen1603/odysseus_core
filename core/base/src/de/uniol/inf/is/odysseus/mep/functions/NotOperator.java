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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class NotOperator extends AbstractUnaryOperator<Boolean> {

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "!";
	}

	@Override
	public Boolean getValue() {
		return !((Boolean) getInputValue(0));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.RIGHT_TO_LEFT;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[]{SDFDatatype.BOOLEAN};
	
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException("! has only 1 argument.");
		}
		else{
			SDFDatatype[] accTypes = new SDFDatatype[1];
			accTypes[0] = SDFDatatype.BOOLEAN;
			return accTypes;
		}
	}


}
