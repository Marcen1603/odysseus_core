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
package de.uniol.inf.is.odysseus.usermanagement.mem.service.impl;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IGenericDAO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.TenantDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Tenant;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.User;

public class UserManagementServiceImpl extends
		AbstractUserManagement<Tenant, User, Role, Privilege> implements
		IUserManagement {

	ISessionManagement sessionMgmt;

	
	@Override
	protected Tenant createEmptyTenant() {
		return new Tenant();
	}
	
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

	@Override
	protected IGenericDAO<User, String> getUserDAO(ITenant tenant) {
		return UserDAO.getInstance(tenant);
	}

	@Override
	protected IGenericDAO<Role, String> getRoleDAO(ITenant tenant) {
		return RoleDAO.getInstance(tenant);
	}

	@Override
	protected IGenericDAO<Privilege, String> getPrivilegeDAO(ITenant tenant) {
		return PrivilegeDAO.getInstance(tenant);
	}
	
	@Override
	protected IGenericDAO<Tenant, String> getTenantDAO() {
		return TenantDAO.getInstance();
	}
	
	@Override
	public String getType() {
		return "Memorystore";
	}

	@Override
	protected void process_init() {
		initDefaultUsers();	
	}
	
	protected void activate(ComponentContext context) {
		// is needed to satisfy
	}

	protected void deactivate(ComponentContext context) {

	}

	@Override
	public ISessionManagement getSessionManagement() {
		if (sessionMgmt == null) {
			sessionMgmt = new SessionManagementServiceImpl();
		}
		return sessionMgmt;
	}

}
