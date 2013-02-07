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
import java.util.Map;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * Realisiert das TRANSCFG-Schlüsselwort für den PreParser. 
 * 
 * @author Timo Michelsen, Marco Grawunder
 * 
 */
public class TransCfgPreParserKeyword extends AbstractPreParserExecutorKeyword {

	public static final String TRANSCFG = "TRANSCFG";	
	
	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		if (parameter.length() == 0)
			throw new OdysseusScriptException("Parameter needed for #"+TRANSCFG);

		variables.put(TRANSCFG, parameter);
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		variables.put(TRANSCFG, parameter);
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters() {
		try {
			return getServerExecutor().getQueryBuildConfigurationNames();
		} catch (OdysseusScriptException e) {			
			return new ArrayList<>();
		}			
	}

}
