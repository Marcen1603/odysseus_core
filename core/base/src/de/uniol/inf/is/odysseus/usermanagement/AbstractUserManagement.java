package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;

abstract class AbstractUserManagement {

	private Map<String, User> loggedIn = new HashMap<String, User>();

	private IUserStore userStore = null;
	final private IStore<String, Role> roleStore;
	final private IStore<String, Privilege> privStore;
	private int roleid = 0;
	private int privid = 0;

	private int sessionID = -1;

	protected AbstractUserManagement(IUserStore userStore) {
		this.userStore = userStore;
		this.roleStore = new MemoryStore<String, Role>();
		this.privStore = new MemoryStore<String, Privilege>();
		this.roleid = 1;
		this.privid = 1;
	}

	/**
	 * Register new User with plain text password
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 */
	public void registerUser(User caller, String username, String password)
			throws UsernameAlreadyUsedException, UserStoreException,
			HasNoPermissionException {
		try {
			if (AccessControl.hasPermission(UserManagementAction.CREATE_USER,
					UserManagementAction.alias, caller)) {
				registerUserInt(username, password);
			} else {
				throw new HasNoPermissionException("User " + caller.toString()
						+ " has no permission to create new users.");
			}
		} catch (HasNoPermissionException e) {
			new RuntimeException(e);
		}
	}

	protected void registerUserInt(String username, String password)
			throws UserStoreException, UsernameAlreadyUsedException {
		User user = this.userStore.getUserByName(username);
		if (user == null) {
			user = new User(username, password);
			this.userStore.storeUser(user);
		} else {
			throw new UsernameAlreadyUsedException();
		}
		fireUserManagementListener();
	}

	/**
	 * 
	 * Update Users password with plain text password if caller has permission
	 * or is same user
	 * 
	 * @param caller
	 * @param username
	 * @param password
	 * @throws UsernameNotExistException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 */
	public void updateUserPassword(User caller, String username, String password)
			throws UsernameNotExistException, UserStoreException,
			HasNoPermissionException {
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

	/**
	 * Login user with non hash password
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username, String password) {
		// no restric
		return login(username, password, false);
	}

	/**
	 * Get user with hash password
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws HasNoPermissionException
	 */
	public User getUser(String username, String password) {
		// no restric
		return login(username, password, true);
	}

	private User login(String username, String password,
			boolean passwordIsHashed) {
		User user = this.userStore.getUserByName(username);
		if (user != null) {
			if (!user.validatePassword(password, passwordIsHashed)) {
				user = null;
			} else {
				loggedIn.put(username, user);
				//System.out.println("User " + username + " logged in!");
				user.setSession(new Session(getSessionId()));
			}
		}
		fireUserManagementListener();
		return user;
	}

	private synchronized int getSessionId() {
		return ++sessionID;
	}

	/**
	 * logout a user selected by name
	 * 
	 * @param caller
	 * @param username
	 * @throws HasNoPermissionException
	 */
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

	protected boolean hasNoUsers() {
		return this.userStore.isEmpty();
	}

	protected boolean hasNoRoles() {
		return this.roleStore.isEmpty();
	}

//	@Deprecated
//	public void clearUserStore() throws StoreException {
//		this.userStore.clear();
//	}

	public User findUser(String username, User caller) {
		return this.userStore.getUserByName(username);
	}

	public Collection<User> getUsers(User caller)
			throws HasNoPermissionException {
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
	 * grants permissions to a specified user- or role name (by entityname)
	 * 
	 * @param caller
	 * @param entityname
	 * @param operations
	 * @param objecturi
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantPermission(User caller, String entityname,
			IUserAction operation, String objecturi)
			throws HasNoPermissionException, StoreException {
		if (((AccessControl.hasPermission(UserManagementAction.GRANT,
				UserManagementAction.alias, caller)
		// caller has object
				&& caller.hasObject(objecturi) != null
				// is creator of Object || (obj is UM || DD)
				&& ((objecturi.equals(UserManagementAction.alias) || objecturi
						.equals("DataDictionary")) || AccessControl
						.isCreatorOfObject(caller.getUsername(), objecturi))
		// caller has permission he want to grant
		&& AccessControl.hasPermission(operation, objecturi, caller))
		// hat user superPermission von permission
		|| hasSuperOperation(UserManagementAction.GRANT,
				UserManagementAction.alias, caller))) {

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
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to grant permission.");
		}
	}

	public void revokePermission(User caller, String entityname,
			IUserAction operation, String objecturi)
			throws HasNoPermissionException {
		if (!caller.toString().equals(entityname)
				&& ((AccessControl.hasPermission(UserManagementAction.GRANT,
						UserManagementAction.alias, caller)
				// caller hat object
						&& caller.hasObject(objecturi) != null
				// user is owner of object
				&& AccessControl.isCreatorOfObject(caller.getUsername(),
						objecturi))
				// hat user superPermission von permission
				|| hasSuperOperation(UserManagementAction.GRANT,
						UserManagementAction.alias, caller))) {
			AbstractUserManagementEntity entity = getEntity(entityname);
			// if entity has't already rights on this object
			if (entity.hasObject(objecturi) != null) {
				entity.hasObject(objecturi).removeOperation(operation);
				// wenn Priv leer dann loeschen
				if (entity.hasObject(objecturi).isEmpty()) {
					entity.removePrivilege(objecturi);
					try {
						this.privStore.remove(entityname + "::" + objecturi);
					} catch (StoreException e) {
						new RuntimeException(e);
					}
				}
			}
			if (entity instanceof Role) {
				fireRoleChangedEvent();
			} else {
				fireUserManagementListener();
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to grant permission.");
		}
	}

	/**
	 * returns user or role whether the given name is in the userstore or
	 * rolestore
	 * 
	 * @param name
	 * @return AbstractUserManagementEntity
	 */
	private AbstractUserManagementEntity getEntity(String name) {
		AbstractUserManagementEntity entity = this.userStore
				.getUserByName(name);
		if (entity == null) {
			entity = this.roleStore.get(name);
		}
		return entity;
	}

	/**
	 * revokes all permissions on an object from user or role (removes the
	 * privilege)
	 * 
	 * @param caller
	 * @param entityname
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void revokePrivilegeForObject(User caller, String entityname,
			String objecturi) throws HasNoPermissionException {
		if (!caller.toString().equals(entityname)
				&& ((AccessControl.hasPermission(UserManagementAction.REVOKE,
						UserManagementAction.alias, caller) && caller
						.hasObject(objecturi) != null) || hasSuperOperation(
						UserManagementAction.REVOKE,
						UserManagementAction.alias, caller))) {
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
	 * Grant a Role to a specified User
	 * 
	 * @param caller
	 * @param role
	 * @param username
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantRole(User caller, String rolename, String username)
			throws HasNoPermissionException, StoreException {
		if (((AccessControl.hasPermission(UserManagementAction.GRANT,
				UserManagementAction.alias, caller)
		// ist in der rolle
		&& caller.hasRole(rolename) != null)
		// hat superPermission
		|| hasSuperOperation(UserManagementAction.GRANT,
				UserManagementAction.alias, caller))) {
			User user = this.userStore.getUserByName(username);
			Role role = this.roleStore.get(rolename);
			if (user.hasRole(rolename) == null) {
				if (role != null) {
					try {
						System.out.println(role.getRolename());
						user.addRole(role);
						fireUserManagementListener();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to grant this role");
		}
	}

	/**
	 * returns true if the given user has higher permission as the given
	 * operation
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
	 * revoke a role from a specified user
	 * 
	 * @param caller
	 * @param rolename
	 * @param user
	 * @throws HasNoPermissionException
	 */
	public void revokeRole(User caller, String rolename, String username)
			throws HasNoPermissionException {
		if ((AccessControl.hasPermission(UserManagementAction.REVOKE,
				UserManagementAction.alias, caller) && caller.hasRole(rolename) != null)
				|| hasSuperOperation(UserManagementAction.REVOKE,
						UserManagementAction.alias, caller)
				// caller kann sich nicht selbst entfernen
				&& !caller.getUsername().equals(username)) {
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
	 * deletes a user by performing a logout for the user and removing him from
	 * userstore
	 * 
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void deleteUser(User caller, String username)
			throws HasNoPermissionException, StoreException {
		if (!caller.toString().equals(username)
				&& AccessControl.hasPermission(
						UserManagementAction.DELETE_USER,
						UserManagementAction.alias, caller)) {
			if (!username.equals("System")) {
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

	private void removeAllEntityPrivileges(AbstractUserManagementEntity entity)
			throws StoreException {
		try {
			for (Privilege priv : entity.getPrivileges()) {
				System.out.println("Privilege: " + priv.getPrivname()
						+ " deleted.");
				this.privStore.remove(priv.getPrivname());
			}
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}

	/**
	 * creates a Role and put it to the Store
	 * 
	 * @param rolename
	 * @param privileges
	 * @param caller
	 * @return
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public Role createRole(String rolename, User caller)
			throws HasNoPermissionException, StoreException {
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

	public void deleteRole(String rolename, User caller)
			throws HasNoPermissionException, StoreException {
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
	 * creats a Privilege and put it to the Store
	 * 
	 * @param privname
	 * @param obj
	 * @param operations
	 * @param caller
	 * @return
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	private Privilege createPrivilege(String objecturi,
			AbstractUserManagementEntity owner, List<IUserAction> operations,
			User caller) throws StoreException {
		if (!this.privStore.containsKey(owner.toString() + "::" + objecturi)
				&& owner.hasObject(objecturi) == null) {
			Privilege priv = new Privilege(objecturi, owner, operations,
					getPrivID());
			this.privStore.put(priv.getPrivname(), priv);
			return priv;
		}
		return null;
	}

	private Privilege createPrivilege(String objecturi,
			AbstractUserManagementEntity owner, IUserAction operation,
			User caller) throws HasNoPermissionException, StoreException {
		List<IUserAction> operations = new ArrayList<IUserAction>();
		operations.add(operation);
		return createPrivilege(objecturi, owner, operations, caller);
	}

	private List<IUserManagementListener> listeners = new CopyOnWriteArrayList<IUserManagementListener>();

	public void addUserManagementListener(IUserManagementListener l) {
		listeners.add(l);
	}

	public void removeUserManagementListener(User caller,
			IUserManagementListener l) throws HasNoPermissionException {
		listeners.remove(l);
	}

	private void fireUserManagementListener() {
		for (IUserManagementListener l : listeners) {
			l.usersChangedEvent();
		}
	}

	private void fireRoleChangedEvent() {
		for (IUserManagementListener l : listeners) {
			l.roleChangedEvent();
		}
	}

	public boolean isLoggedIn(String username) {
		return loggedIn.containsKey(username);
	}

	protected int getRoleID() {
		return roleid++;
	}

	protected int getPrivID() {
		return privid++;
	}

}
