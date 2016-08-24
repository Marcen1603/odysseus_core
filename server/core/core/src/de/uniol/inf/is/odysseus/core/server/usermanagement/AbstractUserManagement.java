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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryPermission;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.ExecutorPermission;
import de.uniol.inf.is.odysseus.core.server.usermanagement.policy.ChangePasswordPolicy;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermissionProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.IPrivilege;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

abstract public class AbstractUserManagement<USER extends IUser, ROLE extends IRole, PRIVILEGE extends IPrivilege>
		implements IUserManagementWritable {

	Logger logger = LoggerFactory.getLogger(AbstractUserManagement.class);

	private final List<IUserManagementListener> listener = new CopyOnWriteArrayList<IUserManagementListener>();

	private final SessionStore sessionStore = SessionStore.getInstance();

	static private Map<String, IPermission[]> permissions = new HashMap<>();
	
	Map<ITenant, Boolean> initialized = new HashMap<>();
	
	private Map<ITenant, USER> superusers = new HashMap<>();

	public void bindPermissionProvider(IPermissionProvider provider){
		synchronized(permissions){
			permissions.put(provider.getName(), provider.getPermissions());
			for (Entry<ITenant,USER> superuser: superusers.entrySet()){
				assignRoleWithFullRightsToUser(superuser.getKey(), superuser.getValue(), provider.getName(), provider.getPermissions());
				getUserDAO(superuser.getKey()).update(superuser.getValue());
			}
		}
	}
	
	public void unbindPermissionProvider(IPermissionProvider provider){
		synchronized(permissions){
			// TODO: Remove rights from users again
		}
	}
	
	
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
	public void changePassword(final IUser user, final byte[] password, final ISession caller) {
		final ISession session = this.sessionStore.get(caller.getId());
		if (ChangePasswordPolicy.allow(user, session.getUser()) || this.hasPermission(session,
				UserManagementPermission.ALTER_USER, UserManagementPermission.objectURI)) {
			final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
			tmpUser.setPassword(password);
			this.getUserDAO(caller.getTenant()).update(tmpUser);
		}
	}

	protected abstract IGenericDAO<USER, String> getUserDAO(ITenant tenant);

	protected abstract IGenericDAO<ROLE, String> getRoleDAO(ITenant tenant);

	protected abstract IGenericDAO<PRIVILEGE, String> getPrivilegeDAO(ITenant tenant);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.usermanagement.service.
	 * UsermanagementService #createRole(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.server.usermanagement.domain.Session)
	 */
	@Override
	public IRole createRole(final String name, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.CREATE_ROLE, UserManagementPermission.objectURI)) {
			final ROLE role = createEmptyRole();
			role.setName(name);
			this.getRoleDAO(caller.getTenant()).create(role);
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
		if (this.hasPermission(caller, UserManagementPermission.CREATE_USER, UserManagementPermission.objectURI)) {
			if (name.contains(".")) {
				throw new UsernameNotAllowedException("Username is not allowed to contain '.'");
			}
			if (getUserDAO(caller.getTenant()).findByName(name) == null) {
				final USER user = createEmptyUser();
				user.setName(name);
				// Every User has the public role
				user.addRole(findRole("Public", caller));
				this.getUserDAO(caller.getTenant()).create(user);
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
		final ROLE role = this.getRoleDAO(caller.getTenant()).findByName(name);
		return role;
	}

	@Override
	public boolean hasRole(String name, String rolename, final ISession caller) {
		final USER user = this.getUserDAO(caller.getTenant()).findByName(name);
		final ROLE role = this.getRoleDAO(caller.getTenant()).findByName(rolename);
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
		final ROLE role = this.getRoleDAO(caller.getTenant()).find(roleId);
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
		if (this.hasPermission(caller, UserManagementPermission.GET_ALL_USER, UserManagementPermission.objectURI)
				|| this.hasPermission(caller, UserManagementPermission.GET_ALL, UserManagementPermission.objectURI)) {
			final List<ROLE> roles = this.getRoleDAO(caller.getTenant()).findAll();
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
		if (this.hasPermission(caller, UserManagementPermission.ALTER_USER, UserManagementPermission.objectURI)) {
			final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
			tmpUser.setActive(true);
			this.getUserDAO(caller.getTenant()).update(tmpUser);
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
		if (this.hasPermission(caller, UserManagementPermission.DEACTIVATE_USER, UserManagementPermission.objectURI)) {
			final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
			tmpUser.setActive(false);
			this.getUserDAO(caller.getTenant()).update(tmpUser);
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
		if (this.hasPermission(caller, UserManagementPermission.DELETE_USER, UserManagementPermission.objectURI)) {
			final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
			this.getUserDAO(caller.getTenant()).delete(tmpUser);
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
		final USER user = this.getUserDAO(caller.getTenant()).findByName(name);
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
		final IUser user = this.getUserDAO(caller.getTenant()).find(userId);
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
		if (this.hasPermission(caller, UserManagementPermission.GET_ALL_USER, UserManagementPermission.objectURI)
				|| this.hasPermission(caller, UserManagementPermission.GET_ALL, UserManagementPermission.objectURI)) {
			final List<USER> users = this.getUserDAO(caller.getTenant()).findAll();
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
	public void grantRole(final IUser user, final IRole role, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT_ROLE, UserManagementPermission.objectURI)) {
			final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
			final ROLE tmpRole = this.getRoleDAO(caller.getTenant()).find(role.getId());
			tmpUser.addRole(tmpRole);
			this.getUserDAO(caller.getTenant()).update(tmpUser);
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
	public void revokeRole(final IUser user, final IRole role, final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE_ROLE, UserManagementPermission.objectURI)) {
			final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
			final ROLE tmpRole = this.getRoleDAO(caller.getTenant()).find(role.getId());
			tmpUser.removeRole(tmpRole);
			this.getUserDAO(caller.getTenant()).update(tmpUser);
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
	public void grantPermission(final IUser user, final IPermission permission, final String objectURI,
			final ISession caller) {
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
	public void grantPermissions(final IUser user, final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT, UserManagementPermission.objectURI)) {
			boolean create = true;
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO(caller.getTenant()).find(privilege.getId());
					if (tmpPrivilege != null) {
						create = false;
						for (final IPermission permission : permissions) {
							tmpPrivilege.addPermission(permission);
						}
						this.getPrivilegeDAO(caller.getTenant()).update(tmpPrivilege);
					}
				}
			}
			if (create) {
				final PRIVILEGE tmpPrivilege = createEmptyPrivilege();
				tmpPrivilege.setObjectURI(objectURI);
				for (final IPermission permission : permissions) {
					tmpPrivilege.addPermission(permission);
				}
				this.getPrivilegeDAO(caller.getTenant()).create(tmpPrivilege);
				final USER tmpUser = this.getUserDAO(caller.getTenant()).find(user.getId());
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
	public void grantPermission(final IRole role, final IPermission permission, final String objectURI,
			final ISession caller) {
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
	public void grantPermissions(final IRole role, final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.GRANT, UserManagementPermission.objectURI)) {
			boolean create = true;
			for (final IPrivilege privilege : role.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO(caller.getTenant()).find(privilege.getId());
					if (tmpPrivilege != null) {
						create = false;
						for (final IPermission permission : permissions) {
							tmpPrivilege.addPermission(permission);
						}
						this.getPrivilegeDAO(caller.getTenant()).update(tmpPrivilege);
					}
				}
			}
			if (create) {
				final PRIVILEGE tmpPrivilege = createEmptyPrivilege();
				tmpPrivilege.setObjectURI(objectURI);
				for (final IPermission permission : permissions) {
					tmpPrivilege.addPermission(permission);
				}
				this.getPrivilegeDAO(caller.getTenant()).create(tmpPrivilege);
				final ROLE tmpRole = this.getRoleDAO(caller.getTenant()).find(role.getId());
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
	public void revokePermission(final IUser user, final IPermission permission, final String objectURI,
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
	public void revokePermissions(final IUser user, final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE, UserManagementPermission.objectURI)) {
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO(caller.getTenant()).find(privilege.getId());
					if (tmpPrivilege != null) {
						for (final IPermission permission : permissions) {
							tmpPrivilege.removePermission(permission);
						}
						this.getPrivilegeDAO(caller.getTenant()).update(tmpPrivilege);
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
	public void revokePermission(final IRole role, final IPermission permission, final String objectURI,
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
	public void revokePermissions(final IRole role, final Set<IPermission> permissions, final String objectURI,
			final ISession caller) {
		if (this.hasPermission(caller, UserManagementPermission.REVOKE, UserManagementPermission.objectURI)) {
			for (final IPrivilege privilege : role.getPrivileges()) {
				if (privilege.getObjectURI().equals(objectURI)) {
					final PRIVILEGE tmpPrivilege = this.getPrivilegeDAO(caller.getTenant()).find(privilege.getId());
					if (tmpPrivilege != null) {
						for (final IPermission permission : permissions) {
							tmpPrivilege.removePermission(permission);
						}
						this.getPrivilegeDAO(caller.getTenant()).update(tmpPrivilege);
					}
				}
			}
		}
		fireUserChangedEvent();
	}

	@Override
	public boolean hasPermission(final ISession session, final IPermission permission, final String objectURI) {
		if (objectURI == null) {
			throw new IllegalArgumentException("Object URI cannot be null !");
		}
		final Map<String, Set<IPermission>> permissions = new HashMap<String, Set<IPermission>>();
		final ISession tmpSession = this.sessionStore.get(session.getId());
		if (tmpSession.isValid()) {
			final IUser user = tmpSession.getUser();
			for (final IPrivilege privilege : user.getPrivileges()) {
				if (!permissions.containsKey(privilege.getObjectURI())) {
					permissions.put(privilege.getObjectURI(), new HashSet<IPermission>());
				}
				permissions.get(privilege.getObjectURI()).addAll(privilege.getPermissions());
			}
			for (final IRole role : user.getRoles()) {
				for (final IPrivilege privilege : role.getPrivileges()) {
					if (!permissions.containsKey(privilege.getObjectURI())) {
						permissions.put(privilege.getObjectURI(), new HashSet<IPermission>());
					}
					permissions.get(privilege.getObjectURI()).addAll(privilege.getPermissions());
				}
			}
			tmpSession.updateSession();
			return permissions.containsKey(objectURI) && permissions.get(objectURI).contains(permission);
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
		if (this.hasPermission(caller, UserManagementPermission.DELETE_ROLE, UserManagementPermission.objectURI)) {
			final ROLE tmpRole = this.getRoleDAO(caller.getTenant()).find(role.getId());
			if (tmpRole != null) {
				this.getRoleDAO(caller.getTenant()).delete(tmpRole);
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

	public void initDefaultUsers(ITenant t) {
		if (getUserDAO(t).findByName("System") == null) {
			try {
				logger.trace("Creating new user database for Tenant " + t.getName());
				createSuperUser(t);
				createDSUserRole(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fireUserChangedEvent();
	}

	protected void createDSUserRole(ITenant t) {
		ROLE dsUserRole = createEmptyRole();
		dsUserRole.setName("DSUser");
		dsUserRole = getRoleDAO(t).create(dsUserRole);

		PRIVILEGE userMgmtPrivilege = createEmptyPrivilege();
		userMgmtPrivilege.setObjectURI(UserManagementPermission.objectURI);
		userMgmtPrivilege.addPermission(UserManagementPermission.LOGOUT);
		userMgmtPrivilege = getPrivilegeDAO(t).create(userMgmtPrivilege);
		dsUserRole.addPrivilege(userMgmtPrivilege);
		getRoleDAO(t).update(dsUserRole);

		PRIVILEGE dictPrivilege = createEmptyPrivilege();
		dictPrivilege.setObjectURI(DataDictionaryPermission.objectURI);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_DATATYPE);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_ENTITY);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_SOURCETYPE);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_STREAM);
		dictPrivilege.addPermission(DataDictionaryPermission.ADD_VIEW);
		dictPrivilege = getPrivilegeDAO(t).create(dictPrivilege);
		dsUserRole.addPrivilege(dictPrivilege);
		getRoleDAO(t).update(dsUserRole);

		PRIVILEGE execPrivilege = createEmptyPrivilege();
		execPrivilege.setObjectURI(ExecutorPermission.objectURI);
		execPrivilege.addPermission(ExecutorPermission.ADD_QUERY);
		execPrivilege.addPermission(ExecutorPermission.START_QUERY);
		execPrivilege.addPermission(ExecutorPermission.STOP_QUERY);
		execPrivilege.addPermission(ExecutorPermission.REMOVE_QUERY);

		execPrivilege = getPrivilegeDAO(t).create(execPrivilege);
		dsUserRole.addPrivilege(execPrivilege);
		getRoleDAO(t).update(dsUserRole);
	}

	protected void createSuperUser(ITenant t) {		
		USER superuser = createEmptyUser();
		superusers.put(t,superuser);
		superuser.setName("System");
		superuser.setPassword("manager".getBytes());
		superuser = getUserDAO(t).create(superuser);
		synchronized (permissions) {			
			for (String uri:permissions.keySet()){
				assignRoleWithFullRightsToUser(t, superuser, uri, permissions.get(uri));
			}
		}
		
//		// --- ADMIN ROLE ----
//		assignRoleWithFullRightsToUser(t, user, UserManagementPermission.objectURI,
//				UserManagementPermission.class.getEnumConstants());
//			
//		// --- Data dictionary Role ----
//		assignRoleWithFullRightsToUser(t, user, DataDictionaryPermission.objectURI,
//				DataDictionaryPermission.class.getEnumConstants());
//	
//		// --- Configuration Role ----
//		assignRoleWithFullRightsToUser(t, user, ConfigurationPermission.objectURI,
//				ConfigurationPermission.class.getEnumConstants());
//	
//		// --- Updater Role ----
//		assignRoleWithFullRightsToUser(t, user, UpdatePermission.objectURI,
//				UpdatePermission.class.getEnumConstants());
//
//		// --- Query Execution Role ----
//		assignRoleWithFullRightsToUser(t, user, ExecutorPermission.objectURI,
//				ExecutorPermission.class.getEnumConstants());

		superuser.setActive(true);

		ROLE pub = createEmptyRole();
		pub.setName("Public");
		getRoleDAO(t).create(pub);
		superuser.addRole(pub);

		getUserDAO(t).update(superuser);
	}

	private void assignRoleWithFullRightsToUser(ITenant t, USER user, String objecturi,
			IPermission[] enumConstants) {
		ROLE role = createRole(t, objecturi);
		PRIVILEGE privilege = createPrivilege(t, objecturi, enumConstants);
		role.addPrivilege(privilege);
		getRoleDAO(t).update(role);
		user.addRole(role);

	}

	private ROLE createRole(ITenant t, String name) {
		ROLE role = createEmptyRole();
		role.setName(name);
		role = getRoleDAO(t).create(role);
		return role;
	}

	private PRIVILEGE createPrivilege(ITenant t, String privURI, IPermission[] permissions) {
		PRIVILEGE privilege = createEmptyPrivilege();
		privilege.setObjectURI(privURI);
		for (IPermission permission : permissions) {
			privilege.addPermission(permission);
		}
		privilege = getPrivilegeDAO(t).create(privilege);
		return privilege;
	}

	@Override
	public boolean isInitialized(ITenant tenant) {
		return (initialized.get(tenant) != null);
	}

	@Override
	public void initialize(ITenant tenant) {
		if (initialized.get(tenant) == null) {
			process_init(tenant);
			initialized.put(tenant, true);
		}
	}

	abstract protected void process_init(ITenant tenant);

}
