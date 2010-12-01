package de.uniol.inf.is.odysseus;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserAction;

public enum ConfigurationAction implements IUserAction {
	SET_PARAM, SAVE_PARAM;
	
	static List<IUserAction> all;
	public static final String alias = "Configuration";
	
	public synchronized static List<IUserAction> getAll() {
		if (all == null) {
			all = new ArrayList<IUserAction>();
			for (IUserAction action : ConfigurationAction.class
					.getEnumConstants()) {
				all.add(action);
			}

		}
		return all;
	}

}
