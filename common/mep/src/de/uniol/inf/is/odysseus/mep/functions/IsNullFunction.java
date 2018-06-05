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
package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class IsNullFunction extends AbstractFunction<Boolean> implements IHasAlias{

	private static final long serialVersionUID = 4074484016029763344L;

	public IsNullFunction() {
		super("isNull",1, getAllTypes(1),SDFDatatype.BOOLEAN);
	}
	
	@Override
	public Boolean getValue() {
		return getInputValue(0) == null;
	}
	
	@Override
	public String getAliasName() {
		return "isBound";
	}

}
