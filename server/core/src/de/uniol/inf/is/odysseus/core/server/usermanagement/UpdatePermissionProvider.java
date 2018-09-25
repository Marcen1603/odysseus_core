package de.uniol.inf.is.odysseus.core.server.usermanagement;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermissionProvider;

public class UpdatePermissionProvider implements IPermissionProvider {

	@Override
	public String getName() {
		return UpdatePermission.objectURI;
	}

	@Override
	public IPermission[] getPermissions() {
		return UpdatePermission.values();
	}

}
