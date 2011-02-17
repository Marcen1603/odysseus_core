/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;

public abstract class AbstractQueryPreParserKeyword extends
		AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		try {
			IExecutor executor = ExecutorHandler.getExecutor();
			if (executor == null)
				throw new QueryTextParseException("No executor found");

			if (parameter.length() == 0)
				throw new QueryTextParseException("Encountered empty query");

			String parserID = (String)variables.get("PARSER");
			if (parserID == null)
				throw new QueryTextParseException("Parser not set");
			if (!executor.getSupportedQueryParsers().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = (String) variables.get("TRANSCFG");
			if (transCfg == null)
				throw new QueryTextParseException("TransformationConfiguration not set");
			if (executor.getQueryBuildConfiguration(transCfg) == null)
				throw new QueryTextParseException("TransformationConfiguration " + transCfg + " not found");
		} catch (Exception ex) {
			throw new QueryTextParseException("Unknown Exception during validation a query", ex);
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		String parserID = (String) variables.get("PARSER");
		String transCfg = (String) variables.get("TRANSCFG");

		try {
			return exec(parserID, transCfg, parameter, getCurrentUser(variables), getDataDictionary());
		} catch (Exception ex) {
			throw new QueryTextParseException("Error during executing query", ex);
		}
	}

	protected abstract Object exec(String parserID, String transCfg, String parameter, User user, IDataDictionary dd) throws PlanManagementException;



}
