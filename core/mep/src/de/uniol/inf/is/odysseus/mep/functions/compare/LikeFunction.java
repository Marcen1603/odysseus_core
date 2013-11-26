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
package de.uniol.inf.is.odysseus.mep.functions.compare;

import de.uniol.inf.is.odysseus.mep.functions.AbstractBooleanStringFunction;


public class LikeFunction extends AbstractBooleanStringFunction{

	private static final long serialVersionUID = 7705981108537461304L;

	@Override
	public String getSymbol() {
		return "strlike";
	}

	@Override
	public Boolean getValue() {
		return ((String)getInputValue(0)).matches((String)getInputValue(1));
	}

}
