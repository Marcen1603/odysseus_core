/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.usermanagement;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.usermanagement.IAbstractEntity;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

public abstract class AbstractRole implements IRole {

	private static final long serialVersionUID = -2633056745390718448L;
	private String name;
	/** The privileges of the role */
	final private List<IPrivilege> privileges = new ArrayList<IPrivilege>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Role#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Role#getPrivileges()
	 */
	@Override
	public List<IPrivilege> getPrivileges() {
		return this.privileges;
	}

	/**
	 * @param privilege
	 */
	@Override
	public void addPrivilege(final IPrivilege privilege) {
		this.privileges.add(privilege);
	}

	/**
	 * @param privilege
	 */
	public void removePrivilege(final IPrivilege privilege) {
		this.privileges.remove(privilege);
	}

	/**
	 * @param privileges
	 *            The privileges to set.
	 */
	public void setPrivileges(final List<IPrivilege> privileges) {
		this.privileges.clear();
		this.privileges.addAll(privileges);
	}

	@Override
	public void update(IAbstractEntity entity) {
		if (entity instanceof AbstractRole){
			AbstractRole role = (AbstractRole) entity;
			this.privileges.clear();
			this.privileges.addAll(role.privileges);
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(getName()).append("\n")
				.append("Privileges:\n");
		for (IPrivilege privilege : getPrivileges()) {
			sb.append("\t").append(privilege.toString()).append("\n");
		}
		return sb.toString();
	}
}
