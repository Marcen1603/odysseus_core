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
		if (AccessControl.hasPermission(UserManagementActions.CREATEUSER, null,
				caller)) {
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
				UserManagementActions.UPDATEUSER_PASSWORD, null, caller)) {
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
	 */
	public User getUser(String username, String password) {
		return login(username, password, true);
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

	public void logout(String username) {
		User user = loggedIn.remove(username);
		user.setSession(null);
		fireUserManagementListener();
	}

	protected boolean hasNoUsers() {
		return userStore.isEmpty();
	}

	public void clearUserStore() throws StoreException {
		userStore.clear();
	}

	public User findUser(String username, User caller) {
		// Todo: Testen ob caller das darf
		return userStore.getUserByName(username);
	}

	public Collection<User> getUsers() {
		return userStore.getUsers();
	}

	public boolean hasNoRoles() {
		return this.roleStore.isEmpty();
	}

	/**
	 * grant permission to a user. checks if grantUser is admin
	 * 
	 * @param grantUser
	 * @param user
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void grantPrivilegeToUser(User grantUser, User user, Privilege priv)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.GRANT, null,
				grantUser) && user.hasPrivilege(priv) == null) {
			try {
				// user.addPrivilege(priv);
				user.addPrivilege(privStore.get(priv.getPrivname()));
				fireUserManagementListener();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException("User " + grantUser.toString()
					+ " has no permission to grant privileges.");
		}
	}

	/**
	 * grants permission to user with object and list of operation-enums. calls
	 * method with privilege parameter
	 * 
	 * @param grantUser
	 * @param user
	 * @param obj
	 * @param operations
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantPermissionToUser(User grantUser, User user, String obj,
			List<IUserActions> operations) throws HasNoPermissionException,
			StoreException {
		if (user.hasObject(obj) == null) {
			grantPrivilegeToUser(grantUser, user,
					createPrivilege(null, obj, operations, grantUser));
		}
	}

	/**
	 * grants permission to user with object and list of operation-enums. calls
	 * method with privilege parameter
	 * 
	 * @param grantUser
	 * @param username
	 * @param object
	 * @param operations
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantPermissionToUser(User grantUser, String username,
			Enum<ObjectSet> objectset, String object, List<String> operations)
			throws HasNoPermissionException, StoreException {
		User user = userStore.getUserByName(username);
		if (operations != null && operations.size() > 0) {
			List<IUserActions> newoperations = getOperationEnums(objectset,
					operations);
			if (user != null) {
				if (user.hasObject(object) == null) {
					grantPrivilegeToUser(
							grantUser,
							user,
							createPrivilege(null, object, newoperations,
									grantUser));
				}
			}
		}
	}

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
	 * grants permission to a role. checks if grantUser has permission
	 * 
	 * @param grantUser
	 * @param role
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void grantPrivilegeToRole(User grantUser, Role role, Privilege priv)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.GRANT, null,
				grantUser)) {
			try {
				// role.addPrivileges(priv);
				role.addPrivilege(privStore.get(priv.getPrivname()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException("User " + grantUser.toString()
					+ " has no permission to grant a role");
		}
	}

	/**
	 * grants permission to role with object and list of operation-enums. calls
	 * method with privilege parameter
	 * 
	 * @param grantUser
	 * @param role
	 * @param obj
	 * @param operations
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public void grantPermissionToRole(User grantUser, Role role, String obj,
			List<IUserActions> operations) throws HasNoPermissionException,
			StoreException {
		if (AccessControl.hasPermission(UserManagementActions.GRANT, null,
				grantUser)) {
			try {
				grantPrivilegeToRole(grantUser, role,
						createPrivilege(null, obj, operations, grantUser));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException("User " + grantUser.toString()
					+ " has no permission to grant privileges");
		}
	}

	/**
	 * Revoke a Permission (Privilege) from a specifed User
	 * 
	 * @param revokeUser
	 * @param user
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void revokePrivilegeFromUser(User revokeUser, User user,
			Privilege priv) throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementActions.REVOKE, null,
				revokeUser)) {
			if (user.hasPrivilege(priv) != null) {
				user.removePrivilege(priv);
			}
			Role role = user.hasPrivilegeInRole(priv);
			if (role != null) {
				user.removeRole(role);
			}
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException("User " + revokeUser.toString()
					+ " has no permission to revoke privileges");
		}
	}

	/**
	 * Revoke a Permission (Privilege) from a specified Role
	 * 
	 * @param revokeUser
	 * @param role
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void revokePrivilegeFromRole(User revokeUser, Role role,
			Privilege priv) throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementActions.REVOKE, null,
				revokeUser)) {
			role.removePrivilege(priv);
		} else {
			throw new HasNoPermissionException("User " + revokeUser.toString()
					+ " has no permission to revoke privileges");
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
	public void grantRoleToUser(User grantUser, Role role, User user)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.GRANT, null,
				grantUser) && user.hasRole(role) == null) {
			try {
				user.addRole(roleStore.get(role.getRolename()));
				fireUserManagementListener();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException("User " + grantUser.toString()
					+ " has no permission to grant privileges");
		}
	}

	/**
	 * Revke a Role from a specified User
	 * 
	 * @param revokeUser
	 * @param role
	 * @param user
	 * @throws HasNoPermissionException
	 */
	public void revokeRoleFromUser(User revokeUser, Role role, User user)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementActions.REVOKE, null,
				revokeUser) && user.hasRole(role) != null) {
			user.removeRole(role);
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException("User " + revokeUser.toString()
					+ " has no permission to revoke roles");
		}
	}

	/**
	 * TBD: not jet implemented
	 * 
	 * @throws HasNoPermissionException
	 */
	public void deleteUser(User delUser, String username)
			throws HasNoPermissionException {
		if (AccessControl.hasPermission(UserManagementActions.DELETEUSER, null,
				delUser)) {
			// TODO del user
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
	 * @param createUser
	 * @return
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public Role createRole(String rolename, List<Privilege> privileges,
			User createUser) throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.CREATEROLE, null,
				createUser)) {
			// wenn role noch nicht in store
			if (!roleStore.containsKey(rolename)) {
				Role role = new Role(rolename, privileges, this.roleid++);
				roleStore.put(rolename, role);
				return role;
			}
		} else {
			throw new HasNoPermissionException("User " + createUser.toString()
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
	 * @param createUser
	 * @return
	 * @throws HasNoPermissionException
	 * @throws StoreException
	 */
	public Privilege createPrivilege(String privname, String obj,
			List<IUserActions> operations, User createUser)
			throws HasNoPermissionException, StoreException {
		if (AccessControl.hasPermission(UserManagementActions.CREATEPRIV, null,
				createUser)) {
			if (!privStore.containsKey(privname)) {
				if (!privname.isEmpty() || privname == null) {
					Privilege priv = new Privilege(privname, obj, operations,
							this.privid++);
					privStore.put(priv.getPrivname(), priv);
					return priv;
				} else {
					Privilege priv = new Privilege(obj, operations,
							this.privid++);
					privStore.put(priv.getPrivname(), priv);
					return priv;
				}
			}
		} else {
			throw new HasNoPermissionException("User " + createUser.toString()
					+ " has no permission to create new privileges");
		}
		return null;
	}

	private List<IUserManagementListener> listeners = new CopyOnWriteArrayList<IUserManagementListener>();

	public void addTenantManagementListener(IUserManagementListener l) {
		listeners.add(l);
	}

	public void removeTenantManagementListener(IUserManagementListener l) {
		listeners.remove(l);
	}

	public void fireUserManagementListener() {
		for (IUserManagementListener l : listeners) {
			l.usersChangedEvent();
		}
	}

	public boolean isLoggedIn(String username) {
		return loggedIn.containsKey(username);
	}

	public int getRoleID() {
		return roleid;
	}

	public int getPrivID() {
		return privid;
	}

}
