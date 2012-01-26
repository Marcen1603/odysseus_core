package de.uniol.inf.is.odysseus.usermanagement.filestore.service.impl;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.dao.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Privilege;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.Role;
import de.uniol.inf.is.odysseus.usermanagement.filestore.service.domain.User;

public class UserManagementServiceImpl extends
		AbstractUserManagement<User, Role, Privilege> {

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
		try {
			userDAO = UserDAO.getInstance();
			roleDAO = RoleDAO.getInstance();
			privilegeDAO = PrivilegeDAO.getInstance();

			initDefaultUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void deactivate(ComponentContext context) {

	}

}
