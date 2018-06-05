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
package de.uniol.inf.is.odysseus.script.keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

/**
 * Auswahl des Parsers
 * 
 * @author Timo Michelsen
 * 
 */
public class ParserPreParserKeyword extends AbstractPreParserKeyword {

	public static final String PARSER = "PARSER";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #PARSER");
		if (parameter.equalsIgnoreCase(OdysseusScriptParser.PARSER_NAME))
			throw new OdysseusScriptException(OdysseusScriptParser.PARSER_NAME+" cannot be used as #PARSER");

		variables.put(PARSER, parameter);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		variables.put(PARSER, parameter);
		return null;
	}

	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		// TODO
//		try {
//			Set<String> r = new TreeSet<>(getServerExecutor()
//					.getSupportedQueryParsers(caller));
//			r.remove(OdysseusScriptParser.PARSER_NAME);
//			return r;
//		} catch (PlanManagementException e) {
//			e.printStackTrace();
//		} catch (OdysseusScriptException e) {
//			e.printStackTrace();
//		}
		return new ArrayList<>();
	}
}
