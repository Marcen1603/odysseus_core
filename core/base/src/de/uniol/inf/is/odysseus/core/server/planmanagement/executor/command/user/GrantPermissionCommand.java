package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.PermissionFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class GrantPermissionCommand extends AbstractExecutorCommand {

	private String userName;
	private List<IPermission> operations;
	private List<String> objects;

	public GrantPermissionCommand(String userName,
			List<IPermission> operations, List<String> objects, ISession caller) {
		super(caller);
		this.userName = userName;
		this.operations = operations;
		this.objects = objects;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd,
			IUserManagementWritable um) {
		IUser user = um.findUser(userName, getCaller());
		if (user != null) {
			for (IPermission action : operations) {

				if (PermissionFactory.needsNoObject(action)) {
					um.grantPermission(user, action, null, getCaller());
				} else {
					for (String entityname : objects) {
						um.grantPermission(user, action, entityname, getCaller());
					}
				}

			}
		}
		IRole role = um.findRole(userName, getCaller());
		if (role != null) {
			for (IPermission action : operations) {

				if (PermissionFactory.needsNoObject(action)) {
					um.grantPermission(role, action, null, getCaller());
				} else {
					for (String entityname : objects) {
						um.grantPermission(role, action, entityname, getCaller());
					}
				}

			}
		}
		return getEmptyCollection();
	}

}
