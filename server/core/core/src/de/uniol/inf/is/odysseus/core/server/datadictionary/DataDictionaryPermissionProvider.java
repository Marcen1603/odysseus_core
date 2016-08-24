package de.uniol.inf.is.odysseus.core.server.datadictionary;

import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermissionProvider;

public class DataDictionaryPermissionProvider implements IPermissionProvider {

	@Override
	public String getName() {
		return DataDictionaryPermission.objectURI;
	}

	@Override
	public IPermission[] getPermissions() {
		return DataDictionaryPermission.values();
	}

}
