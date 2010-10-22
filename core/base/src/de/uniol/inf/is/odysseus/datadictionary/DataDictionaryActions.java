package de.uniol.inf.is.odysseus.datadictionary;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserActions;

public enum DataDictionaryActions implements IUserActions {

	CLEAR, INSTANCE,

	ADD_ENTITY, GET_ENTITY,

	ADD_SOURCETYPE, GET_SOURCETYPE, GET_SOURCE,

	SET_VIEW, GET_VIEW, GET_VIEWS, REMOVE_VIEW, CLEAR_VIEWS, SET_LOGIC_VIEW, GET_LOGIC_VIEW, REMOVE_LOGIC_VIEW, GET_VIEW_FOR_TRANSORM, GET_VIEW_REFERENCE, IS_LOGIC_VIEW, HAS_VIEW, CONTAINS_VIEWS, GET_USER_FOR_VIEWS,

	EMPTY_SOURCETYPE_MAP;

	public static List<IUserActions> getAll() {
		List<IUserActions> list = new ArrayList<IUserActions>();
		for (IUserActions action : DataDictionaryActions.class
				.getEnumConstants()) {
			list.add(action);
		}
		return list;
	}
}