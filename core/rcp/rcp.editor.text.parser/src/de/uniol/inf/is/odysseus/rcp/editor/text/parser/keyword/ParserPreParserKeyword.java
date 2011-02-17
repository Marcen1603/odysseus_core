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

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;

/**
 * Realisiert das PARSER-Schlüsselwort für den PreParser. Wenn ein Parser
 * angegeben wird, muss dieser dem <code>IExecutor</code> bekannt sein.
 * 
 * @author Timo Michelsen
 * 
 */
public class ParserPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		if (parameter.length() == 0)
			throw new QueryTextParseException("Parameter needed for #PARSER");

		IExecutor executor = ExecutorHandler.getExecutor();
		if (executor == null)
			throw new QueryTextParseException("No executor found");
		try {
			if (!executor.getSupportedQueryParsers().contains(parameter)) {
				throw new QueryTextParseException("Parser " + parameter + " does not exist");
			}
			variables.put("PARSER", parameter);
		} catch (QueryTextParseException ex ) {
			throw ex;
		} catch (Exception ex) {
			throw new QueryTextParseException("Unknown error", ex);
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		variables.put("PARSER", parameter);
		return null;
	}
}
