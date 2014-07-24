package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateRoleCommand extends AbstractExecutorCommand {

	private String rolename;

	public CreateRoleCommand(String rolename, ISession caller) {
		super(caller);
		this.rolename = rolename;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		um.createRole(rolename, getCaller());
		return getEmptyCollection();
	}

}
