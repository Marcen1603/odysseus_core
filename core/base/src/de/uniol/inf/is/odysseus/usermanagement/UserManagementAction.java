package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

public enum UserManagementAction implements IUserAction {
	CREATE_USER, ALTER_USER, DELETE_USER, DEACTIVATE_USER,

	CREATE_ROLE, DELETE_ROLE,

	GRANT, GRANT_ALL, REVOKE, REVOKE_ALL, GRANT_ROLE, REVOKE_ROLE,

	LOGOUT, GET_ALL_USER, GET_ALL;

	static List<IUserAction> all;
	static final String alias = "UserManagement";

	/**
	 * returns a list with all actions of this action class.
	 * 
	 * @return List<IUserAction>
	 */
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

	/**
	 * returns the higher action (permission) for a given action.
	 * 
	 * @param action
	 * @return IUserAction
	 */
	public synchronized static IUserAction hasSuperAction(
			UserManagementAction action) {
		switch (action) {
		case GET_ALL_USER:
			return GET_ALL;
		case GRANT:
		case GRANT_ROLE:
			return GRANT_ALL;
		case REVOKE:
		case REVOKE_ROLE:
			return REVOKE_ALL;
		default:
			return null;
		}
	}

	/**
	 * returns whether the given action (permission) operates with an objecturi
	 * or the action class alias.
	 * 
	 * @param action
	 * @return
	 */
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