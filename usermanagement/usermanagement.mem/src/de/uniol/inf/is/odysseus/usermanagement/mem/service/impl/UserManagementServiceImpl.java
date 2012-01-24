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
		
		if (userDAO.findByName("System") == null) {
			try {
				User user = new User();
				user.setName("System");
				user.setPassword("manager".getBytes());
				user = userDAO.create(user);

				// --- ADMIN ROLE ----
				Role adminRole = new Role();
				adminRole.setName("sys_admin");
				adminRole = roleDAO.create(adminRole);

				Privilege adminPrivilege = new Privilege();
				adminPrivilege.setObjectURI(UserManagementPermission.objectUri);
				for (IPermission permission : UserManagementPermission.class
						.getEnumConstants()) {
					adminPrivilege.addPermission(permission);
				}
				adminPrivilege = privilegeDAO.create(adminPrivilege);
				adminRole.addPrivilege(adminPrivilege);
				roleDAO.update(adminRole);

				// --- Data dictionary Role ----
				Role dictionaryRole = new Role();
				dictionaryRole.setName("datadictionary");
				dictionaryRole = roleDAO.create(dictionaryRole);

				Privilege dictPrivilege = new Privilege();
				dictPrivilege.setObjectURI(DataDictionaryPermission.objectURI);
				for (IPermission permission : DataDictionaryPermission.class
						.getEnumConstants()) {
					dictPrivilege.addPermission(permission);
				}
				dictPrivilege = privilegeDAO.create(dictPrivilege);
				dictionaryRole.addPrivilege(dictPrivilege);
				roleDAO.update(dictionaryRole);

				// --- Configuration Role ----
				Role configurationRole = new Role();
				configurationRole.setName("configuration");
				configurationRole = roleDAO.create(configurationRole);

				Privilege confPrivilege = new Privilege();
				confPrivilege.setObjectURI(ConfigurationPermission.objectURI);
				for (IPermission permission : ConfigurationPermission.class
						.getEnumConstants()) {
					confPrivilege.addPermission(permission);
				}
				confPrivilege = privilegeDAO.create(confPrivilege);
				configurationRole.addPrivilege(confPrivilege);
				roleDAO.update(configurationRole);

				// --- Query Execution Role ----
				Role queryexecutor = new Role();
				queryexecutor.setName("queryexecutor");
				queryexecutor = roleDAO.create(queryexecutor);

				Privilege execPrivilege = new Privilege();
				execPrivilege.setObjectURI(ExecutorPermission.objectURI);
				for (IPermission permission : ExecutorPermission.class
						.getEnumConstants()) {
					execPrivilege.addPermission(permission);
				}
				execPrivilege = privilegeDAO.create(execPrivilege);
				queryexecutor.addPrivilege(execPrivilege);
				roleDAO.update(queryexecutor);

				user.addRole(adminRole);
				user.addRole(dictionaryRole);
				user.addRole(configurationRole);
				user.addRole(queryexecutor);
				user.setActive(true);

				// TEST
				Privilege tmp = new Privilege();
				tmp.setObjectURI(null);
				tmp.addPermission(ExecutorPermission.ADD_QUERY);
				user.addPrivilege(tmp);

				userDAO.update(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void deactivate(ComponentContext context) {

	}


}
