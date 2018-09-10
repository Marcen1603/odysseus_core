/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.server.opcua.wrappers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionStore;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * The user manager of Odysseus.
 */
@Description("The user manager")
public class UserManager {

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("All roles")
	public Map<String, Role> getRoles() {
		List<? extends IRole> roles = getManager().getRoles(getAdminSession());
		Map<String, Role> map = new TreeMap<>();
		for (IRole role : roles)
			map.put(role.getName(), new Role(role));
		return map;
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("All users")
	public Map<String, User> getUsers() {
		List<? extends IUser> users = getManager().getUsers(getAdminSession());
		Map<String, User> map = new TreeMap<>();
		for (IUser user : users)
			map.put(user.getName(), new User(user));
		return map;
	}

	/**
	 * Gets the sessions.
	 *
	 * @return the sessions
	 */
	@NotCacheable
	@ResponseFormat(Format.XML)
	@Description("All sessions")
	public Map<String, ISession> getSessions() {
		SessionStore store = SessionStore.getInstance();
		Map<String, ISession> map = new TreeMap<>();
		for (Entry<String, ISession> e : store.entrySet())
			map.put(e.getKey(), e.getValue());
		return map;
	}

	/**
	 * Gets the manager.
	 *
	 * @return the manager
	 */
	protected IUserManagement getManager() {
		return UserManagementProvider.instance.getUsermanagement(true);
	}

	/**
	 * Gets the admin session.
	 *
	 * @return the admin session
	 */
	protected ISession getAdminSession() {
		return SessionManagement.instance.loginSuperUser(null);
	}
}