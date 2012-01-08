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
package de.uniol.inf.is.odysseus.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.ConfigurationAction;
import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryAction;
import de.uniol.inf.is.odysseus.planmanagement.executor.ExecutorAction;
import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;

/**
 * 
 * @deprecated Replaced by {@link IUserManagement} and
 *             {@link ISessionManagement} service implementation
 * 
 */
@Deprecated
public class UserManagement implements IUserManagement, ISessionManagement {

	static private UserManagement instance = null;

	private Map<String, User> loggedIn = new HashMap<String, User>();

	private IUserStore userStore = null;
	private IStore<String, Role> roleStore;
	private IStore<String, Privilege> privStore;
	private int roleid = 0;
	private int privid = 0;

	private int sessionID = -1;

	// TODO: sortieren

	private List<IUserManagementListener> listeners = new CopyOnWriteArrayList<IUserManagementListener>();

	private IUserManagement usermanagement = null;

	private ISessionManagement sessionmanagement = null;

	private void init(IUserStore userStore) {
		this.userStore = userStore;
		this.roleStore = new MemoryStore<String, Role>();
		this.privStore = new MemoryStore<String, Privilege>();
		this.roleid = 1;
		this.privid = 1;

		try {
			ISession systemSession = null;
			if (this.sessionmanagement != null) {
				systemSession = this.sessionmanagement.login("System",
						"manager".getBytes());
			}
			if ((this.usermanagement != null) && (systemSession != null)) {
				IRole adminRole = this.usermanagement.createRole("sys_admin",
						systemSession);

				IRole dictionaryRole = this.usermanagement.createRole(
						"datadictionary", systemSession);

				IRole configurationRole = this.usermanagement.createRole(
						"configuration", systemSession);

				IRole queryexecutor = this.usermanagement.createRole(
						"queryexecutor", systemSession);

				this.usermanagement.grantRole(systemSession.getUser(),
						adminRole, systemSession);
				this.usermanagement.grantRole(systemSession.getUser(),
						dictionaryRole, systemSession);
				this.usermanagement.grantRole(systemSession.getUser(),
						configurationRole, systemSession);
				this.usermanagement.grantRole(systemSession.getUser(),
						queryexecutor, systemSession);

				this.usermanagement
						.grantPermissions(adminRole, new HashSet<IPermission>(
								UserManagementAction.getAll()),
								"UserManagement", systemSession);

				for (IPermission permission : DataDictionaryAction.getAll()) {
					this.usermanagement.grantPermission(
							systemSession.getUser(), permission,
							"datadictionary", systemSession);
				}
				for (IPermission permission : ConfigurationAction.getAll()) {
					this.usermanagement.grantPermission(
							systemSession.getUser(), permission,
							"configuration", systemSession);
				}
				for (IPermission permission : ExecutorAction.getAll()) {
					this.usermanagement.grantPermission(
							systemSession.getUser(), permission,
							"queryexecutor", systemSession);
				}
			}
			if (this.hasNoUsers()) {
				// create system user
				this.registerUserInt("System", "manager");
			}
			User sys = this.login("System", "manager", false);
			// set protection
			sys.setSystemProtection(sys);

			if (this.hasNoRoles()) {

				// create admin Role
				Role adminrole = new Role("sys_admin", UserManagement
						.getInstance().getRoleID());
				// set protection
				adminrole.setSystemProtection(sys);

				// create permissions for admin_temp
				// List<IUserAction> adminops = new ArrayList<IUserAction>();
				// adminops.addAll(UserManagementAction.getAll());

				// create privilege for admin (kann nicht �ber create, da
				// noch keine Rechte vorhanden
				Privilege adminpriv = new Privilege("UserManagement",
						adminrole, UserManagementAction.getAll(),
						instance.getPrivID());

				// add privilege for admin_temp
				adminrole.addPrivilege(adminpriv);

				// add admin role to system
				sys.addRole(adminrole);

				// create public group
				this.createGroup("Public", sys);

				// ------------------------------------------------
				// create DataDictoinary Role
				Role ddrole = this.createRole("datadictionary", sys);
				// create permission for admin
				// create DataDic Priv and add to Role
				for (IUserAction action : DataDictionaryAction.getAll()) {
					this.grantPermission(sys, "datadictionary", true, action,
							DataDictionaryAction.alias);
				}
				// set protection
				ddrole.setSystemProtection(sys);
				// add to system
				this.grantRole(sys, "datadictionary", "System");

				// ---------------------------------------------------
				// create Configuration Role
				Role conf = this.createRole("configuration", sys);
				for (IUserAction action : ConfigurationAction.getAll()) {
					this.grantPermission(sys, "configuration", true, action,
							ConfigurationAction.alias);
				}
				conf.setSystemProtection(sys);
				this.grantRole(sys, "configuration", "System");

				// -----------------------------------------------------
				// create ExecutorRole
				Role qrole = this.createRole("queryexecutor", sys);
				for (IUserAction action : ExecutorAction.getAll()) {
					this.grantPermission(sys, "queryexecutor", true, action,
							ExecutorAction.alias);
				}
				// set protection
				qrole.setSystemProtection(sys);
				// add to system
				this.grantRole(sys, "queryexecutor", "System");

				// ----- TEST ----
				// instance.registerUser(sys, "Tester", "test");
				// instance.grantPermission(sys, "Tester",
				// UserManagementAction.CREATE_USER, "UserManagement");
				// instance.grantPermission(sys, "Tester",
				// DataDictionaryAction.ADD_ENTITY,
				// DataDictionaryAction.alias);
				// instance.grantPermission(sys, "Tester",
				// DataDictionaryAction.ADD_SOURCETYPE,
				// DataDictionaryAction.alias);
				// instance.grantPermission(sys, "Tester",
				// DataDictionaryAction.ADD_VIEW,
				// DataDictionaryAction.alias);
				// instance.grantRole(sys, "DSUser", "Tester");

				// Create Default Role for users
				this.createRole("DSUser", sys);
				// Anfragen erstellen und entfernen
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.ADD_ENTITY,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.REMOVE_ENTITY,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.ADD_VIEW,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.ADD_STREAM,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.REMOVE_VIEW,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.ADD_SOURCETYPE,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.GET_ENTITY,
						DataDictionaryAction.alias);
				this.grantPermission(sys, "DSUser", true,
						DataDictionaryAction.READ, DataDictionaryAction.alias);

				// Anfrage verwalten
				this.grantPermission(sys, "DSUser", true,
						ExecutorAction.ADD_QUERY, ExecutorAction.alias);
				this.grantPermission(sys, "DSUser", true,
						ExecutorAction.START_QUERY, ExecutorAction.alias);
				this.grantPermission(sys, "DSUser", true,
						ExecutorAction.STOP_QUERY, ExecutorAction.alias);
				this.grantPermission(sys, "DSUser", true,
						ExecutorAction.REMOVE_QUERY, ExecutorAction.alias);

				// System.out.println("grant DSU to Tester");
				// instance.grantRole(sys, "DSUser", "Tester");
				// instance.grantPermission(sys, "Tester",
				// UserManagementAction.REVOKE_ROLE,
				// UserManagementAction.alias);
				// instance.grantPermission(sys, "Tester",
				// UserManagementAction.GRANT_ROLE, UserManagementAction.alias);

				// System.out.println("grant DSU to sys 1st");
				// instance.grantRole(UserManagement.getInstance().login("Tester",
				// "test", false), "DSUser", "System");
				// instance.grantRole(UserManagement.getInstance().login("Tester",
				// "test", false), "sys_admin", "System");
				// instance.grantRole(sys, "DSUser", "Tester");
				// System.out.println("grant DSU to sys 2nd");
				// instance.grantRole(UserManagement.getInstance().login("Tester",
				// "test", false), "DSUser", "Tester");
				// instance.grantRole(UserManagement.getInstance().login("Tester",
				// "test", false), "DSUser", "System");
				// System.out.println("revoke DSU from sys");
				// instance.revokeRole(UserManagement.getInstance().login("Tester",
				// "test", false), "DSUser", "System");
			}

		} catch (UsernameAlreadyUsedException e) {
		} catch (UserStoreException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public synchronized static UserManagement getInstance() {
		if (instance == null) {
			if (Boolean.parseBoolean(OdysseusDefaults.get("storeUsers"))) {
				instance = new UserManagement();
				try {
					instance.init(new FileUserStore(OdysseusDefaults
							.get("userStoreFilename")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				instance = new UserManagement();
				instance.init(new MemoryUserStore());
			}

		}
		return instance;
	}

	public void addUserManagementListener(IUserManagementListener l) {
		listeners.add(l);
	}

	/**
	 * creates a new Privilege and put it to the Store
	 * 
	 * @param objecturi
	 * @param owner
	 * @param operation
	 * @param caller
	 * @return Privilege
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantPermission(IUser, IPermission, String, ISession)}
	 */
	@Deprecated
	private Privilege createPrivilege(String objecturi,
			AbstractUserManagementEntity owner, IUserAction operation,
			User caller) throws StoreException {
		List<IUserAction> operations = new ArrayList<IUserAction>();
		operations.add(operation);

		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement.findUser(owner.getName(),
					systemSession);
			if (user != null) {
				this.usermanagement.grantPermissions(user,
						new HashSet<IPermission>(operations), objecturi,
						systemSession);
			}
		}
		return createPrivilege(objecturi, owner, operations, caller);
	}

	/**
	 * creates a new Privilege and put it to the Store
	 * 
	 * @param privname
	 * @param obj
	 * @param operations
	 * @param caller
	 * @return Privilege or null
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantPermissions(IUser, Set, String, ISession)}
	 */
	@Deprecated
	private Privilege createPrivilege(String objecturi,
			AbstractUserManagementEntity owner, List<IUserAction> operations,
			User caller) throws StoreException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement.findUser(owner.getName(),
					systemSession);
			if (user != null) {
				this.usermanagement.grantPermissions(user,
						new HashSet<IPermission>(operations), objecturi,
						systemSession);
			}
		}
		if (!this.privStore.containsKey(owner.toString() + "::" + objecturi)
				&& owner.hasObject(objecturi) == null) {
			Privilege priv = new Privilege(objecturi, owner, operations,
					getPrivID());
			this.privStore.put(priv.getPrivname(), priv);
			return priv;
		}
		return null;
	}

	/**
	 * creates a new Role and put it to the Store
	 * 
	 * @param rolename
	 * @param privileges
	 * @param caller
	 * @return Role
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#createRole(String, ISession)}
	 */
	@Deprecated
	public Role createRole(String rolename, User caller)
			throws HasNoPermissionException, StoreException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());
			IRole role = this.usermanagement
					.createRole(rolename, systemSession);

		}
		if (AccessControl.hasPermission(UserManagementAction.CREATE_ROLE,
				UserManagementAction.alias, caller)) {
			// wenn role noch nicht in store
			if (!this.roleStore.containsKey(rolename)) {
				Role role = new Role(rolename, getRoleID());
				this.roleStore.put(rolename, role);
				fireRoleChangedEvent();
				return role;
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to create new roles");
		}
		return null;
	}

	/**
	 * deactivates a given user. The user remains in the system and whon't by
	 * deleted by this method.
	 * 
	 * @param caller
	 * @param username
	 * @deprecated Replaced by
	 *             {@link IUserManagement#deactivateUser(IUser, ISession)}
	 */
	@Deprecated
	public void deactivateUser(User caller, String username) {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement.findUser(username, systemSession);
			if (user != null) {
				this.usermanagement.deactivateUser(user, systemSession);
			}
		}
		if (!caller.getUsername().equals(username)
				&& AccessControl.hasPermission(
						UserManagementAction.DEACTIVATE_USER, username, caller)) {
			if (!username.equals(UserManagement.getInstance().getSuperUser()
					.getUsername())) {
				if (logout(caller, username)) {
					this.userStore.getUserByName(username).deaktivate();
				}
			}
		} else {
			throw new HasNoPermissionException("User " + caller.getUsername()
					+ " has no permission to deactivate a user.");
		}
	}

	/**
	 * deletes a given Role permanently
	 * 
	 * @param rolename
	 * @param caller
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#deleteRole(IRole, ISession)}
	 */
	@Deprecated
	public void deleteRole(String rolename, User caller)
			throws HasNoPermissionException, StoreException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IRole role = this.usermanagement.findRole(rolename, systemSession);
			if (role != null) {
				this.usermanagement.deleteRole(role, systemSession);
			}
		}
		if (AccessControl.hasPermission(UserManagementAction.DELETE_ROLE,
				UserManagementAction.alias, caller)) {
			// wenn role noch nicht in store
			if (this.roleStore.containsKey(rolename)) {
				for (User user : this.userStore.getUsers()) {
					if (user.hasRole(rolename) != null) {
						user.removeRole(this.roleStore.get(rolename));
					}
				}
				// alle role privileges entfernen
				removeAllEntityPrivileges(this.roleStore.get(rolename));
				// role entfernen
				this.roleStore.remove(rolename);
				fireRoleChangedEvent();
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to create new roles");
		}
	}

	/**
	 * deletes a user by performing a logout for the user and removing him from
	 * userstore. This method permanently deletes a given user. If you want to
	 * deactivate a user without deleting him, use the deactivateUser method.
	 * 
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#deleteUser(IUser, ISession)}
	 */
	@Deprecated
	public void deleteUser(User caller, String username)
			throws HasNoPermissionException, StoreException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement.findUser(username, systemSession);
			if (user != null) {
				this.usermanagement.deleteUser(user, systemSession);
			}
		}

		if (!caller.toString().equals(username)
				&& AccessControl.hasPermission(
						UserManagementAction.DELETE_USER,
						UserManagementAction.alias, caller)) {
			if (!username.equals(UserManagement.getInstance().getSuperUser()
					.getUsername())) {
				// logout
				if (logout(caller, username)) {
					// remove all user privileges
					removeAllEntityPrivileges(this.userStore
							.getUserByName(username));
					// delete user from store
					if (this.userStore.removeByName(username) != null) {
						fireUserManagementListener();
					}
				}
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to deltete user");
		}
	}

	// @Deprecated
	// public void clearUserStore() throws StoreException {
	// this.userStore.clear();
	// }

	/**
	 * checks if the granted permission has dependencies and grant them too if
	 * the caller has the needed permissions to do so.
	 * 
	 * @param caller
	 * @param entityname
	 * @param action
	 * @param objecturi
	 */
	// TODO: zusammenh�nge granten
	private void dependingGrants(User caller, String entityname,
			IUserAction action, String objecturi, boolean callerIsOwner) {
		if (action instanceof DataDictionaryAction) {
			switch ((DataDictionaryAction) action) {
			case READ:
				this.grantPermission(caller, entityname, callerIsOwner,
						DataDictionaryAction.GET_ENTITY, objecturi);
			default:
				;
			}
		}
		if (action instanceof UserManagementAction) {
			switch ((UserManagementAction) action) {
			default:
				;
			}
		}
		if (action instanceof ExecutorAction) {
			switch ((ExecutorAction) action) {
			default:
				;
			}
		}
	}

	/**
	 * returns the user object from the userStore for the given username.
	 * 
	 * @param username
	 * @param caller
	 * @return User
	 * @deprecated Replaced by
	 *             {@link IUserManagement#findUser(String, ISession)}
	 */
	@Deprecated
	public User findUser(String username, User caller) {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement.findUser(username, systemSession);
		}
		return this.userStore.getUserByName(username);
	}

	private void fireRoleChangedEvent() {
		for (IUserManagementListener l : listeners) {
			l.roleChangedEvent();
		}
	}

	private void fireUserManagementListener() {
		for (IUserManagementListener l : listeners) {
			l.usersChangedEvent();
		}
	}

	/**
	 * returns user or role whether the given name is in the userstore or
	 * rolestore
	 * 
	 * @param name
	 * @return AbstractUserManagementEntity
	 * @deprecated Replaced by
	 *             {@link IUserManagement#findRole(String, ISession)}
	 */
	@Deprecated
	private AbstractUserManagementEntity getEntity(String name) {
		AbstractUserManagementEntity entity = this.userStore
				.getUserByName(name);
		if (entity == null) {
			entity = this.roleStore.get(name);
		}
		return entity;
	}

	protected int getPrivID() {
		return privid++;
	}

	protected int getRoleID() {
		return roleid++;
	}

	private synchronized int getSessionId() {
		return ++sessionID;
	}

	/**
	 * returns all existing users.
	 * 
	 * @param caller
	 * @return Collection<User>
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by {@link IUserManagement#getUsers(ISession)}
	 */
	@Deprecated
	public Collection<User> getUsers(User caller)
			throws HasNoPermissionException {

		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			List<? extends IUser> users = this.usermanagement
					.getUsers(systemSession);
		}
		if (AccessControl.hasPermission(UserManagementAction.GET_ALL_USER,
				UserManagementAction.alias, caller)
				|| hasSuperOperation(UserManagementAction.GET_ALL,
						UserManagementAction.alias, caller)) {
			return this.userStore.getUsers();
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to get all user.");
		}
	}

	/**
	 * grants permissions to a specified user- or role name (by entityname).
	 * 
	 * @param caller
	 * @param entityname
	 * @param operations
	 * @param objecturi
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantPermission(IUser, IPermission, String, ISession)}
	 */
	@Deprecated
	public void grantPermission(User caller, String entityname,
			boolean callerIsOwner, IUserAction operation, String objecturi)
			throws HasNoPermissionException, StoreException {

		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement
					.findUser(entityname, systemSession);
			if (user != null) {
				this.usermanagement.grantPermission(user, operation, objecturi,
						systemSession);
			} else {
				IRole role = this.usermanagement.findRole(entityname,
						systemSession);
				if (role != null) {
					this.usermanagement.grantPermission(role, operation,
							objecturi, systemSession);
				}
			}
		}
		if (hasGrantOrRevokeAccess(caller, entityname, objecturi,
				callerIsOwner, UserManagementAction.GRANT, false)) {
			AbstractUserManagementEntity entity = getEntity(entityname);
			// if entity has't already rights on this object
			if (entity.hasObject(objecturi) == null) {
				entity.addPrivilege(createPrivilege(objecturi, entity,
						operation, caller));
			} else {
				entity.hasObject(objecturi).addOperation(operation);
			}
			if (entity instanceof User) {
				fireUserManagementListener();
			} else {
				fireRoleChangedEvent();
			}

			// vergibt zusammenh�ngende Rechte
			dependingGrants(caller, entityname, operation, objecturi,
					callerIsOwner);
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to grant permission. " + operation
					+ " on " + objecturi);
		}
	}

	/**
	 * Grants a Role to a specified User.
	 * 
	 * @param caller
	 * @param role
	 * @param username
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#grantRole(IUser, IRole, ISession)}
	 */
	@Deprecated
	public void grantRole(User caller, String rolename, String username)
			throws HasNoPermissionException, StoreException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());
			IUser user = this.usermanagement.findUser(username, systemSession);
			IRole role = this.usermanagement.findRole(rolename, systemSession);
			if ((role != null) && (user != null)) {
				this.usermanagement.grantRole(user, role, systemSession);
			}
		}
		if (hasGrantAndRevokeOnRoleAccess(caller, username, rolename,
				UserManagementAction.GRANT_ROLE, false)) {
			User user = this.userStore.getUserByName(username);
			Role role = this.roleStore.get(rolename);
			if (user.hasRole(rolename) == null) {
				if (role != null) {
					try {
						user.addRole(role);
						fireUserManagementListener();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to grant role '" + rolename + "'.");
		}
	}

	/**
	 * checks if the given user has permission to perform a certain action on a
	 * given role. the role is specified by name. Primary used for grant or
	 * revoke access checks.
	 * 
	 * @param caller
	 * @param username
	 * @param rolename
	 * @param action
	 * @return boolean
	 */
	private boolean hasGrantAndRevokeOnRoleAccess(User caller, String username,
			String rolename, IUserAction action, boolean revoke) {
		if (revoke && isRevokeProtected(rolename)) {
			System.out
					.println("Role '" + rolename + "' has revoke protection.");
			return false;
		}
		if ((AccessControl.hasPermission((UserManagementAction) action,
				UserManagementAction.alias, caller)
		// ist in der rolle
				&& caller.hasRole(rolename) != null
		// caller kann sich nicht selbst entfernen
		&& !caller.getUsername().equals(username))
				|| hasSuperOperation((UserManagementAction) action,
						UserManagementAction.alias, caller)) {
			return true;
		}
		return false;
	}

	/**
	 * checks if the given user has permission to perform a certain action on a
	 * given object. Primary used for revoke or grant access check.
	 * 
	 * @param caller
	 * @param entityname
	 * @param objecturi
	 * @param action
	 * @return boolean
	 */
	private boolean hasGrantOrRevokeAccess(User caller, String entityname,
			String objecturi, boolean callerIsOwner, IUserAction action,
			boolean revoke) {
		if (revoke && isRevokeProtected(entityname)) {
			System.out.println("User '" + entityname
					+ "' has revoke protection.");
			return false;
		}
		if (((AccessControl.hasPermission((UserManagementAction) action,
				UserManagementAction.alias, caller)
		// caller hat object
				&& caller.hasObject(objecturi) != null
		// grantet nicht sich selbst
		&& !caller.toString().equals(entityname))

				// oder user is owner of object
				|| (callerIsOwner)

		// oder hat user superPermission von permission
		|| hasSuperOperation((UserManagementAction) action,
					UserManagementAction.alias, caller))) {
			return true;
		}
		return false;
	}

	protected boolean hasNoRoles() {
		return this.roleStore.isEmpty();
	}

	protected boolean hasNoUsers() {
		return this.userStore.isEmpty();
	}

	/**
	 * checks if the given user has higher permission as the given operation.
	 * Calls the corresponding method in the action class.
	 * 
	 * @param operation
	 * @param object
	 * @param user
	 * @return boolean
	 */
	private boolean hasSuperOperation(UserManagementAction operation,
			String object, User user) {
		return AccessControl.hasPermission(
				UserManagementAction.hasSuperAction(operation), object, user);
	}

	/**
	 * 
	 * @deprecated Replaced by
	 *             {@link ISessionManagement#isValid(ISession, ISession)}
	 */
	@Deprecated
	public boolean isLoggedIn(String username) {
		synchronized (loggedIn) {
			return loggedIn.containsKey(username); // Test auf Session?
		}
	}

	/**
	 * 
	 * @deprecated Replaced by {@link ISessionManagement#login(String, byte[])}
	 */
	@Deprecated
	public User login(String username, String password, boolean passwordIsHashed) {
		if (this.sessionmanagement != null) {
			ISession session = this.sessionmanagement.login(username,
					password.getBytes());

		}
		User user = this.userStore.getUserByName(username);
		if (user != null) {
			if (!user.validatePassword(password, passwordIsHashed)) {
				user = null;
			} else {
				synchronized (loggedIn) {
					loggedIn.put(username, user);
				}
				user.setSession(new Session(getSessionId()));
			}
		}
		fireUserManagementListener();
		return user;
	}

	/**
	 * 
	 * @deprecated Replaced by {@link ISessionManagement#login(String, byte[])}
	 */
	@Deprecated
	public User login(String username, User caller) {
		User user = this.userStore.getUserByName(username);
		if (AccessControl.hasPermission(UserManagementAction.SUDO_LOGIN,
				UserManagementAction.alias, caller)) {
			synchronized (loggedIn) {
				loggedIn.put(username, user);
			}
			user.setSession(new Session(getSessionId()));
			fireUserManagementListener();
		} else {
			user = null;
		}
		return user;
	}

	/**
	 * logout a user selected by name
	 * 
	 * @param caller
	 * @param username
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by {@link ISessionManagement#logout(ISession)}
	 */
	@Deprecated
	public boolean logout(User caller, String username)
			throws HasNoPermissionException {
		if (caller.getUsername().equals(username)
				|| AccessControl.hasPermission(UserManagementAction.LOGOUT,
						UserManagementAction.alias, caller)) {
			User user = loggedIn.remove(username);
			if (user != null) {
				user.setSession(null);
			}
			fireUserManagementListener();
			return true;
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission logging out another user.");
		}
	}

	/**
	 * Register new User with plain text password
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#createUser(String, ISession)}
	 */
	@Deprecated
	public void registerUser(User caller, String username, String password)
			throws UsernameAlreadyUsedException, UserStoreException,
			HasNoPermissionException {

		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());
			IUser user = this.usermanagement
					.createUser(username, systemSession);

			if (user != null) {
				this.usermanagement.changePassword(user, password.getBytes(),
						systemSession);
			}
		}
		if (AccessControl.hasPermission(UserManagementAction.CREATE_USER,
				UserManagementAction.alias, caller)) {
			registerUserInt(username, password);
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to create new users.");
		}

	}

	protected void registerUserInt(String username, String password)
			throws UserStoreException, UsernameAlreadyUsedException {
		User user = this.userStore.getUserByName(username);
		if (user == null && !this.roleStore.containsKey(username)) {
			user = new User(username, password);
			this.userStore.storeUser(user);
			user.addRole(roleStore.get("Public"));
		} else {
			throw new UsernameAlreadyUsedException("User " + username
					+ " already used");
		}
		fireUserManagementListener();
	}

	/**
	 * removes all permissions from a given entity (role or user).
	 * 
	 * @param entity
	 * @throws StoreException
	 */
	private void removeAllEntityPrivileges(AbstractUserManagementEntity entity)
			throws StoreException {
		for (Privilege priv : entity.getPrivileges()) {
			this.privStore.remove(priv.getPrivname());
		}
	}

	/**
	 * checks if the given entity has system protection.
	 * 
	 * @see de.uniol.inf.is.odysseus.usermanagement.AbstractUserManagementEntity.java
	 * @param entityname
	 * @return boolean
	 */
	private boolean isRevokeProtected(String entityname) {
		AbstractUserManagementEntity entity = getEntity(entityname);
		return entity.isSystemProtected();
	}

	public void removeUserManagementListener(User caller,
			IUserManagementListener l) throws HasNoPermissionException {
		listeners.remove(l);
	}

	/**
	 * This method provides the functionality for revoking single permissions
	 * from a entity for a given object defined by its uri. Fires change events
	 * to inform corresponding classes.
	 * 
	 * @param caller
	 * @param entityname
	 * @param operation
	 * @param objecturi
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#revokePermission(IUser, IPermission, String, ISession)}
	 */
	@Deprecated
	public void revokePermission(User caller, String entityname,
			boolean callerIsOwner, IUserAction operation, String objecturi)
			throws HasNoPermissionException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());

			IUser user = this.usermanagement
					.findUser(entityname, systemSession);
			if (user != null) {
				this.usermanagement.revokePermission(user, operation,
						objecturi, systemSession);

			} else {
				IRole role = this.usermanagement.findRole(entityname,
						systemSession);
				if (role != null) {
					this.usermanagement.revokePermission(role, operation,
							objecturi, systemSession);
				}
			}
		}
		if (hasGrantOrRevokeAccess(caller, entityname, objecturi,
				callerIsOwner, UserManagementAction.REVOKE, true)) {
			AbstractUserManagementEntity entity = getEntity(entityname);
			// if entity has't already rights on this object
			if (entity.hasObject(objecturi) != null) {
				entity.hasObject(objecturi).removeOperation(operation);
				// wenn Priv leer dann loeschen
				if (entity.hasObject(objecturi).isEmpty()) {
					entity.removePrivilege(objecturi);
					this.privStore.remove(entityname + "::" + objecturi);
				}
			}
			if (entity instanceof Role) {
				fireRoleChangedEvent();
			} else {
				fireUserManagementListener();
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to revoke permission.");
		}
	}

	/**
	 * revokes all permissions on an object from user or role by removing the
	 * privilege for a whole object.
	 * 
	 * @param caller
	 * @param entityname
	 * @param priv
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#revokePermission(IUser, IPermission, String, ISession)}
	 */
	@Deprecated
	public void revokePrivilegeForObject(User caller, String entityname,
			String objecturi, boolean callerIsOwner)
			throws HasNoPermissionException {
		if (hasGrantOrRevokeAccess(caller, entityname, objecturi,
				callerIsOwner, UserManagementAction.REVOKE, true)) {
			AbstractUserManagementEntity entity = getEntity(entityname);
			entity.removePrivilege(objecturi);
			if (entity instanceof User) {
				// TODO remove role with permission on particular object ?
				// for (Role role : ((User) entity).getRoles()) {
				// role.hasPrivilege(objecturi);
				// ((User) entity).removeRole(role);
				// }
				fireUserManagementListener();
			} else {
				fireRoleChangedEvent();
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to revoke privilege for an object");
		}
	}

	/**
	 * revokes a role from a specified user.
	 * 
	 * @param caller
	 * @param rolename
	 * @param username
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#revokeRole(IUser, IRole, ISession)}
	 */
	@Deprecated
	public void revokeRole(User caller, String rolename, String username)
			throws HasNoPermissionException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());
			IUser user = this.usermanagement.findUser(username, systemSession);
			IRole role = this.usermanagement.findRole(rolename, systemSession);
			if ((role != null) && (user != null)) {
				this.usermanagement.revokeRole(user, role, systemSession);
			}
		}
		if (hasGrantAndRevokeOnRoleAccess(caller, username, rolename,
				UserManagementAction.REVOKE_ROLE, true)) {
			User user = this.userStore.getUserByName(username);
			if (user.hasRole(rolename) != null) {
				user.removeRole(this.roleStore.get(rolename));
				fireUserManagementListener();
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to revoke this role");
		}
	}

	/**
	 * Update Users password with plain text password if caller has permission
	 * or is same user
	 * 
	 * @param caller
	 * @param username
	 * @param password
	 * @throws UsernameNotExistException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 * @deprecated Replaced by
	 *             {@link IUserManagement#changePassword(IUser, byte[], ISession)}
	 */
	@Deprecated
	public void updateUserPassword(User caller, String username, String password)
			throws UsernameNotExistException, UserStoreException,
			HasNoPermissionException {
		if ((this.sessionmanagement != null) && (this.usermanagement != null)) {
			ISession systemSession = this.sessionmanagement.login("System",
					"manager".getBytes());
			IUser user = this.usermanagement.findUser(username, systemSession);
			if (user != null) {
				this.usermanagement.changePassword(user, password.getBytes(),
						systemSession);
			}
		}
		if (AccessControl.hasPermission(UserManagementAction.ALTER_USER,
				UserManagementAction.alias, caller)
				|| caller.toString().equals(username)) {
			User user = this.userStore.getUserByName(username);
			if (user != null) {
				user.setPassword(password);
				this.userStore.storeUser(user);
			} else {
				throw new UsernameNotExistException(username);
			}
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to update user password.");
		}
	}

	@Deprecated
	public void createGroup(String string, User sys) {
		Role r = createRole(string, sys);
		r.setGroup(true);
	}

	@Deprecated
	public User getSuperUser() {
		return login("System", "manager", false);
	}

	// ----- New user and session management ------
	protected void bindUserManagement(IUserManagement usermanagement) {
		this.usermanagement = usermanagement;
	}

	protected void unbindUserManagement(IUserManagement usermanagement) {
		this.usermanagement = null;
	}

	protected void bindSessionManagement(ISessionManagement sessionmanagement) {
		this.sessionmanagement = sessionmanagement;
	}

	protected void unbindSessionManagement(ISessionManagement sessionmanagement) {
		this.sessionmanagement = null;
	}

	@Override
	public ISession login(String username, byte[] password) {
		return this.sessionmanagement.login(username, password);
	}

	@Override
	public void logout(ISession caller) {
		this.sessionmanagement.logout(caller);
	}

	@Override
	public boolean isValid(ISession session, ISession caller) {
		return this.sessionmanagement.isValid(session, caller);
	}

	@Override
	public IRole createRole(String name, ISession caller) {
		return this.usermanagement.createRole(name, caller);
	}

	@Override
	public void deleteRole(IRole role, ISession caller) {
		this.usermanagement.deleteRole(role, caller);
	}

	@Override
	public IRole findRole(String name, ISession caller) {
		return this.usermanagement.findRole(name, caller);
	}

	@Override
	public IRole getRole(String roleId, ISession caller) {
		return this.usermanagement.getRole(roleId, caller);
	}

	@Override
	public List<? extends IRole> getRoles(ISession caller) {
		return this.usermanagement.getRoles(caller);
	}

	@Override
	public IUser createUser(String name, ISession caller) {
		return this.usermanagement.createUser(name, caller);
	}

	@Override
	public void changePassword(IUser user, byte[] password, ISession caller) {
		this.usermanagement.changePassword(user, password, caller);
	}

	@Override
	public void activateUser(IUser user, ISession caller) {
		this.usermanagement.activateUser(user, caller);
	}

	@Override
	public void deactivateUser(IUser user, ISession caller) {
		this.usermanagement.deactivateUser(user, caller);

	}

	@Override
	public void deleteUser(IUser user, ISession caller) {
		this.usermanagement.deleteUser(user, caller);
	}

	@Override
	public IUser findUser(String name, ISession caller) {
		return this.usermanagement.findUser(name, caller);
	}

	@Override
	public IUser getUser(String userId, ISession caller) {
		return this.usermanagement.getUser(userId, caller);
	}

	@Override
	public List<? extends IUser> getUsers(ISession caller) {
		return this.usermanagement.getUsers(caller);
	}

	@Override
	public void grantRole(IUser user, IRole role, ISession caller) {
		this.usermanagement.grantRole(user, role, caller);
	}

	@Override
	public void revokeRole(IUser user, IRole role, ISession caller) {
		this.usermanagement.revokeRole(user, role, caller);
	}

	@Override
	public void grantPermission(IUser user, IPermission permission,
			String objectURI, ISession caller) {
		this.usermanagement
				.grantPermission(user, permission, objectURI, caller);
	}

	@Override
	public void grantPermissions(IUser user, Set<IPermission> permissions,
			String objectURI, ISession caller) {
		this.usermanagement.grantPermissions(user, permissions, objectURI,
				caller);
	}

	@Override
	public void grantPermission(IRole role, IPermission permission,
			String objectURI, ISession caller) {
		this.usermanagement
				.grantPermission(role, permission, objectURI, caller);
	}

	@Override
	public void grantPermissions(IRole role, Set<IPermission> permissions,
			String objectURI, ISession caller) {
		this.usermanagement.grantPermissions(role, permissions, objectURI,
				caller);
	}

	@Override
	public void revokePermission(IUser user, IPermission permission,
			String objectURI, ISession caller) {
		this.usermanagement.revokePermission(user, permission, objectURI,
				caller);
	}

	@Override
	public void revokePermissions(IUser user, Set<IPermission> permissions,
			String objectURI, ISession caller) {
		this.usermanagement.revokePermissions(user, permissions, objectURI,
				caller);

	}

	@Override
	public void revokePermission(IRole role, IPermission permission,
			String objectURI, ISession caller) {
		this.usermanagement.revokePermission(role, permission, objectURI,
				caller);
	}

	@Override
	public void revokePermissions(IRole role, Set<IPermission> permissions,
			String objectURI, ISession caller) {
		this.usermanagement.revokePermissions(role, permissions, objectURI,
				caller);
	}

	@Override
	public boolean hasPermission(ISession caller, IPermission permission,
			String objectURI) {
		return usermanagement.hasPermission(caller, permission, objectURI);
	}
}
