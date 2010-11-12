package de.uniol.inf.is.odysseus.datadictionary;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserAction;

public enum DataDictionaryAction implements IUserAction {
	ADD_ENTITY, GET_ENTITY, REMOVE_ENTITY,

	ADD_SOURCETYPE, GET_SOURCE, REMOVE_SOURCE,

	ADD_VIEW, GET_VIEW, REMOVE_VIEW, ADD_LOGICAL_VIEW, GET_VIEW_FOR_TRANFORMATION,

	GET_ALL, REMOVE_ALL;

	static List<IUserAction> all;
	public static final String alias = "DataDictionary";

	public synchronized static List<IUserAction> getAll() {
		if (all == null) {
			all = new ArrayList<IUserAction>();
			for (IUserAction action : DataDictionaryAction.class
					.getEnumConstants()) {
				all.add(action);
			}

		}
		return all;
	}

	public static IUserAction hasSuperAction(DataDictionaryAction action) {
		switch (action) {
		case GET_ENTITY:
			return GET_ALL;
		case GET_SOURCE:
			return GET_ALL;
		case GET_VIEW:
			return GET_ALL;
		case GET_VIEW_FOR_TRANFORMATION:
			return GET_ALL;
		case REMOVE_VIEW:
			return REMOVE_ALL;
		case REMOVE_ENTITY:
			return REMOVE_ALL;
		case REMOVE_SOURCE:
			return REMOVE_ALL;
		default:
			return null;
		}
	}

	public static boolean needsNoObject(IUserAction action) {
		switch ((DataDictionaryAction) action) {
		case ADD_ENTITY:
			return true;
		case ADD_VIEW:
			return true;
		case ADD_LOGICAL_VIEW:
			return true;
		default:
			return false;
		}
	}
}