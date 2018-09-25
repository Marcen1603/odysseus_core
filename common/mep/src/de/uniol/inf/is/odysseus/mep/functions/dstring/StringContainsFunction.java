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
package de.uniol.inf.is.odysseus.mep.functions.dstring;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.datatype.DString;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;


public class StringContainsFunction extends AbstractBinaryDStringFunction<Boolean> implements IHasAlias {

	private static final long serialVersionUID = -2241632788238873550L;

	public StringContainsFunction() {
		super("strcontains", SDFDatatype.BOOLEAN);
	}
	
	@Override
	public Boolean getValue() {
		return ((DString)getInputValue(0)).contains((getInputValue(1).toString()));
	}

	@Override
	public String getAliasName() {
		return "contains";
	}

}
