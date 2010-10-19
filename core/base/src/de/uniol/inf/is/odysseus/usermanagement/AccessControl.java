package de.uniol.inf.is.odysseus.usermanagement;

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
	public static boolean hasPermission(IUserActions operation, String object,
			User user) {
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
	private static boolean hasOperationOnObject(IUserActions operation,
			String obj, User user) {
		// user special privs
		if (user.getPrivileges() != null && user.getPrivileges().size() > 0) {
			for (Privilege priv : user.getPrivileges()) {
				if (obj == null) {
					if (priv.getObject() == null) {
						if (priv.getOperations().contains(operation)) {
							return true;
						}
					}
				} else if (priv.getObject().equals(obj)) {
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
					if (obj == null) {
						if (priv.getObject() == null) {
							if (priv.getOperations().contains(operation)) {
								return true;
							}
						}
					} else if (priv.getObject().equals(obj)) {
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
