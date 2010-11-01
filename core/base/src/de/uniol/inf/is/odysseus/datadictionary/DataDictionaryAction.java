package de.uniol.inf.is.odysseus.datadictionary;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserAction;

public enum DataDictionaryAction implements IUserAction {
	CLEAR, INSTANCE,

	ADD_ENTITY, GET_ENTITY, GET_ALL_ENTITY,

	ADD_SOURCETYPE, GET_SOURCETYPE, GET_SOURCE, GET_ALL_SOURCE,

	SET_VIEW, GET_VIEW, GET_VIEWS, REMOVE_VIEW, CLEAR_VIEWS, SET_LOGIC_VIEW, GET_LOGIC_VIEW, 
	REMOVE_LOGIC_VIEW, GET_VIEW_FOR_TRANSORM, GET_VIEW_REFERENCE, IS_LOGIC_VIEW, HAS_VIEW, 
	CONTAINS_VIEWS, GET_USER_FOR_VIEWS, GET_ALL_VIEWS,

	EMPTY_SOURCETYPE_MAP;

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

	public IUserAction hasSuperAction(DataDictionaryAction action) {
		switch (action) {
		case GET_ENTITY:
			return GET_ALL_ENTITY;
		case GET_SOURCE:
			return GET_ALL_SOURCE;
		case GET_VIEW:
			return GET_ALL_VIEWS;
		case GET_VIEWS:
			return GET_ALL_VIEWS;
		case GET_LOGIC_VIEW:
			return GET_ALL_VIEWS;
		case GET_VIEW_FOR_TRANSORM:
			return GET_ALL_VIEWS;
		case GET_VIEW_REFERENCE:
			return GET_ALL_VIEWS;
		default:
			return null;
		}
	}

	// TODO: F�llen
	public static boolean needsNoObject(IUserAction action) {
		return false;
	}
}