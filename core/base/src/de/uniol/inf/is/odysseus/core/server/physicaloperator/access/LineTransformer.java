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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class LineTransformer extends AbstractTransformer<String, String[]> implements IToStringArrayTransformer<String> {

	@Override
	public ITransformer<String, String[]> getInstance(
			Map<String, String> options, SDFSchema schema) {
		return new LineTransformer();
	}
	
	@Override
	public String[] transform(String input) {
		String[] splittedLine;
		splittedLine = new String[1];
		splittedLine[0] = input;
		return splittedLine;
	}
	
	@Override
	public String getName() {
		return "LineToString";
	}

}
