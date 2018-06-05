package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;

public enum UpdatePermission implements IPermission {
	UPDATE, INSTALL, REMOVE, LIST, CONFIGURE;	

	public final static String objectURI = "updating";

	public static boolean needsNoObject(IPermission action) {
		return true;
	}
	
}