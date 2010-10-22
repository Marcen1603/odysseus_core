package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.List;

public enum UserManagementActions implements IUserActions {
	INSTANCE, GRANT, REVOKE, CREATE_USER, DELETE_USER, GET_USER, GET_ALL_USER, UPDATE_USER_PASSWORD, CREATE_ROLE, CREATE_PRIV, LOGOUT, CLEAR_USERSTORE, FIND_USER, IS_LOGGEDIN, ADD_LISTENER, REMOVE_LISTENER, DELETE_ROLE;

	public static List<IUserActions> getAll() {
		List<IUserActions> list = new ArrayList<IUserActions>();
		for (IUserActions action : UserManagementActions.class
				.getEnumConstants()) {
			list.add(action);
		}
		return list;
	}

}