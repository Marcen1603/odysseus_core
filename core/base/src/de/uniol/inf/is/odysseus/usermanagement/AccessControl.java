package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;

/**
 * The Access Control manages the access to specified methods. All restricted
 * methods calling the method <b>hasPermission()</b>.
 * <ul>
 * <li>It checks if the specified user or role has the needed permission.</il>
 * <li>If the user do not have the needed permissions the Access Control checks
 * if the user has a role that has the needed permission.</li>
 * <li>Checks if the user is the owner of the accessed object.</li>
 * </ul>
 * 
 * The class checks the granted actions (permissions) from the action classes.
 * This actions are contained in the Privileges.
 * 
 * @see de.uniol.inf.is.odysseus.usermanagement.IUserAction.java
 * @see hasPermission
 * @see isCreatorOfObject
 * @author Christian van Göns
 * 
 */
public final class AccessControl {

	static private AccessControl instance = null;

	private static long sessionTimeout =  OdysseusDefaults.getLong("sessionTimeout",240 * 60000);
	
	private AccessControl() {
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
	public static boolean hasPermission(IUserAction operation, String object,
			User user) {
		if (user != null) {
			if (operation != null) {
				if (sessionTimestamp(user)) {
					return hasOperationOnObject(operation, object, user);
				}
			}
			return false;
		} else {
			throw new NullUserException("User is null.");
		}
	}

	/**
	 * checks if the user session is timed out
	 * 
	 * @param user
	 * @return boolean
	 */
	private static boolean sessionTimestamp(User user) {
		if (user.getSession() != null) {
			// Session Zeitstempel
			long dif = System.currentTimeMillis()
					- user.getSession().getTimestamp();
			if (dif < sessionTimeout) {
				// Session aktuallisieren
				user.getSession().updateTimestamp();
				return true;
			}
			throw new HasNoPermissionException("User " + user
					+ " session timeout. Login again ");
		}
		throw new HasNoPermissionException("User " + user
				+ " has no valid session ");
	}


	/**
	 * search for operation in user special privileges and user role privileges.
	 * returns true if given opererion found for the given object
	 * 
	 * @param operation
	 * @param objecturi
	 * @param user
	 * @return
	 */
	private static boolean hasOperationOnObject(IUserAction operation,
			String objecturi, User user) {
		// user special privs
		if (user.getPrivileges() != null && user.getPrivileges().size() > 0) {
			for (Privilege priv : user.getPrivileges()) {
				if (priv.getObject().equals(objecturi)) {
					if (priv.getOperations().contains(operation)) {
						return true;
					}
				}
			}
		}

		// user role privs
		for (Role role : user.getRoles()) {
			if (role.getPrivileges() != null && role.getPrivileges().size() > 0) {
				for (Privilege priv : role.getPrivileges()) {
					if (priv.getObject().equals(objecturi)) {
						if (priv.getOperations().contains(operation)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
