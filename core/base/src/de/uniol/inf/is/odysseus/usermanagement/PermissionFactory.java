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

import de.uniol.inf.is.odysseus.ConfigurationPermission;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryPermission;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorPermission;

public class PermissionFactory {

	static public IPermission valueOf(String v) {
		String value = v.toUpperCase();
		IPermission permission = null;
		try {
			permission = DataDictionaryPermission.valueOf(value);
		} catch (IllegalArgumentException e) {}
		if (permission != null) return permission;
		
		try {
			permission = UserManagementPermission.valueOf(value);
		} catch (IllegalArgumentException e) {}
		if (permission != null) return permission;

		try {
			permission = ExecutorPermission.valueOf(value);
		} catch (IllegalArgumentException e) {}
		if (permission != null) return permission;

		try {
			permission = ConfigurationPermission.valueOf(value);
		} catch (IllegalArgumentException e) {}
		if (permission != null) return permission;

		return null;
	}

	public static boolean needsNoObject(IPermission action) {
		if (action instanceof DataDictionaryPermission) {
			return DataDictionaryPermission.needsNoObject(action);
		}
		if (action instanceof UserManagementPermission) {
			return UserManagementPermission.needsNoObject(action);
		}
		if (action instanceof ExecutorPermission){
			return ExecutorPermission.needsNoObject(action);
		}
		if (action instanceof ConfigurationPermission){
			return ConfigurationPermission.needsNoObject(action);
		}
		return false;
	}

}
