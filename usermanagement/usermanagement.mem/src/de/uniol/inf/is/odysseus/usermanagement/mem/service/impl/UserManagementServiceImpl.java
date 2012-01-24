package de.uniol.inf.is.odysseus.usermanagement.mem.service.impl;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.ConfigurationPermission;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryPermission;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.usermanagement.UserManagementPermission;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.mem.service.domain.User;


public class UserManagementServiceImpl extends AbstractUserManagement<User,Role, Privilege> {

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
		
		userDAO = UserDAO.getInstance();
		roleDAO = RoleDAO.getInstance();
		privilegeDAO = PrivilegeDAO.getInstance();
		
		initDefaultUsers();		
	}

	protected void deactivate(ComponentContext context) {

	}


}
