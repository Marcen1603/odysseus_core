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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.usermanagement.IRole;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.IUser;
import de.uniol.inf.is.odysseus.usermanagement.IUserAction;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagement;
import de.uniol.inf.is.odysseus.usermanagement.UserManagementAction;
import de.uniol.inf.is.odysseus.usermanagement.UserManagementPermission;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.PrivilegeImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.RoleImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.SessionImpl;
import de.uniol.inf.is.odysseus.usermanagement.domain.impl.UserImpl;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.PrivilegeDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.RoleDAO;
import de.uniol.inf.is.odysseus.usermanagement.persistence.impl.UserDAO;
import de.uniol.inf.is.odysseus.usermanagement.policy.ChangePasswordPolicy;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class UserManagementServiceImpl implements IUserManagement {
	private final EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory("odysseusPU");
	private final static String OBJECT_URI = "Usermanagement";
	private final UserDAO userDAO = new UserDAO();
	private final RoleDAO roleDAO = new RoleDAO();
	private final PrivilegeDAO privilegeDAO = new PrivilegeDAO();
	private final SessionStore sessionStore = SessionStore.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #createRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public IRole createRole(final String name, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.CREATE_ROLE,
				UserManagementServiceImpl.OBJECT_URI)) {
			final RoleImpl role = new RoleImpl();
			role.setName(name);
			this.roleDAO.create(role);
			return role;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #deleteRole(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void deleteRole(final IRole role, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.DELETE_ROLE,
				UserManagementServiceImpl.OBJECT_URI)) {
			final RoleImpl tmpRole = this.roleDAO.find(role.getId());
			if (tmpRole != null) {
				this.roleDAO.delete(tmpRole);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #findRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public IRole findRole(final String name, final ISession caller) {
		final RoleImpl role = this.roleDAO.findByName(name);
		return role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #getRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public IRole getRole(final String roleId, final ISession caller) {
		final RoleImpl role = this.roleDAO.find(roleId);
		return role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #getRoles(de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public List<? extends IRole> getRoles(final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GET_ALL_USER,
				UserManagementServiceImpl.OBJECT_URI)
				|| this.hasPermission(caller, UserManagementPermission.GET_ALL,
						UserManagementServiceImpl.OBJECT_URI)) {
			final List<RoleImpl> roles = this.roleDAO.findAll();
			return roles;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #createUser(java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public IUser createUser(final String name, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.CREATE_USER,
				UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl user = new UserImpl();
			user.setName(name);
			this.userDAO.create(user);
			return user;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #changePassword(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * byte[], de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void changePassword(final IUser user, final byte[] password,
			final ISession caller) {
		final ISession session = this.sessionStore.get(caller.getId());
		if (ChangePasswordPolicy.allow(user, session.getUser())
				|| this.hasPermission(session,
						UserManagementPermission.ALTER_USER,
						UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl tmpUser = this.userDAO.find(user.getId());
			tmpUser.setPassword(password);
			this.userDAO.update(tmpUser);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #activateUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void activateUser(final IUser user, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.ALTER_USER,
				UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl tmpUser = this.userDAO.find(user.getId());
			tmpUser.setActive(true);
			this.userDAO.update(tmpUser);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #deactivateUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void deactivateUser(final IUser user, final ISession caller) {
		if (this.hasPermission(caller,
				UserManagementPermission.DEACTIVATE_USER,
				UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl tmpUser = this.userDAO.find(user.getId());
			tmpUser.setActive(false);
			this.userDAO.update(tmpUser);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #deleteUser(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void deleteUser(final IUser user, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.DELETE_USER,
				UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl tmpUser = this.userDAO.find(user.getId());
			this.userDAO.delete(tmpUser);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #findUser(java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public IUser findUser(final String name, final ISession caller) {
		final UserImpl user = this.userDAO.findByName(name);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #getUser(java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public IUser getUser(final String userId, final ISession caller) {
		final IUser user = this.userDAO.find(userId);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #getUsers(de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public List<? extends IUser> getUsers(final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GET_ALL_USER,
				UserManagementServiceImpl.OBJECT_URI)
				|| this.hasPermission(caller, UserManagementPermission.GET_ALL,
						UserManagementServiceImpl.OBJECT_URI)) {
			final List<UserImpl> users = this.userDAO.findAll();
			return users;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #grantRole(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void grantRole(final IUser user, final IRole role,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT_ROLE,
				UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl tmpUser = this.userDAO.find(user.getId());
			final RoleImpl tmpRole = this.roleDAO.find(role.getId());
			tmpUser.addRole(tmpRole);
			this.userDAO.update(tmpUser);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #revokeRole(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void revokeRole(final IUser user, final IRole role,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE_ROLE,
				UserManagementServiceImpl.OBJECT_URI)) {
			final UserImpl tmpUser = this.userDAO.find(user.getId());
			final RoleImpl tmpRole = this.roleDAO.find(role.getId());
			tmpUser.removeRole(tmpRole);
			this.userDAO.update(tmpUser);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #grantPermission(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
	 * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void grantPermission(final IUser user, final IPermission permission,
			final String objectURI, final ISession caller) {
		final Set<IPermission> permissions = new HashSet<IPermission>();
		permissions.add(permission);
		this.grantPermissions(user, permissions, objectURI, caller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #grantPermissions(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void grantPermissions(final IUser user,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT,
				UserManagementServiceImpl.OBJECT_URI)) {
			boolean create = true;
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PrivilegeImpl tmpPrivilege = this.privilegeDAO
							.find(privilege.getId());
					if (tmpPrivilege != null) {
						create = false;
						for (final IPermission permission : permissions) {
							tmpPrivilege.addPermission(permission);
						}
						this.privilegeDAO.update(tmpPrivilege);
					}
				}
			}
			if (create) {
				final PrivilegeImpl tmpPrivilege = new PrivilegeImpl();
				tmpPrivilege.setObjectURI(objectURI);
				for (final IPermission permission : permissions) {
					tmpPrivilege.addPermission(permission);
				}
				this.privilegeDAO.create(tmpPrivilege);
				final UserImpl tmpUser = this.userDAO.find(user.getId());
				tmpUser.addPrivilege(tmpPrivilege);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #grantPermission(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
	 * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void grantPermission(final IRole role, final IPermission permission,
			final String objectURI, final ISession caller) {
		final Set<IPermission> permissions = new HashSet<IPermission>();
		permissions.add(permission);
		this.grantPermissions(role, permissions, objectURI, caller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #grantPermissions(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void grantPermissions(final IRole role,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT,
				UserManagementServiceImpl.OBJECT_URI)) {
			boolean create = true;
			for (final IPrivilege privilege : role.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PrivilegeImpl tmpPrivilege = this.privilegeDAO
							.find(privilege.getId());
					if (tmpPrivilege != null) {
						create = false;
						for (final IPermission permission : permissions) {
							tmpPrivilege.addPermission(permission);
						}
						this.privilegeDAO.update(tmpPrivilege);
					}
				}
			}
			if (create) {
				final PrivilegeImpl tmpPrivilege = new PrivilegeImpl();
				tmpPrivilege.setObjectURI(objectURI);
				for (final IPermission permission : permissions) {
					tmpPrivilege.addPermission(permission);
				}
				this.privilegeDAO.create(tmpPrivilege);
				final RoleImpl tmpRole = this.roleDAO.find(role.getId());
				tmpRole.addPrivilege(tmpPrivilege);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #revokePermission(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
	 * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void revokePermission(final IUser user,
			final IPermission permission, final String objectURI,
			final ISession caller) {
		final Set<IPermission> permissions = new HashSet<IPermission>();
		permissions.add(permission);
		this.revokePermissions(user, permissions, objectURI, caller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #revokePermissions(de.uniol.inf.is.odysseus.usermanagement.domain.User,
	 * java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void revokePermissions(final IUser user,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE,
				UserManagementServiceImpl.OBJECT_URI)) {
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PrivilegeImpl tmpPrivilege = this.privilegeDAO
							.find(privilege.getId());
					if (tmpPrivilege != null) {
						for (final IPermission permission : permissions) {
							tmpPrivilege.removePermission(permission);
						}
						this.privilegeDAO.update(tmpPrivilege);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #revokePermission(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Permission,
	 * java.lang.String, de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void revokePermission(final IRole role,
			final IPermission permission, final String objectURI,
			final ISession caller) {
		final Set<IPermission> permissions = new HashSet<IPermission>();
		permissions.add(permission);
		this.revokePermissions(role, permissions, objectURI, caller);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.usermanagement.service.UsermanagementService
	 * #revokePermissions(de.uniol.inf.is.odysseus.usermanagement.domain.Role,
	 * java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.usermanagement.domain.Session)
	 */
	@Override
	public void revokePermissions(final IRole role,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE,
				UserManagementServiceImpl.OBJECT_URI)) {
			for (final IPrivilege privilege : role.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PrivilegeImpl tmpPrivilege = this.privilegeDAO
							.find(privilege.getId());
					if (tmpPrivilege != null) {
						for (final IPermission permission : permissions) {
							tmpPrivilege.removePermission(permission);
						}
						this.privilegeDAO.update(tmpPrivilege);
					}
				}
			}
		}
	}

	@Override
	public boolean hasPermission(final ISession session,
			final IPermission permission, final String objectURI) {
		final Map<String, Set<IPermission>> permissions = new HashMap<String, Set<IPermission>>();
		final SessionImpl tmpSession = this.sessionStore.get(session.getId());
		if (tmpSession.isValid()) {
			final IUser user = tmpSession.getUser();
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (!permissions.containsKey(privilege.getObjectURI())) {
					permissions.put(privilege.getObjectURI(),
							new HashSet<IPermission>());
				}
				permissions.get(privilege.getObjectURI()).addAll(
						privilege.getPermissions());
			}
			for (final IRole role : user.getRoles()) {
				for (final IPrivilege privilege : role.getPrivileges()) {
					if (!permissions.containsKey(privilege.getObjectURI())) {
						permissions.put(privilege.getObjectURI(),
								new HashSet<IPermission>());
					}
					permissions.get(privilege.getObjectURI()).addAll(
							privilege.getPermissions());
				}
			}
			tmpSession.updateSession();
			return permissions.containsKey(objectURI)
					&& permissions.get(objectURI).contains(permission);
		}
		return false;
	}

	protected void activate(ComponentContext context) {
		final EntityManager em = this.entityManagerFactory
				.createEntityManager();
		this.userDAO.setEntityManager(em);
		this.roleDAO.setEntityManager(em);
		this.privilegeDAO.setEntityManager(em);
		if (userDAO.findByName("System") == null) {
			try {
				UserImpl user = new UserImpl();
				user.setName("System");
				user.setPassword("manager".getBytes());
				user = userDAO.create(user);

				RoleImpl adminRole = new RoleImpl();
				adminRole.setName("sys_admin");
				adminRole = roleDAO.create(adminRole);

				PrivilegeImpl adminPrivilege = new PrivilegeImpl();
				adminPrivilege
						.setObjectURI(UserManagementServiceImpl.OBJECT_URI);
				for (IPermission permission : UserManagementPermission.class
						.getEnumConstants()) {
					adminPrivilege.addPermission(permission);
				}
				adminPrivilege = privilegeDAO.create(adminPrivilege);
				adminRole.addPrivilege(adminPrivilege);
				roleDAO.update(adminRole);

				RoleImpl dictionaryRole = new RoleImpl();
				dictionaryRole.setName("datadictionary");
				dictionaryRole = roleDAO.create(dictionaryRole);

				RoleImpl configurationRole = new RoleImpl();
				configurationRole.setName("configuration");
				configurationRole = roleDAO.create(configurationRole);

				RoleImpl queryexecutor = new RoleImpl();
				queryexecutor.setName("queryexecutor");
				queryexecutor = roleDAO.create(queryexecutor);

				user.addRole(adminRole);
				user.addRole(dictionaryRole);
				user.addRole(configurationRole);
				user.addRole(queryexecutor);
				userDAO.update(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void deactivate(ComponentContext context) {

	}
}
