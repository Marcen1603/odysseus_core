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
package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryAction;

public class UserActionFactory {
	
	static public IUserAction valueOf(String value){
		IUserAction action = DataDictionaryAction.valueOf(value);
		if (action != null){
			return action;
		}else{
			action = UserManagementAction.valueOf(value);
			return action;
		}
	}

	public static boolean needsNoObject(IUserAction action) {
		if (action instanceof DataDictionaryAction){
			return DataDictionaryAction.needsNoObject(action);
		}
		if (action instanceof UserManagementAction){
			return UserManagementAction.needsNoObject(action);
		}
		return false;
	}

	public static String getAliasObject(IUserAction action) {
		if (action instanceof DataDictionaryAction){
			return DataDictionaryAction.alias;
		}
		if (action instanceof UserManagementAction){
			return UserManagementAction.alias;
		}		
		
		return null;
	}
	
	
	
	
}
