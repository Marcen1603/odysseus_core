package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserAction;

public enum ExecutorAction implements IUserAction {
	ADD_QUERY, START_QUERY, STOP_QUERY, REMOVE_QUERY,
	START_ALL_QUERIES, STOP_ALL_QUERIES, REMOVE_ALL_QUERIES;
	
	
	static List<IUserAction> all;

	public static String alias = "Executor";
	
	public synchronized static List<IUserAction> getAll() {
		if (all == null) {
			all = new ArrayList<IUserAction>();
			for (IUserAction action : ExecutorAction.class
					.getEnumConstants()) {
				all.add(action);
			}

		}
		return all;
	}
	
	public static IUserAction hasSuperAction(ExecutorAction action) {
		switch (action) {
		case START_QUERY:
			return START_ALL_QUERIES;
		case STOP_QUERY:
			return STOP_ALL_QUERIES;
		case REMOVE_QUERY:
			return REMOVE_ALL_QUERIES;
		default:
			return null;
		}
	}
}
