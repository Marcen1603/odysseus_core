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
package de.uniol.inf.is.odysseus.usermanagement.filestore.service.impl;

import java.io.IOException;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class UserManagementServiceImpl extends AbstractUserManagement<User, Role, Privilege> {

	private OdysseusConfiguration config = OdysseusConfiguration.instance;

	@Override
	protected Role createEmptyRole() {
		return new Role();
	}

	@Override
	protected User createEmptyUser() {
		return new User();
	}

	@Override
	protected Privilege createEmptyPrivilege() {
		return new Privilege();
	}

	protected void activate(ComponentContext context) {
	}

	protected void deactivate(ComponentContext context) {

	}

	@Override
	protected IGenericDAO<User, String> getUserDAO(ITenant tenant) {
		try {
			return UserDAO.getInstance(tenant);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected IGenericDAO<Role, String> getRoleDAO(ITenant tenant) {
		try {
			return RoleDAO.getInstance(tenant);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected IGenericDAO<Privilege, String> getPrivilegeDAO(ITenant tenant) {
		try {
			return PrivilegeDAO.getInstance(tenant);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getType() {
		if (config.get("StoretypeUserMgmt").equalsIgnoreCase("Filestore")) {
			return "Filestore";
		} else {
			return "Memorystore";
		}
	}

	@Override
	protected void process_init(ITenant tenant) {
		initDefaultUsers(tenant);
	}

	
	void setConfig(OdysseusConfiguration config) {
		this.config = config;
	}

	@Override
	public IUser findUser(ITenant tenant, String username) {
		try {
			return UserDAO.getInstance(tenant).findByName(username);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
