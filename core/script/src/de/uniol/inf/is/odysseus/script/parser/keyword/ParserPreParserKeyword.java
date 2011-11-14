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
package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Map;

import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * Auswahl des Parsers
 * 
 * @author Timo Michelsen
 * 
 */
public class ParserPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			User caller) throws OdysseusScriptParseException {
		if (parameter.length() == 0)
			throw new OdysseusScriptParseException(
					"Parameter needed for #PARSER");
		variables.put("PARSER", parameter);
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			User caller) throws OdysseusScriptParseException {
		variables.put("PARSER", parameter);
		return null;
	}
}
