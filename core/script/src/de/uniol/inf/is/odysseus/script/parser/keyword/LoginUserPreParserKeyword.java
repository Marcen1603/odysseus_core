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
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class LoginUserPreParserKeyword extends AbstractPreParserKeyword{

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		// kann hier nicht validieren, da es sein kann, dass in der gleichen anfrage zuvor
		// erst der Nutzer angelegt wurde.
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		String[] para = getSimpleParameters(parameter);
		String userName = para[0];
		String password = para[1];
		
		ISession user = null;
		if (password != null && password.length() > 0){
			user = UserManagement.getSessionmanagement().login(userName, password.getBytes());
		}
//		else{
//			user = UserManagement.getSessionmanagement().login(userName, caller);			
//		}
		if( user == null ) 
			throw new OdysseusScriptException("Login with user " + userName + " failed");
				
		// In den Variablen als aktiven User merken
		variables.put("USER", user);
		
		return user;
	}

}
