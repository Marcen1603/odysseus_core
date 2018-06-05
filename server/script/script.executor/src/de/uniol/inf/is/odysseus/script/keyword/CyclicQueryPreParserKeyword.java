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

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class CyclicQueryPreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String CYCLICQUERY = "CYCLICQUERY";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		try {
			String parserID = (String) variables.get("PARSER");
			if (parserID == null)
				throw new OdysseusScriptException("Parser not set");
			if (!executor.getSupportedQueryParsers(caller).contains(parserID))
				throw new OdysseusScriptException("Parser " + parserID
						+ " not found");
			String transCfg = (String) variables.get("TRANSCFG");
			if (transCfg == null)
				throw new OdysseusScriptException(
						"TransformationConfiguration not set");

		} catch (Exception ex) {
			throw new OdysseusScriptException(
					"Unknown Exception during validation a cyclic query", ex);
		}
	}

	@Override
	public List<IExecutorCommand>  execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

		throw new QueryParseException("The keyword "+CYCLICQUERY+" can currently not be used!");
	}

}
