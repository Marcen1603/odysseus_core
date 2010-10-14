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

	private AccessControl() {
		roleStore = new MemoryStore<String, Role>();
		privStore = new MemoryStore<String, Privilege>();
	}

	public synchronized static AccessControl getInstance() {
		if (instance == null) {
			instance = new AccessControl();
		}
		return instance;
	}

	/**
	 * return true if the specified user contains the enum of the operation he
	 * trys to execute or is admin
	 * 
	 * @param operation
	 * @param user
	 * @return boolean
	 */
	public static boolean hasPermission(Enum operation, Object object, User user) {
		if (user != null) {
			if (user.isAdmin()) {
				return true;
			}

			else if (operation != null && object != null) {

				// search in user special privileges with object
				searchPrivileges(user.getPrivileges(), operation, object);

				// search in user role privileges with object
				for (int i = 0; i < user.getRoles().size(); i++) {
					Role role = user.getRoles().get(i);
					searchPrivileges(role.getPrivileges(), operation, object);
				}
			}
		}
		return false;
	}

	private static boolean searchPrivileges(List<Privilege> privs,
			Enum operation, Object object) {
		for (int j = 0; j < privs.size(); j++) {
			Privilege priv = privs.get(j);
			if (priv.getObject().equals(object)) {

				if (priv.getOperations().contains(operation)) {
					return true;
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
	public void grantPermissionToUser(User grantUser, User user, Privilege priv)
			throws HasNoPermissionException, StoreException {
		if (grantUser.isAdmin()) {
			try {
				// user.addPrivilege(priv);
				user.addPrivilege(privStore.get(priv.toString()));
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
		Privilege priv = new Privilege(obj, operations);
		grantPermissionToUser(grantUser, user, priv);
	}

	/**
	 * grants permission to a role. checks if grantUser has permission
	 * 
	 * @param grantUser
	 * @param role
	 * @param priv
	 * @throws HasNoPermissionException
	 */
	public void grantPermissionToRole(User grantUser, Role role, Privilege priv)
			throws HasNoPermissionException, StoreException {
		if (grantUser.isAdmin()) {
			try {
				// role.addPrivileges(priv);
				role.addPrivilege(privStore.get(priv.toString()));
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
			Privilege priv = new Privilege(obj, operations);
			privStore.put(priv.getPrivname(), priv);
			grantPermissionToRole(grantUser, role, priv);
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
	public void revokePermissionFromUser(User revokeUser, User user,
			Privilege priv) throws HasNoPermissionException {
		if (revokeUser.isAdmin()) {
			user.removePrivilege(priv);
			// TODO aus store entfernen ? was ist mit dem priv in anderen rollen
			// oder usern ?
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
	public void revokePermissionFromRole(User revokeUser, Role role,
			Privilege priv) throws HasNoPermissionException {
		if (revokeUser.isAdmin()) {
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
		if (grantUser.isAdmin()) {
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
		if (revokeUser.isAdmin()) {
			user.removeRole(role);
			fireUserManagementListener();
		} else {
			throw new HasNoPermissionException(revokeUser.toString());
		}
	}

	// public void grantRoleToRole(User grantUser, Role role, Role subrole)
	// throws HasNoPermissionException {
	// if (hasPermission(AccessOperationEnum.GRANT, grantUser)) {
	// role.addSubrole(subrole);
	// } else {
	// throw new HasNoPermissionException(AccessOperationEnum.GRANT,
	// grantUser.toString());
	// }
	// }

	/**
	 * calls UserManagement to create a User
	 */
	public void createUser(User createUser, String username, String passwort)
			throws UsernameAlreadyUsedException, UserStoreException,
			HasNoPermissionException {
		if (createUser.isAdmin()) {
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
		if (delUser.isAdmin()) {
			//
		} else {
			throw new HasNoPermissionException(delUser.toString());

		}
	}

	/**
	 * calls the UserManagement to change user into admin
	 * 
	 * @param grantUser
	 * @param user
	 * @throws HasNoPermissionException
	 * @throws UsernameNotExistException
	 * @throws UserStoreException
	 */
	public void grantAdminPrivileges(User grantUser, User user)
			throws HasNoPermissionException, UsernameNotExistException,
			UserStoreException {
		if (grantUser.isAdmin()) {
			if (!user.isAdmin()) {
				UserManagement.getInstance().updateUserAdmin(grantUser,
						user.getUsername());
			}
		} else {
			throw new HasNoPermissionException(grantUser.toString());
		}
	}

	/**
	 * calls UserManagement to change Admin into User
	 * 
	 * @param grantUser
	 * @param user
	 * @throws HasNoPermissionException
	 * @throws UsernameNotExistException
	 * @throws UserStoreException
	 */
	public void revokeAdminPrivileges(User grantUser, User user)
			throws HasNoPermissionException, UsernameNotExistException,
			UserStoreException {
		if (grantUser.isAdmin()) {
			if (user.isAdmin()) {
				UserManagement.getInstance().updateAdminUser(grantUser,
						user.getUsername());
			}
		} else {
			throw new HasNoPermissionException(grantUser.toString());
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
		if (hasPermission(null, null, createUser)) {
			if (!roleStore.containsKey(rolename)) {
				Role role = new Role(rolename, privileges);
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
		if (hasPermission(null, null, createUser)) {
			if (!privStore.containsKey(privname)) {
				Privilege priv = new Privilege(privname, obj, operations);
				privStore.put(privname, priv);
				return priv;
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
