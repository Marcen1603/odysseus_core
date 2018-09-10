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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * Realisiert das Priority-Schlüsselwort für den PreParser.
 *
 * @author Timo Michelsen, Marco Grawunder
 *
 */
public class QueryParamPreParserKeyword extends AbstractPreParserKeyword {

	public static final String NAME = "QPARAM";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"+NAME);

		String[] params = getSimpleParameters(parameter);
		if (params.length != 2 || Strings.isNullOrEmpty(params[1]))
			throw new OdysseusScriptException("Key value pair needed for "+NAME+" instead of "+parameter);

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		@SuppressWarnings("unchecked")
		Map<String, String> qParams = (Map<String, String>) variables.get(NAME);
		if (qParams == null){
			qParams = new HashMap<>();
			variables.put(NAME, qParams);
		}
		String[] params = getSimpleParameters(parameter);

		qParams.put(params[0], params[1]);

		return null;
	}

}
