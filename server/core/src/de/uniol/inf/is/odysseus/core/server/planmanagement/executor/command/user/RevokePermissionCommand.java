package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.PermissionFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class RevokePermissionCommand extends AbstractPermissionExecutorCommand {

	private static final long serialVersionUID = -4520504360577308790L;
	
	private String userName;
	private List<String> rights;
	private List<String> objects;

	public RevokePermissionCommand(String userName,
			List<String> operations, List<String> objects, ISession caller) {
		super(caller);
		this.userName = userName;
		this.rights = operations;
		this.objects = objects;
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {

		List<IPermission> operations = determinePermissions(rights);
		
		revokeFromUser(um, operations);

		removeFromRole(um, operations);
	
	}

	private void removeFromRole(IUserManagementWritable um, List<IPermission> operations) {
		IRole role = um.findRole(userName, getCaller());
		if (role != null) {
			for (IPermission action : operations) {

				if (PermissionFactory.needsNoObject(action)) {
					um.revokePermission(role, action, null, getCaller());
				} else {
					for (String entityname : objects) {
						um.revokePermission(role, action, entityname, getCaller());
					}
				}

			}
		}
	}

	private void revokeFromUser(IUserManagementWritable um, List<IPermission> operations) {
		IUser user = um.findUser(userName, getCaller());
		if (user != null) {
			for (IPermission action : operations) {

				if (PermissionFactory.needsNoObject(action)) {
					um.revokePermission(user, action, null, getCaller());
				} else {
					for (String entityname : objects) {
						um.revokePermission(user, action, entityname, getCaller());
					}
				}

			}
		}
	}

}
