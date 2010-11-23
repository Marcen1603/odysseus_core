package de.uniol.inf.is.odysseus.datadictionary;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserAction;

/**
 * This class containing the actions (permissions) for the DataDictionary.
 * It encapsulate the following permissions:
 * <ul>
 * <li>ADD_ENTIY - permission to add a new entity</li>
 * <li>GET_ENTITY - permission to get a entity specified by the object uri in the privilege</li>
 * <li>REMOVE_ENTITY - permission to remove an existing entity specified by the object uri in the privilege</li>
 * <li>ADD_SOURCETYPE - permission to add a new source type</li>
 * <li>ADD_STREAM - permission to add a new stream</li>
 * <li>REMOVE_STREAM - permission to remove an existing stream specified by the object uri in the privilege</li>
 * <li>ADD_VIEW - permission to add a new view for a existing stream or entity</li>
 * <li>REMOVE_VIEW - permission to remove an existing view specified by the object uri in the privilege</li>
 * <li>READ - permission to read/get a stream or view specified by the object uri in the privilege</li>
 * <li>GET_ALL - super permission to get all get-able things (get_entity, read) from DataDictionary without having permissions on the specified object uri</li>
 * <li>REMOVE_ALL - super permission to remove all removable things from DataDictionary without having permissions on the specified object uri</li>
 * </ul>
 * @see de.uniol.inf.is.odysseus.usermanagement.Privilege.java
 * @see de.uniol.inf.is.odysseus.datadictionary.DataDictionary.java
 * @author Christian van Göns
 */
public enum DataDictionaryAction implements IUserAction {
	ADD_ENTITY, GET_ENTITY, REMOVE_ENTITY,

	ADD_SOURCETYPE, 

	ADD_STREAM, REMOVE_STREAM, REMOVE_VIEW, ADD_VIEW, READ,

	GET_ALL, REMOVE_ALL;

	static List<IUserAction> all;
	public static final String alias = "DataDictionary";

	/**
	 * returns a list with all actions of this action class.
	 * 
	 * @return List<IUserAction>
	 */
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

	/**
	 * returns the higher action (permission) for a given action.
	 * 
	 * @param action
	 * @return IUserAction
	 */
	public static IUserAction hasSuperAction(DataDictionaryAction action) {
		switch (action) {
		case GET_ENTITY:
			return GET_ALL;
		case READ:
			return GET_ALL;
		case REMOVE_VIEW:
			return REMOVE_ALL;
		case REMOVE_ENTITY:
			return REMOVE_ALL;
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
		switch ((DataDictionaryAction) action) {
		case ADD_ENTITY:
			return true;
		case ADD_STREAM:
			return true;
		case ADD_VIEW:
			return true;
		default:
			return false;
		}
	}
}