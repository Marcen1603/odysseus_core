package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermissionProvider;

public class ExecutorPermissionProvider implements IPermissionProvider {

	@Override
	public String getName() {
		return ExecutorPermission.objectURI;
	}

	@Override
	public IPermission[] getPermissions() {
		return ExecutorPermission.values();
	}

}
