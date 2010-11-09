package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

public enum UserManagementAction implements IUserAction {
	CREATE_USER, ALTER_USER, DELETE_USER,

	CREATE_ROLE, DELETE_ROLE,

	GRANT, GRANT_ALL, REVOKE, REVOKE_ALL, GRANT_ROLE, REVOKE_ROLE,

	LOGOUT, GET_ALL_USER, GET_ALL;

	static List<IUserAction> all;
	static final String alias = "UserManagement";

	public synchronized static List<IUserAction> getAll() {
		if (all == null) {
			all = new ArrayList<IUserAction>();
			for (IUserAction action : UserManagementAction.class
					.getEnumConstants()) {
				all.add(action);
			}

		}
		return all;
	}

	public synchronized static IUserAction hasSuperAction(
			UserManagementAction action) {
		switch (action) {
		case GET_ALL_USER:
			return GET_ALL;
		case GRANT:
			return GRANT_ALL;
		case REVOKE:
			return REVOKE_ALL;
		default:
			return null;
		}
	}

	public static boolean needsNoObject(IUserAction action) {
		switch ((UserManagementAction) action) {
		case CREATE_USER:
			return true;
		case CREATE_ROLE:
			return true;
		case ALTER_USER:
			return true;
		case DELETE_USER:
			return true;
		case DELETE_ROLE:
			return true;
		case GRANT_ALL:
			return true;
		case GRANT_ROLE:
			return true;
		case REVOKE_ALL:
			return true;
		case REVOKE_ROLE:
			return true;
		case GET_ALL_USER:
			return true;
		case GET_ALL:
			return true;
		case LOGOUT:
			return true;
		default:
			return false;
		}
	}

}