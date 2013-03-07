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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class IfFunction extends AbstractFunction<Object> {

	private static final Logger LOG = LoggerFactory.getLogger(IfFunction.class);
	private static final long serialVersionUID = 7330905069703118113L;

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
	public SDFDatatype getReturnType() {
		// if then and else arguments have the same type, we are sure to return
		// a value of that type
		SDFDatatype type1 = getArguments()[1].getReturnType();
		SDFDatatype type2 = getArguments()[2].getReturnType();
		if (type1 == type2) {
			return getArguments()[1].getReturnType();
		}
		
		if( type1.compatibleTo(type2)) {
			return type2;
		} else if( type2.compatibleTo(type1)) {
			return type1;
		} else {
			LOG.warn("Incompatible return types in if-Function");
			return SDFDatatype.OBJECT;
		}
	}
	
	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
															{SDFDatatype.BOOLEAN},
															{SDFDatatype.OBJECT},
															{SDFDatatype.OBJECT}};
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 2){
			throw new IllegalArgumentException("iffunction has 3 arguments.");
		}
        return accTypes[argPos];
	}

}
