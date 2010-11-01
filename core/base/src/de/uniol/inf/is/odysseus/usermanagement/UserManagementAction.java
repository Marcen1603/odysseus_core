package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

public enum UserManagementAction implements IUserAction {
	// TODO kommentieren
	INSTANCE, GRANT, GRANT_ALL, REVOKE, REVOKE_ALL, CREATE_USER, DELETE_USER, GET_USER, GET_ALL_USER, UPDATE_USER_PASSWORD, CREATE_ROLE, CREATE_PRIV, LOGOUT, CLEAR_USERSTORE, FIND_USER, IS_LOGGEDIN, ADD_LISTENER, REMOVE_LISTENER, DELETE_ROLE, CREATE_ALL;

	static List<IUserAction> all;

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
		case GET_USER:
			return GET_ALL_USER;
		case GRANT:
			return GRANT_ALL;
		case REVOKE:
			return REVOKE_ALL;
		default:
			return null;
		}
	}

}