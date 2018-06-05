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
package de.uniol.inf.is.odysseus.parallelization.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.preexecution.IParallelizationPreExecutionHandler;
import de.uniol.inf.is.odysseus.parallelization.preexecution.ParallelizationPreExecutionHandlerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * Odysseus Script keyword #PARALLELIZATION. This class provides global
 * parallelization functionality in Odysseus script and also validates the given
 * parameters for this keyword. It also creates transformation handlers for
 * processing of parallelization
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelizationPreParserKeyword extends AbstractPreParserKeyword {
	public static final String KEYWORD = "PARALLELIZATION";

	private static final String PARALLELIZATION_TYPE = "type";

	private IParallelizationPreExecutionHandler handler;

	/**
	 * Validates the given parameters for this keyword
	 */
	@Override
	public void validate(Map<String, Object> variables, String parameterString,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		// get type parameter from keyword
		String[] split = parameterString.trim().split(" ");
		String parallelizationType = "";
		for (int i = 0; i < split.length; i++) {
			String parameterBeginning = "(" + PARALLELIZATION_TYPE + "=";
			if (split[i].trim().startsWith(parameterBeginning)) {
				;
				parallelizationType = split[i].substring(
						parameterBeginning.length(), split[i].length() - 1);
			}
		}
		if (parallelizationType.isEmpty()) {
			// if parameters are not defined via names, get the first one
			parallelizationType = split[0];
		}

		// validate if pre execution handler exists
		if (ParallelizationPreExecutionHandlerRegistry
				.isValidType(parallelizationType)) {
			// get preExecution handler and validate the complete parameter string
			handler = ParallelizationPreExecutionHandlerRegistry
					.getPreExecutionHandlerByType(parallelizationType);
			handler.validateParameters(parameterString);
		} else {
			throw new OdysseusScriptException(
					"value for "
							+ PARALLELIZATION_TYPE
							+ " is invalid. Valid values are: "
							+ ParallelizationPreExecutionHandlerRegistry
									.getValidTypes());
		}

	}

	/**
	 * Parses the given parameters and create needed transformation handler
	 * parameters
	 */
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameterString, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		// get settings and do pre execution (inter or intra-operator parallelization)
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		handler.preExecute(parameterString, settings);

		return null;
	}
}
