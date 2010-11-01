package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;

public class AccessControl {

	static private AccessControl instance = null;

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
		// TODO Session Zeitstempel + aktuallisieren
		if (user != null && operation != null) {
			return hasOperationOnObject(operation, object, user);
		}
		return false;
	}

	/**
	 * returns true if username equals creator of the given objecturi
	 * 
	 * @param username
	 * @param objecturi
	 * @return
	 */
	public static boolean isCreatorOfObject(String username, String objecturi) {
		try {
			String user = DataDictionary.getInstance().getUserForEntity(
					objecturi);
			if (user.isEmpty()) {
				user = DataDictionary.getInstance().getUserForSource(objecturi);
			}
			if (!user.isEmpty()) {
				if (user.equals(username)) {
					return true;
				}
			}
		} catch (Exception e) {
			new RuntimeException(e);
		}
		return false;
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
