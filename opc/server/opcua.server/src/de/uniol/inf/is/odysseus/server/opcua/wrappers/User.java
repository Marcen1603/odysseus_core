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

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.Format;
import com.xafero.turjumaan.server.java.api.NotCacheable;
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * One user in Odysseus.
 */
@Description("An user")
public class User {

	/** The user. */
	private final IUser user;

	/**
	 * Instantiates a new user.
	 *
	 * @param user
	 *            the user
	 */
	public User(IUser user) {
		this.user = user;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Description("The id of this entity")
	public String getId() {
		return user.getId();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Description("The name of the user")
	public String getName() {
		return user.getName();
	}

	/**
	 * Gets the privileges.
	 *
	 * @return the privileges
	 */
	@NotCacheable
	@ResponseFormat(Format.JSON)
	@Description("The privileges of the user")
	public List<? extends IPrivilege> getPrivileges() {
		return user.getPrivileges();
	}

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	@NotCacheable
	@ResponseFormat(Format.JSON)
	@Description("The roles of the user")
	public List<? extends IRole> getRoles() {
		return user.getRoles();
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@Description("The version of this entity")
	public Long getVersion() {
		return user.getVersion();
	}
}