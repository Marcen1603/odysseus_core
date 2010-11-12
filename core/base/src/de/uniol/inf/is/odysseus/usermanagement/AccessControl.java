package de.uniol.inf.is.odysseus.usermanagement;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;

public final class AccessControl {

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
			// Session jünger als 4 Std. (in Minuten)
			if (dif < OdysseusDefaults.sessionTimeout) {
				// Session aktuallisieren
				user.getSession().setTimestamp();
				return true;
			}
			throw new HasNoPermissionException("User "+user+" session timeout. Login again ");			
		}
		throw new HasNoPermissionException("User "+user+" has no valid session ");
	}

	/**
	 * returns true if username equals creator of the given objecturi
	 * 
	 * @param username
	 * @param objecturi
	 * @return
	 */
	public static boolean isCreatorOfObject(String username, String objecturi) {
		if (!username.isEmpty()) {
			try {
				String user = DataDictionary.getInstance().getUserForEntity(
						objecturi);
				if (user.isEmpty()) {
					user = DataDictionary.getInstance().getUserForSource(
							objecturi);
				}
				if (user.isEmpty()) {
					user = DataDictionary.getInstance()
							.getUserForView(objecturi).getUsername();
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
		} else {
			throw new NullUserException("Username is empty.");
		}
	}

	/**
	 * returns true if username euqlas creator of the given view
	 * 
	 * @param username
	 * @param viewname
	 * @return
	 */
	public static boolean isCreatorOfView(String username, String viewname) {
		if (!username.isEmpty()) {
			try {
				String user = DataDictionary.getInstance()
						.getUserForView(viewname).getUsername();
				if (!user.isEmpty()) {
					if (user.equals(username)) {
						return true;
					}
				}
			} catch (Exception e) {
				new RuntimeException(e);
			}
			return false;
		} else {
			throw new NullUserException("Username is empty.");
		}
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
