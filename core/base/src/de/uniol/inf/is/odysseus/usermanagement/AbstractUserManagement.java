package de.uniol.inf.is.odysseus.usermanagement;

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

	public AbstractUserManagement(IUserStore userStore) {
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
					"UserManagement", caller)) {
				registerUserInt(username, password);
			} else {
				throw new HasNoPermissionException("User " + caller.toString()
						+ " has no permission to create new user.");
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
		if (AccessControl.hasPermission(
				UserManagementAction.UPDATE_USER_PASSWORD, "UserManagement",
				caller)
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
				System.out.println("User " + username + " logged in!");
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
		// TODO StreamSQL statemant zum logout
		if (caller.getUsername().equals(username)
				|| AccessControl.hasPermission(UserManagementAction.LOGOUT,
						"UserManagement", caller)) {
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

	@Deprecated
	public void clearUserStore() throws StoreException {
		// ,HasNoPermissionException {
		// if
		// (AccessControl.hasPermission(UserManagementActions.CLEAR_USERSTORE,
		// null, caller)) {
		this.userStore.clear();
		// } else {
		// throw new HasNoPermissionException("User " + caller.toString()
		// + " has no permission to clear userstore.");
		// }
	}

	public User findUser(String username, User caller) {
		// throws HasNoPermissionException {
		// if (AccessControl.hasPermission(UserManagementActions.FIND_USER,
		// null,
		// caller)) {
		return this.userStore.getUserByName(username);
		// } else {
		// throw new HasNoPermissionException("User " + caller.toString()
		// + " has no permission for finding user.");
		// }
	}

	public Collection<User> getUsers(User caller)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementAction.GET_ALL_USER,
				"UserManagement", caller)
				|| hasSuperOperation(UserManagementAction.GET_ALL_USER,
						"UserManagement", caller)) {
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
			List<IUserAction> operations, String objecturi)
			throws HasNoPermissionException, StoreException {
		// TODO auf object owner prüfen ?
		if (((AccessControl.hasPermission(UserManagementAction.GRANT,
				"UserManagement", caller)
		// caller hat object
		&& caller.hasObject(objecturi) != null)
		// hat user superPermission von permission
		|| hasSuperOperation(UserManagementAction.GRANT, "UserManagement",
				caller))) {
			AbstractUserManagementEntity entity = getEntity(entityname);
			// if entity has't already rights on this object
			if (entity.hasObject(objecturi) == null) {
				entity.addPrivilege(createPrivilege(objecturi, entity,
						operations, caller));
			} else {
				entity.hasObject(objecturi).addOperations(operations);
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

	public void revokePermission(User caller, String name,
			List<IUserAction> operations, String objecturi)
			throws HasNoPermissionException {
		// TODO auf object owner prüfen ?
		// TODO remove obj, hiermit -> siehe revokePermissionForObject
		if (!caller.toString().equals(name)
				&& ((AccessControl.hasPermission(UserManagementAction.GRANT,
						"UserManagement", caller)
				// caller hat object
				&& caller.hasObject(objecturi) != null)
				// hat user superPermission von permission
				|| hasSuperOperation(UserManagementAction.GRANT,
						"UserManagement", caller))) {
			AbstractUserManagementEntity entity = getEntity(name);
			// if entity has't already rights on this object
			if (entity.hasObject(objecturi) != null) {
				entity.hasObject(objecturi).removeOperations(operations);
				// wenn Priv leer dann loeschen
				if (entity.hasObject(objecturi).isEmpty()) {
					entity.removePrivilege(objecturi);
					try {
						this.privStore.remove(name + "::" + objecturi);
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
	 * @param name
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void revokePrivilegeForObject(User caller, String name,
			String objecturi) throws HasNoPermissionException {
		// TODO siehe revokePermission
		// geht nicht mit revokePermissions, da man nicht weiß, welche
		// permissions enthalten sind
		if (!caller.toString().equals(name)
				&& ((AccessControl.hasPermission(UserManagementAction.REVOKE,
						"UserManagement", caller) && caller
						.hasObject(objecturi) != null) || hasSuperOperation(
						UserManagementAction.REVOKE, objecturi, caller))) {
			AbstractUserManagementEntity entity = getEntity(name);
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
				"UserManagement", caller)
		// ist in der rolle
		&& caller.hasRole(rolename) != null)
		// hat superPermission
		|| hasSuperOperation(UserManagementAction.GRANT, "UserManagement",
				caller))) {
			User user = this.userStore.getUserByName(username);
			Role role = this.roleStore.get(rolename);
			if (user.hasRole(rolename) == null && role != null) {
				try {
					System.out.println(role.getRolename());
					user.addRole(role);
					fireUserManagementListener();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to grant roles");
		}
	}

	// TODO create hasSuperAction methode
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
		if (AccessControl.hasPermission(UserManagementAction.REVOKE,
				"UserManagement", caller)
				|| hasSuperOperation(UserManagementAction.REVOKE,
						"UserManagement", caller)
				// caller kann sich nicht selbst entfernen
				&& !caller.getUsername().equals(username)) {
			User user = this.userStore.getUserByName(username);
			if (user.hasRole(rolename) != null) {
				user.removeRole(this.roleStore.get(rolename));
				fireUserManagementListener();
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to revoke roles");
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
						UserManagementAction.DELETE_USER, "UserManagement",
						caller)) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				"UserManagement", caller)) {
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
				"UserManagement", caller)) {
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
	public Privilege createPrivilege(String objecturi,
			AbstractUserManagementEntity owner, List<IUserAction> operations,
			User caller) throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementAction.CREATE_PRIV,
				"UserManagement", caller)) {
			if (!this.privStore
					.containsKey(owner.toString() + "::" + objecturi)
					&& owner.hasObject(objecturi) == null) {
				Privilege priv = new Privilege(objecturi, owner, operations,
						getPrivID());
				this.privStore.put(priv.getPrivname(), priv);
				return priv;
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to create new privileges");
		}
		return null;
	}

	private List<IUserManagementListener> listeners = new CopyOnWriteArrayList<IUserManagementListener>();

	public void addTenantManagementListener(IUserManagementListener l) {
		// throws HasNoPermissionException {
		// if (AccessControl.hasPermission(UserManagementActions.ADD_LISTENER,
		// null, caller)) {
		listeners.add(l);
		// } else {
		// throw new HasNoPermissionException("" + caller.toString()
		// + " has no permission to add listener");
		// }
	}

	public void removeTenantManagementListener(User caller,
			IUserManagementListener l) throws HasNoPermissionException {
		// if
		// (AccessControl.hasPermission(UserManagementActions.REMOVE_LISTENER,
		// null, caller)) {
		listeners.remove(l);
		// } else {
		// throw new HasNoPermissionException("" + caller.toString()
		// + " has no permission to add listener");
		// }
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
		// throws HasNoPermissionException {
		// if (AccessControl.hasPermission(UserManagementActions.IS_LOGGEDIN,
		// null, caller)) {
		return loggedIn.containsKey(username);
		// } else {
		// throw new HasNoPermissionException("User " + caller.toString()
		// + " has no permission to check loginstatus of user.");
		// }
	}

	public int getRoleID() {
		return roleid++;
	}

	public int getPrivID() {
		return privid++;
	}

}
