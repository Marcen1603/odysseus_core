/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.usermanagement.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.ConfigurationPermission;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryPermission;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.usermanagement.AbstractUserManagement;
import de.uniol.inf.is.odysseus.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.usermanagement.UserManagementPermission;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.PrivilegeImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.RoleImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class UserManagementServiceImpl extends AbstractUserManagement<UserImpl,RoleImpl,PrivilegeImpl> implements IUserManagement {
	private EntityManagerFactory entityManagerFactory;

	@Override
	protected UserImpl createEmptyUser() {
		return new UserImpl();
	}
	
	@Override
	protected RoleImpl createEmptyRole() {
		return new RoleImpl();
	}
	
	@Override
	protected PrivilegeImpl createEmptyPrivilege(){
		return new PrivilegeImpl();
	}

	protected void activate(ComponentContext context) {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("odysseusPU");
		final EntityManager em = this.entityManagerFactory
				.createEntityManager();
		
		userDAO = new UserDAO();
		roleDAO = new RoleDAO();
		privilegeDAO = new PrivilegeDAO();

		
		((UserDAO)userDAO).setEntityManager(em);
		((RoleDAO)roleDAO).setEntityManager(em);
		((PrivilegeDAO)privilegeDAO).setEntityManager(em);
		if (userDAO.findByName("System") == null) {
			try {
				UserImpl user = new UserImpl();
				user.setName("System");
				user.setPassword("manager".getBytes());
				user = userDAO.create(user);

				// --- ADMIN ROLE ----
				RoleImpl adminRole = new RoleImpl();
				adminRole.setName("sys_admin");
				adminRole = roleDAO.create(adminRole);

				PrivilegeImpl adminPrivilege = new PrivilegeImpl();
				adminPrivilege.setObjectURI(UserManagementPermission.objectUri);
				for (IPermission permission : UserManagementPermission.class
						.getEnumConstants()) {
					adminPrivilege.addPermission(permission);
				}
				adminPrivilege = privilegeDAO.create(adminPrivilege);
				adminRole.addPrivilege(adminPrivilege);
				roleDAO.update(adminRole);

				// --- Data dictionary Role ----
				RoleImpl dictionaryRole = new RoleImpl();
				dictionaryRole.setName("datadictionary");
				dictionaryRole = roleDAO.create(dictionaryRole);

				PrivilegeImpl dictPrivilege = new PrivilegeImpl();
				dictPrivilege.setObjectURI(DataDictionaryPermission.objectURI);
				for (IPermission permission : DataDictionaryPermission.class
						.getEnumConstants()) {
					dictPrivilege.addPermission(permission);
				}
				dictPrivilege = privilegeDAO.create(dictPrivilege);
				dictionaryRole.addPrivilege(dictPrivilege);
				roleDAO.update(dictionaryRole);

				// --- Configuration Role ----
				RoleImpl configurationRole = new RoleImpl();
				configurationRole.setName("configuration");
				configurationRole = roleDAO.create(configurationRole);

				PrivilegeImpl confPrivilege = new PrivilegeImpl();
				confPrivilege.setObjectURI(ConfigurationPermission.objectURI);
				for (IPermission permission : ConfigurationPermission.class
						.getEnumConstants()) {
					confPrivilege.addPermission(permission);
				}
				confPrivilege = privilegeDAO.create(confPrivilege);
				configurationRole.addPrivilege(confPrivilege);
				roleDAO.update(configurationRole);

				// --- Query Execution Role ----
				RoleImpl queryexecutor = new RoleImpl();
				queryexecutor.setName("queryexecutor");
				queryexecutor = roleDAO.create(queryexecutor);

				PrivilegeImpl execPrivilege = new PrivilegeImpl();
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
				PrivilegeImpl tmp = new PrivilegeImpl();
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
