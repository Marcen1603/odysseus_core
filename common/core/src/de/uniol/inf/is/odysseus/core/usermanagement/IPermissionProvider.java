package de.uniol.inf.is.odysseus.core.usermanagement;

public interface IPermissionProvider {

	String getName();
	IPermission[] getPermissions();
	
}
