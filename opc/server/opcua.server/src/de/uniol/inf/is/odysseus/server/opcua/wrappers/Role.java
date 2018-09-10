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
import com.xafero.turjumaan.server.java.api.ResponseFormat;

import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * One role in Odysseus.
 */
@Description("A role")
public class Role {

	/** The role. */
	private final IRole role;

	/**
	 * Instantiates a new role.
	 *
	 * @param role
	 *            the role
	 */
	public Role(IRole role) {
		this.role = role;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Description("The id of this entity")
	public String getId() {
		return role.getId();
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Description("The name of the role")
	public String getName() {
		return role.getName();
	}

	/**
	 * Gets the privileges.
	 *
	 * @return the privileges
	 */
	@ResponseFormat(Format.JSON)
	@Description("The privileges of the role")
	public List<? extends IPrivilege> getPrivileges() {
		return role.getPrivileges();
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@Description("The version of this entity")
	public Long getVersion() {
		return role.getVersion();
	}
}