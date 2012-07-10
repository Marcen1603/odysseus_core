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
package de.uniol.inf.is.odysseus.core.server.udf;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;

@UserDefinedFunction(name="ELEMENT_SAMPLING")
public class ElementSampling<R> implements IUserDefinedFunction<R, R> {

	int everyNthObject;
	int objectsRead;
	
	/*
	 * 
	 * initString: Send each nth Element
	 */
	@Override
	public void init(String initString) {
		everyNthObject = Integer.parseInt(initString);
		objectsRead = 0;
	}

	@Override
	public R process(R in, int port) {
		objectsRead++;
		if (objectsRead % everyNthObject  == 0){
			return in;
		}
		return null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
