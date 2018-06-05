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
package de.uniol.inf.is.odysseus.script.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Merlin Wasmann
 *
 */
public class PlanGenerationMethodPreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String PLANGENERATIONMETHOD = "PLANGENERATIONMETHOD";
	
	private static final Collection<String> allowedParameters = new ArrayList<String>() {
		private static final long serialVersionUID = -6089179664464825837L;

	{
		add("exhaustiveSearch");
		add("dynamicProgramming");
		add("iterativeDynamicProgramming");
		add("copy");
		add("swap");
	}};
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if(parameter.length() == 0) {
			throw new OdysseusScriptException("Parameter needed for #" + PLANGENERATIONMETHOD);
		}
	}
	
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> additionalSettings = getAdditionalTransformationSettings(variables);
		Map<String, String> config = new HashMap<String, String>();
		config.put(PLANGENERATIONMETHOD, parameter);
		additionalSettings.add(new PlanGenerationConfiguration(config));
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return allowedParameters;
	}

}
