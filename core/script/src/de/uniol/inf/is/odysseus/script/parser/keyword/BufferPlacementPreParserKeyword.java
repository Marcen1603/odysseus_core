/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class BufferPlacementPreParserKeyword extends AbstractPreParserKeyword {

	public static final String BUFFERPLACEMENT = "BUFFERPLACEMENT";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
		
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		addSettings.add(new ParameterBufferPlacementStrategy(parameter));
		return null;
	}

}
