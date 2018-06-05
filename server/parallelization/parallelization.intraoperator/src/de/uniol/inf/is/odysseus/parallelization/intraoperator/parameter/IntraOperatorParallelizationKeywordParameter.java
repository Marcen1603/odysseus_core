/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter;

import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;

/**
 * Defines the parameters for the #INTRAOPERATOR keyword. This parameter is used
 * in parameter helper for parsing the keyword parameters
 * 
 * @author ChrisToenjesDeye
 *
 */
public enum IntraOperatorParallelizationKeywordParameter implements
		IKeywordParameter {
	OPERATORID("id", 0, false), DEGREE_OF_PARALLELIZATION("degree", 1, false), BUFFERSIZE(
			"buffersize", 2, true);

	private String name;
	private int position;
	private boolean isOptional;

	private IntraOperatorParallelizationKeywordParameter(String name,
			int position, boolean isOptional) {
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public boolean isOptional() {
		return isOptional;
	}

}
