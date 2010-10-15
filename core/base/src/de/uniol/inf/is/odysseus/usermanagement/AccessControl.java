package de.uniol.inf.is.odysseus.usermanagement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;

public class AccessControl {

	final private IStore<String, Role> roleStore;
	final private IStore<String, Privilege> privStore;
	static private AccessControl instance = null;
	private int roleid = 0;
	private int privid = 0;

	private AccessControl() {
		roleStore = new MemoryStore<String, Role>();
		privStore = new MemoryStore<String, Privilege>();
		this.roleid = 1;
		this.privid = 1;
	}

	public synchronized static AccessControl getInstance() {
		if (instance == null) {
			instance = new AccessControl();
		}
		return instance;
	}

	/**
	 * return true if the specified user contains the enum of the operation he
	 * trys to execute
	 * 
	 * @param operation
	 * @param user
	 * @return boolean
	 */
	public static boolean hasPermission(Enum operation, Object object, User user) {
		if (user != null && operation != null) {
			return hasOperationOnObject(operation, object, user);
		}
		return false;
	}

	/**
	 * search for operation in user special privileges and user role privileges.
	 * returns true if given opererion found for the given object
	 * 
	 * @param operation
	 * @param obj
	 * @param user
	 * @return
	 */
	private static boolean hasOperationOnObject(Enum operation, Object obj,
			User user) {
		// user special privs
		for (Privilege priv : user.getPrivileges()) {
			if (priv.getObject().equals(obj)) {
				if (priv.getOperations().contains(operation)) {
					return true;
				}
			}
		}

		// user role privs
		for (Role role : user.getRoles()) {
			for (Privilege priv : role.getPrivileges()) {
				if (priv.getObject().equals(obj)) {
					if (priv.getOperations().contains(operation)) {
						return true;
					}
				}
			}
		}
		return false;
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
		if (hasPermission(AccessOperationEnum.GRANT, null, grantUser)
				&& user.hasPrivilege(priv) == null) {
			try {
				// user.addPrivilege(priv);
				user.addPrivilege(privStore.get(priv.getPrivname()));
				fireUserManagementListener();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException(grantUser.toString());
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
	public void grantPermissionToUser(User grantUser, User user, Object obj,
			List<Enum> operations) throws HasNoPermissionException,
			StoreException {
		if (user.hasObject(obj) == null) {
			grantPrivilegeToUser(grantUser, user,
					createPrivilege(null, obj, operations, grantUser));
		}
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
		if (hasPermission(AccessOperationEnum.GRANT, null, grantUser)) {
			try {
				// role.addPrivileges(priv);
				role.addPrivilege(privStore.get(priv.getPrivname()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException(grantUser.toString());
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
	public void grantPermissionToRole(User grantUser, Role role, Object obj,
			List<Enum> operations) throws HasNoPermissionException,
			StoreException {
		try {
			grantPrivilegeToRole(grantUser, role,
					createPrivilege(null, obj, operations, grantUser));
		} catch (Exception e) {
			e.printStackTrace();
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
		if (hasPermission(AccessOperationEnum.REVOKE, null, revokeUser)) {
			if (user.hasPrivilege(priv) != null) {
				user.removePrivilege(priv);
			}
			Role role = user.hasPrivilegeInRole(priv);
			if (role != null) {
				user.removeRole(role);
			}
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException(revokeUser.toString());
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
		if (hasPermission(AccessOperationEnum.REVOKE, null, revokeUser)) {
			role.removePrivilege(priv);
		} else {
			throw new HasNoPermissionException(revokeUser.toString());
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
		if (hasPermission(AccessOperationEnum.GRANT, null, grantUser)
				&& user.hasRole(role) == null) {
			try {
				user.addRole(roleStore.get(role.getRolename()));
				fireUserManagementListener();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new HasNoPermissionException(grantUser.toString());
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
		if (hasPermission(AccessOperationEnum.REVOKE, null, revokeUser)
				&& user.hasRole(role) != null) {
			user.removeRole(role);
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException(revokeUser.toString());
		}
	}

	/**
	 * calls UserManagement to create a User
	 * 
	 * @param createUser
	 * @param username
	 * @param passwort
	 * @throws UsernameAlreadyUsedException
	 * @throws UserStoreException
	 * @throws HasNoPermissionException
	 */
	public void createUser(User createUser, String username, String passwort)
			throws UsernameAlreadyUsedException, UserStoreException,
			HasNoPermissionException {
		if (hasPermission(AccessOperationEnum.CREATEUSER, null, createUser)) {
			UserManagement.getInstance().registerUserInt(username, passwort);
		} else {
			throw new HasNoPermissionException(createUser.toString());

		}
	}

	/**
	 * TBD: not jet implemented
	 * 
	 * @throws HasNoPermissionException
	 */
	public void deleteUser(User delUser, String username)
			throws HasNoPermissionException {
		if (hasPermission(AccessOperationEnum.DELETEUSER, null, delUser)) {
			// TODO del user
		} else {
			throw new HasNoPermissionException(delUser.toString());

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
		if (hasPermission(AccessOperationEnum.CREATEROLE, null, createUser)) {
			// wenn role noch nicht in store
			if (!roleStore.containsKey(rolename)) {
				Role role = new Role(rolename, privileges, this.roleid++);
				roleStore.put(rolename, role);
				return role;
			}
		} else {
			throw new HasNoPermissionException(createUser.toString());
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
	public Privilege createPrivilege(String privname, Object obj,
			List<Enum> operations, User createUser)
			throws HasNoPermissionException, StoreException {
		if (hasPermission(AccessOperationEnum.CREATEPRIV, null, createUser)) {
			// wenn privilege noch nicht in store
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
			throw new HasNoPermissionException(createUser.toString());
		}
		// nachschauen ob name schon im store vorhanden etc.
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

}
