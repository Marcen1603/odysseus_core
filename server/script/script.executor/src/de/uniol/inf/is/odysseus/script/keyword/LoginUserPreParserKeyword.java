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
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class LoginUserPreParserKeyword extends AbstractPreParserKeyword{

	public static final String LOGIN = "LOGIN";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		// kann hier nicht validieren, da es sein kann, dass in der gleichen anfrage zuvor
		// erst der Nutzer angelegt wurde.
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		String[] para = getSimpleParameters(parameter);
		String userName = para[0];
		String password = para[1];
		
		ISession user = null;
		if (password != null && password.length() > 0){
			user = executor.login(userName, password.getBytes(), caller.getTenant().getName());
		}
		if( user == null ) {
			throw new OdysseusScriptException("Login with user " + userName + " failed");
		}
		
		// In den Variablen als aktiven User merken
		variables.put("USER", user);
		
		return null;
	}

}
