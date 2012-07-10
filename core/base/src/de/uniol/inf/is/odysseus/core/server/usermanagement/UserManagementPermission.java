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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;


/**
 * This class containing the actions (permissions) for the UserManagement.
 * It encapsulate the following permissions:
 * <ul>
 * <li>CREATE_USER - permission to create a new user</li>
 * <li>ALTER_USER - permission to alter an existing user</li>
 * <li>DELETE_USER - permission to delete an existing user</li>
 * <li>DEACTIVATE_USER - permission to deactivate an existing user</li>
 * <li>SET_SYSTEM_USER - permission to set a user or role to system status. With this status you can't loose permissions.</li>
 * <li>CREATE_ROLE - permission to create a new role</li>
 * <li>DELETE_ROLE - permission to delete an existing role</li>
 * <li>GRANT - permission to grant permissions the user have to other user or roles</li>
 * <li>GRANT_ALL - super permission to grant any permission to other user or roles</li>
 * <li>REVOKE - permission to revoke permissions the user have from other user or roles</li>
 * <li>REVOKE_ALL - super permission to revoke any permission from other user or roles</li>
 * <li>GRANT_ROLE - permission to grant roles the user have to other user or roles</li>
 * <li>REVOKE_ROLE - permission to revoke roles the user have to other user or roles</li>
 * <li>LOGOUT - permission to logout an other user</li>
 * <li>GET_ALL_USER - permission to get a list that contains all existing user in the system</li>
 * <li>GET_ALL - super permission to get any get-able thing (get_all_user) from UserManagement</li>
 * </ul>
 * @see de.uniol.inf.is.odysseus.core.usermanagement.AbstractUserManagement.java
 * @author Christian van Göns
 */
public enum UserManagementPermission implements IPermission {
	CREATE_USER, ALTER_USER, DELETE_USER, DEACTIVATE_USER, SET_SYSTEM_USER,

	CREATE_ROLE, DELETE_ROLE,

	GRANT, GRANT_ALL, REVOKE, REVOKE_ALL, GRANT_ROLE, REVOKE_ROLE,

	LOGOUT, GET_ALL_USER, GET_ALL, SUDO_LOGIN;


	public final static String objectUri = "usermanagement";
	
	/**
	 * returns the higher action (permission) for a given action.
	 * 
	 * @param action
	 * @return IPermission
	 */
	public synchronized static IPermission hasSuperAction(
			UserManagementPermission action) {
		switch (action) {
		case GET_ALL_USER:
			return GET_ALL;
		case GRANT:
		case GRANT_ROLE:
			return GRANT_ALL;
		case REVOKE:
		case REVOKE_ROLE:
			return REVOKE_ALL;
		default:
			return null;
		}
	}

	/**
	 * returns whether the given action (permission) operates with an objecturi
	 * or the action class alias.
	 * 
	 * @param action
	 * @return
	 */
	public static boolean needsNoObject(IPermission action) {
		switch ((UserManagementPermission) action) {
		case CREATE_USER:
			return true;
		case CREATE_ROLE:
			return true;
		case ALTER_USER:
			return true;
		case DELETE_USER:
			return true;
		case DELETE_ROLE:
			return true;
		case GRANT_ALL:
			return true;
		case GRANT_ROLE:
			return true;
		case REVOKE_ALL:
			return true;
		case REVOKE_ROLE:
			return true;
		case GET_ALL_USER:
			return true;
		case GET_ALL:
			return true;
		case LOGOUT:
			return true;
		default:
			return false;
		}
	}

}