package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;

abstract public class AbstractUserManagement {

	private Map<String, User> loggedIn = new HashMap<String, User>();

	private IUserStore userStore = null;
	final private IStore<String, Role> roleStore;
	final private IStore<String, Privilege> privStore;
	private int roleid = 0;
	private int privid = 0;

	private int sessionID = -1;

	public AbstractUserManagement(IUserStore userStore) {
		this.userStore = userStore;
		roleStore = new MemoryStore<String, Role>();
		privStore = new MemoryStore<String, Privilege>();
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
		if (AccessControl.hasPermission(UserManagementActions.CREATE_USER,
				null, caller)) {
			registerUserInt(username, password);
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to create new user.");
		}
	}

	protected void registerUserInt(String username, String password)
			throws UserStoreException, UsernameAlreadyUsedException {
		User user = userStore.getUserByName(username);
		if (user == null) {
			user = new User(username, password);
			userStore.storeUser(user);
		} else {
			throw new UsernameAlreadyUsedException();
		}
		fireUserManagementListener();
	}

	/**
	 * Update Users password with plain text password
	 * 
	 * @param username
	 * @param password
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 */
	public void updateUserPassword(User caller, String username, String password)
			throws UsernameNotExistException, UserStoreException,
			HasNoPermissionException {
		if (AccessControl.hasPermission(
				UserManagementActions.UPDATE_USER_PASSWORD, null, caller)) {
			User user = userStore.getUserByName(username);
			if (user != null) {
				user.setPassword(password);
				userStore.storeUser(user);
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
		// throws HasNoPermissionException {
		// if (AccessControl.hasPermission(UserManagementActions.GET_USER, null,
		// caller)) {
		return login(username, password, true);
		// } else {
		// throw new HasNoPermissionException("User " + caller.toString()
		// + " has no permission to get user.");
		// }
	}

	private User login(String username, String password,
			boolean passwordIsHashed) {
		User user = userStore.getUserByName(username);
		if (user != null) {
			if (!user.validatePassword(password, passwordIsHashed)) {
				user = null;
			} else {
				loggedIn.put(username, user);
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
	public void logout(User caller, String username)
			throws HasNoPermissionException {
		if (caller.getUsername().equals(username)
				|| AccessControl.hasPermission(UserManagementActions.LOGOUT,
						null, caller)) {
			User user = loggedIn.remove(username);
			user.setSession(null);
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission logging out another user.");
		}
	}

	protected boolean hasNoUsers() {
		return userStore.isEmpty();
	}

	protected boolean hasNoRoles() {
		return this.roleStore.isEmpty();
	}

	public void clearUserStore() throws StoreException {
		// ,HasNoPermissionException {
		// if
		// (AccessControl.hasPermission(UserManagementActions.CLEAR_USERSTORE,
		// null, caller)) {
		userStore.clear();
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
		return userStore.getUserByName(username);
		// } else {
		// throw new HasNoPermissionException("User " + caller.toString()
		// + " has no permission for finding user.");
		// }
	}

	public Collection<User> getUsers() {
		// throws HasNoPermissionException {
		// if (AccessControl.hasPermission(UserManagementActions.GET_ALL_USER,
		// null, caller)) {
		return userStore.getUsers();
		// } else {
		// throw new HasNoPermissionException("User " + caller.toString()
		// + " has no permission to get all user.");
		// }
	}

	/**
	 * grants permissions to a specified user- or role name
	 * 
	 * @param caller
	 * @param name
	 * @param operations
	 * @param objecturi
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantPermission(User caller, String name,
			List<IUserActions> operations, String objecturi)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.GRANT, null,
				caller)) {
			AbstractUserManagementEntity entity = getEntety(name);
			// if entity has't already rights on this object
			if (entity.hasObject(objecturi) == null) {
				entity.addPrivilege(createPrivilege(objecturi, entity,
						operations, caller));
			} else {
				entity.getPrivilegeByName(name + "::" + objecturi)
						.addOperations(operations);
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
	AbstractUserManagementEntity getEntety(String name) {
		AbstractUserManagementEntity entity = userStore.getUserByName(name);
		if (entity == null) {
			entity = roleStore.get(name);
		}
		return entity;
	}

	/**
	 * returns a list of actions possible for the type of object
	 * 
	 * @param objectset
	 * @param operationnames
	 * @return
	 */
	private List<IUserActions> getOperationEnums(Enum<ObjectSet> objectset,
			List<String> operationnames) {
		// TODO create new List<Enums>
		// switch objectset
		if (objectset == ObjectSet.QUERY) {
			System.out.println("It's a Query");
		}
		if (objectset == ObjectSet.SOURCE) {
			System.out.println("It's a Source");
		}
		if (objectset == ObjectSet.VIEW) {
			System.out.println("It's a View");
		}
		if (objectset == ObjectSet.USER) {
			System.out.println("It's a User");
		}

		return null;
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
	public void revokeObject(User caller, String name, String objecturi)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementActions.REVOKE,
				"UserManagement", caller)) {
			AbstractUserManagementEntity entity = getEntety(name);
			entity.removePrivilege(objecturi);
			if (entity instanceof User) {
				// remove role with permission on particular object
				for (Role role : ((User) entity).getRoles()) {
					role.hasPrivilege(objecturi);
					((User) entity).removeRole(role);
				}
				fireUserManagementListener();
			} else {
				// TODO fire Listener role changed
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to revoke privilege for an object");
		}
	}

	/**
	 * Grant a Role to a specified User
	 * 
	 * @param grantUser
	 * @param role
	 * @param user
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantRoleToUser(User grantUser, String rolename, User user)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.GRANT, null,
				grantUser) && user.hasRole(rolename) == null) {
			try {
				user.addRole(roleStore.get(rolename));
				fireUserManagementListener();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new HasNoPermissionException("User " + grantUser.toString()
					+ " has no permission to grant roles");
		}
	}

	/**
	 * revoke a role from a specified user
	 * 
	 * @param revokeUser
	 * @param rolename
	 * @param user
	 * @throws HasNoPermissionException
	 */
	public void revokeRoleFromUser(User revokeUser, String rolename, User user)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementActions.REVOKE, null,
				revokeUser) && user.hasRole(rolename) != null) {
			user.removeRole(roleStore.get(rolename));
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException("User " + revokeUser.toString()
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
	public void deleteUser(User delUser, String username)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.DELETE_USER,
				null, delUser)) {
			if (!username.equals("System")) {
				// logout
				logout(delUser, username);
				// delete user from store
				if (userStore.removeByName(username) != null) {
					fireUserManagementListener();
				}
			}
		} else {
			throw new HasNoPermissionException("User " + delUser.toString()
					+ " has no permission to deltete user");
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
		if (AccessControl.hasPermission(UserManagementActions.CREATE_ROLE,
				null, caller)) {
			// wenn role noch nicht in store
			if (!roleStore.containsKey(rolename)) {
				Role role = new Role(rolename, getRoleID());
				roleStore.put(rolename, role);
				return role;
			}
		} else {
			throw new HasNoPermissionException("User " + caller.toString()
					+ " has no permission to create new roles");
		}
		return null;
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
			AbstractUserManagementEntity owner, List<IUserActions> operations,
			User caller) throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.CREATE_PRIV,
				"UserManagement", caller)) {
			if (!privStore.containsKey(owner.toString() + "::" + objecturi)
					&& owner.getPrivilegeByName(owner.toString() + "::"
							+ objecturi) == null) {
				Privilege priv = new Privilege(objecturi, owner, operations,
						getPrivID());
				privStore.put(priv.getPrivname(), priv);
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
