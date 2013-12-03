package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropRoleCommand extends AbstractExecutorCommand {

	private String rolename;

	public DropRoleCommand(String rolename, ISession caller) {
		super(caller);
		this.rolename = rolename;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd,
			IUserManagementWritable um) {
		IRole role = um.findRole(rolename, getCaller());
		if (role != null) {
			um.deleteRole(role, getCaller());
		}
		return getEmptyCollection();
	}

}
