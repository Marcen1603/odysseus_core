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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.ConfigurationPermission;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryPermission;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.usermanagement.policy.ChangePasswordPolicy;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

abstract public class AbstractUserManagement<USER extends IUser, ROLE extends IRole, PRIVILEGE extends IPrivilege>
		implements IUserManagement {

	Logger logger = LoggerFactory.getLogger(AbstractUserManagement.class);

	private final List<IUserManagementListener> listener = new CopyOnWriteArrayList<IUserManagementListener>();

	private final SessionStore sessionStore = SessionStore.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #changePassword(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.User, byte[],
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void changePassword(final IUser user, final byte[] password,
			final ISession caller) {
		final ISession session = this.sessionStore.get(caller.getId());
		if (ChangePasswordPolicy.allow(user, session.getUser())
				|| this.hasPermission(session,
						UserManagementPermission.ALTER_USER,
						UserManagementPermission.objectUri)) {
			final USER tmpUser = this.getUserDAO().find(user.getId());
			tmpUser.setPassword(password);
			this.getUserDAO().update(tmpUser);
		}
	}

	protected abstract IGenericDAO<USER, String> getUserDAO();

	protected abstract IGenericDAO<ROLE, String> getRoleDAO();

	protected abstract IGenericDAO<PRIVILEGE, String> getPrivilegeDAO();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #createRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IRole createRole(final String name, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.CREATE_ROLE,
				UserManagementPermission.objectUri)) {
			final ROLE role = createEmptyRole();
			role.setName(name);
			this.getRoleDAO().create(role);
			return role;
		}
		return null;
	}

	protected abstract ROLE createEmptyRole();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #createUser(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IUser createUser(final String name, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.CREATE_USER,
				UserManagementPermission.objectUri)) {
			if (getUserDAO().findByName(name) == null) {
				final USER user = createEmptyUser();
				user.setName(name);
				// Every User has the public role
				user.addRole(findRole("Public", caller));
				this.getUserDAO().create(user);
				fireUserChangedEvent();
				return user;
			}
			throw new UsernameAlreadyUsedException(name);
		}

		throw new PermissionException("Not right to create user");
	}

	abstract protected USER createEmptyUser();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #findRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IRole findRole(final String name, final ISession caller) {
		final ROLE role = this.getRoleDAO().findByName(name);
		return role;
	}

	@Override
	public boolean hasRole(String name, String rolename) {
		final USER user = this.getUserDAO().findByName(name);
		final ROLE role = this.getRoleDAO().findByName(rolename);
		return user.hasRole(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #getRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IRole getRole(final String roleId, final ISession caller) {
		final ROLE role = this.getRoleDAO().find(roleId);
		return role;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #getRoles(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.Session)
	 */
	@Override
	public List<? extends IRole> getRoles(final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GET_ALL_USER,
				UserManagementPermission.objectUri)
				|| this.hasPermission(caller, UserManagementPermission.GET_ALL,
						UserManagementPermission.objectUri)) {
			final List<ROLE> roles = this.getRoleDAO().findAll();
			return roles;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #activateUser(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void activateUser(final IUser user, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.ALTER_USER,
				UserManagementPermission.objectUri)) {
			final USER tmpUser = this.getUserDAO().find(user.getId());
			tmpUser.setActive(true);
			this.getUserDAO().update(tmpUser);
			fireUserChangedEvent();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #deactivateUser(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void deactivateUser(final IUser user, final ISession caller) {
		if (this.hasPermission(caller,
				UserManagementPermission.DEACTIVATE_USER,
				UserManagementPermission.objectUri)) {
			final USER tmpUser = this.getUserDAO().find(user.getId());
			tmpUser.setActive(false);
			this.getUserDAO().update(tmpUser);
			fireUserChangedEvent();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #deleteUser(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void deleteUser(final IUser user, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.DELETE_USER,
				UserManagementPermission.objectUri)) {
			final USER tmpUser = this.getUserDAO().find(user.getId());
			this.getUserDAO().delete(tmpUser);
			fireUserChangedEvent();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #findUser(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IUser findUser(final String name, final ISession caller) {
		final USER user = this.getUserDAO().findByName(name);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #getUser(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IUser getUser(final String userId, final ISession caller) {
		final IUser user = this.getUserDAO().find(userId);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #getUsers(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.Session)
	 */
	@Override
	public List<? extends IUser> getUsers(final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GET_ALL_USER,
				UserManagementPermission.objectUri)
				|| this.hasPermission(caller, UserManagementPermission.GET_ALL,
						UserManagementPermission.objectUri)) {
			final List<USER> users = this.getUserDAO().findAll();
			return users;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #grantRole(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void grantRole(final IUser user, final IRole role,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT_ROLE,
				UserManagementPermission.objectUri)) {
			final USER tmpUser = this.getUserDAO().find(user.getId());
			final ROLE tmpRole = this.getRoleDAO().find(role.getId());
			tmpUser.addRole(tmpRole);
			this.getUserDAO().update(tmpUser);
			fireUserChangedEvent();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #revokeRole(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void revokeRole(final IUser user, final IRole role,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE_ROLE,
				UserManagementPermission.objectUri)) {
			final USER tmpUser = this.getUserDAO().find(user.getId());
			final ROLE tmpRole = this.getRoleDAO().find(role.getId());
			tmpUser.removeRole(tmpRole);
			this.getUserDAO().update(tmpUser);
			fireUserChangedEvent();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #grantPermission(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Permission,
	 * java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
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
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #grantPermissions(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.User, java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void grantPermissions(final IUser user,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT,
				UserManagementPermission.objectUri)) {
			boolean create = true;
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO().find(
							privilege.getId());
					if (tmpPrivilege != null) {
						create = false;
						for (final IPermission permission : permissions) {
							tmpPrivilege.addPermission(permission);
						}
						this.getPrivilegeDAO().update(tmpPrivilege);
					}
				}
			}
			if (create) {
				final PRIVILEGE tmpPrivilege = createEmptyPrivilege();
				tmpPrivilege.setObjectURI(objectURI);
				for (final IPermission permission : permissions) {
					tmpPrivilege.addPermission(permission);
				}
				this.getPrivilegeDAO().create(tmpPrivilege);
				final USER tmpUser = this.getUserDAO().find(user.getId());
				tmpUser.addPrivilege(tmpPrivilege);

			}
		}
		fireUserChangedEvent();
	}

	abstract protected PRIVILEGE createEmptyPrivilege();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #grantPermission(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Permission,
	 * java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
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
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #grantPermissions(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.Role, java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void grantPermissions(final IRole role,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT,
				UserManagementPermission.objectUri)) {
			boolean create = true;
			for (final IPrivilege privilege : role.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO().find(
							privilege.getId());
					if (tmpPrivilege != null) {
						create = false;
						for (final IPermission permission : permissions) {
							tmpPrivilege.addPermission(permission);
						}
						this.getPrivilegeDAO().update(tmpPrivilege);
					}
				}
			}
			if (create) {
				final PRIVILEGE tmpPrivilege = createEmptyPrivilege();
				tmpPrivilege.setObjectURI(objectURI);
				for (final IPermission permission : permissions) {
					tmpPrivilege.addPermission(permission);
				}
				this.getPrivilegeDAO().create(tmpPrivilege);
				final ROLE tmpRole = this.getRoleDAO().find(role.getId());
				tmpRole.addPrivilege(tmpPrivilege);

			}
		}
		fireUserChangedEvent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #revokePermission(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.User,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Permission,
	 * java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
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
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #revokePermissions(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.User, java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void revokePermissions(final IUser user,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE,
				UserManagementPermission.objectUri)) {
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO().find(
							privilege.getId());
					if (tmpPrivilege != null) {
						for (final IPermission permission : permissions) {
							tmpPrivilege.removePermission(permission);
						}
						this.getPrivilegeDAO().update(tmpPrivilege);
					}
				}
			}
		}
		fireUserChangedEvent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #revokePermission(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.Role,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Permission,
	 * java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
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
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #revokePermissions(de.uniol.inf.is.odysseus.core.server
	 * .usermanagement.domain.Role, java.util.Set, java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void revokePermissions(final IRole role,
			final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE,
				UserManagementPermission.objectUri)) {
			for (final IPrivilege privilege : role.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO().find(
							privilege.getId());
					if (tmpPrivilege != null) {
						for (final IPermission permission : permissions) {
							tmpPrivilege.removePermission(permission);
						}
						this.getPrivilegeDAO().update(tmpPrivilege);
					}
				}
			}
		}
		fireUserChangedEvent();
	}

	@Override
	public boolean hasPermission(final ISession session,
			final IPermission permission, final String objectURI) {
		if (objectURI == null) {
			throw new IllegalArgumentException("Object URI cannot be null !");
		}
		final Map<String, Set<IPermission>> permissions = new HashMap<String, Set<IPermission>>();
		final ISession tmpSession = this.sessionStore.get(session.getId());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService
	 * #deleteRole(de.uniol.inf.is.odysseus.core.server.usermanagement
	 * .domain.Role,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public void deleteRole(final IRole role, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.DELETE_ROLE,
				UserManagementPermission.objectUri)) {
			final ROLE tmpRole = this.getRoleDAO().find(role.getId());
			if (tmpRole != null) {
				this.getRoleDAO().delete(tmpRole);
			}
		}
	}

	@Override
	public void addUserManagementListener(IUserManagementListener listener) {
		this.listener.add(listener);
	}

	protected void fireUserChangedEvent() {
		for (IUserManagementListener l : listener) {
			l.usersChangedEvent();
		}
	}

	public void initDefaultUsers() {
		if (getUserDAO().findByName("System") == null) {
			try {
				logger.debug("Creating new user database");
				createSuperUser();
				createDSUserRole();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fireUserChangedEvent();
	}

	protected void createDSUserRole() {
		ROLE dsUserRole = createEmptyRole();
		dsUserRole.setName("DSUser");
		dsUserRole = getRoleDAO().create(dsUserRole);
		
		PRIVILEGE userMgmtPrivilege = createEmptyPrivilege();
		userMgmtPrivilege.setObjectURI(UserManagementPermission.objectUri);
		userMgmtPrivilege.addPermission(UserManagementPermission.LOGOUT);
		userMgmtPrivilege = getPrivilegeDAO().create(userMgmtPrivilege);
		dsUserRole.addPrivilege(userMgmtPrivilege);
		getRoleDAO().update(dsUserRole);
		
		PRIVILEGE dictPrivilege = createEmptyPrivilege();
		dictPrivilege.setObjectURI(DataDictionaryPermission.objectURI);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_DATATYPE);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_ENTITY);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_SOURCETYPE);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_STREAM);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_VIEW);
		dictPrivilege = getPrivilegeDAO().create(dictPrivilege);
		dsUserRole.addPrivilege(dictPrivilege);
		getRoleDAO().update(dsUserRole);
		
		PRIVILEGE execPrivilege = createEmptyPrivilege();
		execPrivilege.setObjectURI(ExecutorPermission.objectURI);
		execPrivilege.addPermission(ExecutorPermission.ADD_QUERY);
		execPrivilege.addPermission(ExecutorPermission.START_QUERY);
		execPrivilege.addPermission(ExecutorPermission.STOP_QUERY);
		execPrivilege.addPermission(ExecutorPermission.REMOVE_QUERY);
		
		execPrivilege = getPrivilegeDAO().create(execPrivilege);
		dsUserRole.addPrivilege(execPrivilege);
		getRoleDAO().update(dsUserRole);
	}

	protected void createSuperUser() {
		USER user = createEmptyUser();
		user.setName("System");
		user.setPassword("manager".getBytes());
		user = getUserDAO().create(user);

		// --- ADMIN ROLE ----
		ROLE adminRole = createEmptyRole();
		adminRole.setName("sys_admin");
		adminRole = getRoleDAO().create(adminRole);

		PRIVILEGE adminPrivilege = createEmptyPrivilege();
		adminPrivilege.setObjectURI(UserManagementPermission.objectUri);
		for (IPermission permission : UserManagementPermission.class
				.getEnumConstants()) {
			adminPrivilege.addPermission(permission);
		}
		adminPrivilege = getPrivilegeDAO().create(adminPrivilege);
		adminRole.addPrivilege(adminPrivilege);
		getRoleDAO().update(adminRole);

		// --- Data dictionary Role ----
		ROLE dictionaryRole = createEmptyRole();
		dictionaryRole.setName("datadictionary");
		dictionaryRole = getRoleDAO().create(dictionaryRole);

		PRIVILEGE dictPrivilege = createEmptyPrivilege();
		dictPrivilege.setObjectURI(DataDictionaryPermission.objectURI);
		for (IPermission permission : DataDictionaryPermission.class
				.getEnumConstants()) {
			dictPrivilege.addPermission(permission);
		}
		dictPrivilege = getPrivilegeDAO().create(dictPrivilege);
		dictionaryRole.addPrivilege(dictPrivilege);
		getRoleDAO().update(dictionaryRole);

		// --- Configuration Role ----
		ROLE configurationRole = createEmptyRole();
		configurationRole.setName("configuration");
		configurationRole = getRoleDAO().create(configurationRole);

		PRIVILEGE confPrivilege = createEmptyPrivilege();
		confPrivilege.setObjectURI(ConfigurationPermission.objectURI);
		for (IPermission permission : ConfigurationPermission.class
				.getEnumConstants()) {
			confPrivilege.addPermission(permission);
		}
		confPrivilege = getPrivilegeDAO().create(confPrivilege);
		configurationRole.addPrivilege(confPrivilege);
		getRoleDAO().update(configurationRole);

		// --- Query Execution Role ----
		ROLE queryexecutor = createEmptyRole();
		queryexecutor.setName("queryexecutor");
		queryexecutor = getRoleDAO().create(queryexecutor);

		PRIVILEGE execPrivilege = createEmptyPrivilege();
		execPrivilege.setObjectURI(ExecutorPermission.objectURI);
		for (IPermission permission : ExecutorPermission.class
				.getEnumConstants()) {
			execPrivilege.addPermission(permission);
		}
		execPrivilege = getPrivilegeDAO().create(execPrivilege);
		queryexecutor.addPrivilege(execPrivilege);
		getRoleDAO().update(queryexecutor);

		user.addRole(adminRole);
		user.addRole(dictionaryRole);
		user.addRole(configurationRole);
		user.addRole(queryexecutor);
		user.setActive(true);

		ROLE pub = createEmptyRole();
		pub.setName("Public");
		getRoleDAO().create(pub);
		user.addRole(pub);

		getUserDAO().update(user);
	}

}
