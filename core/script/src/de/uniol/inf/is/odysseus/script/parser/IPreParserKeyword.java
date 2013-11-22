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
package de.uniol.inf.is.odysseus.script.parser;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


/**
 * Schnittstelle, welches ein Schlüsselwort für den Preparser darstellt. Wird
 * vom QueryTextParser verwendet. Dadurch kann der Nutzer eigene Befehle
 * definieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IPreParserKeyword {

	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException;
	public Object execute(Map<String, Object> variables, String parameter, ISession caller, Context context) throws OdysseusScriptException;
	
	void setParser(IOdysseusScriptParser parser);
	IOdysseusScriptParser getParser();	
	public Collection<String> getAllowedParameters(ISession caller);
	
	public boolean isDeprecated();
	public String getDeprecationInfo();
	
}
