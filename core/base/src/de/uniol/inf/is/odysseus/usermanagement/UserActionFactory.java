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

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryPermission;

public class UserActionFactory {
	
	static public IPermission valueOf(String value){
		IPermission action = DataDictionaryPermission.valueOf(value);
		if (action != null){
			return action;
		}else{
			action = UserManagementPermission.valueOf(value);
			return action;
		}
	}

	public static boolean needsNoObject(IPermission action) {
		if (action instanceof DataDictionaryPermission){
			return DataDictionaryPermission.needsNoObject(action);
		}
		if (action instanceof UserManagementPermission){
			return UserManagementPermission.needsNoObject(action);
		}
		return false;
	}

	
	
}
