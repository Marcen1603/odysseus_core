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

import de.uniol.inf.is.odysseus.core.server.OdysseusDefaults;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class OdysseusDefaultsPreParserKeyword extends AbstractPreParserKeyword {

	public static final String ODYSSEUS_PARAM = "ODYSSEUS_PARAM";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
		// Ignore
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller)
			throws OdysseusScriptException {
		String[] params = getSimpleParameters(parameter);
		if (params.length >= 2) {
			boolean permanently = false;
			if (params.length == 3) {
				if ("TRUE".equalsIgnoreCase(params[2])) {
					permanently = true;
				}
			}
			OdysseusDefaults.set(params[0],params[1],permanently, caller);
		}

		return null;
	}
}
