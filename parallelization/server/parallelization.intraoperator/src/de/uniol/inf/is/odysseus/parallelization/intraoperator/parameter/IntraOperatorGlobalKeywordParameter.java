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
 * Defines the parameters for the #PARALLELIZATION keyword if the type is set to
 * intra-operator. This parameter is used in parameter helper for parsing the
 * keyword parameters
 * 
 * @author ChrisToenjesDeye
 *
 */
public enum IntraOperatorGlobalKeywordParameter implements IKeywordParameter {
	PARALLELIZATION_TYPE("type", 0, false), DEGREE_OF_PARALLELIZATION("degree",
			1, false), BUFFERSIZE("buffersize", 2, true);

	private IntraOperatorGlobalKeywordParameter(String name, int position,
			boolean isOptional) {
		this.name = name;
		this.position = position;
		this.isOptional = isOptional;
	}

	private String name;
	private Integer position;
	private boolean isOptional;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getPosition() {
		return this.position;
	}

	@Override
	public boolean isOptional() {
		return this.isOptional;
	}

	public static IntraOperatorGlobalKeywordParameter getParameterByName(
			String parallelizationParameter) {
		for (IntraOperatorGlobalKeywordParameter parameter : IntraOperatorGlobalKeywordParameter
				.values()) {
			if (parameter.name().equalsIgnoreCase(parallelizationParameter)) {
				return parameter;
			}
		}
		return null;
	}
}