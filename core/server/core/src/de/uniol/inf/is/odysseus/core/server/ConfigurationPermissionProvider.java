package de.uniol.inf.is.odysseus.core.server;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermissionProvider;

public class ConfigurationPermissionProvider implements IPermissionProvider {

	@Override
	public String getName() {
		return ConfigurationPermission.objectURI;
	}

	@Override
	public IPermission[] getPermissions() {
		return ConfigurationPermission.values();
	}

}
