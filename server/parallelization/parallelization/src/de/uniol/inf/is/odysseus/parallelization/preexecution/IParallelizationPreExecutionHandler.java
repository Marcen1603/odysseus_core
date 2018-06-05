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
package de.uniol.inf.is.odysseus.parallelization.preexecution;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * interface for pre execution handlers (parallelization). Allows easy extensibility of parallelization types
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IParallelizationPreExecutionHandler {
	
	/**
	 * Validates the given parameter string from odysseus script keyword
	 * @param parameterString
	 */
	void validateParameters(String parameterString);
	
	/**
	 * do pre execution, differs for each parallleization type
	 * @param parameterString
	 * @param settings
	 * @throws OdysseusScriptException
	 */
	void preExecute(String parameterString, List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException;
	
	/**
	 * returns the name of this paralleliaztion type. needed for osgi registry
	 * @return
	 */
	String getType();

}
