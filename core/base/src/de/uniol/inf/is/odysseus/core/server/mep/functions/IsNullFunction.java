/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class IsNullFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = 4074484016029763344L;

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public String getSymbol() {
		return "isNull";
	}

	@Override
	public Boolean getValue() {
		return getInputValue(0) == null;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}
	
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos){
		// Accept all types ... TODO: Is this a problem?
        return null;
	}
}
