package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermissionProvider;

public class UserManagementPermissionProvider implements IPermissionProvider {

	@Override
	public String getName() {
		return UserManagementPermission.objectURI;
	}

	@Override
	public IPermission[] getPermissions() {
		return UserManagementPermission.values();
	}

}
