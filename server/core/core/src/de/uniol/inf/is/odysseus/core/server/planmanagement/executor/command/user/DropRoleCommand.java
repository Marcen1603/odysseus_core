package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropRoleCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -2680536306987144307L;
	
	private String rolename;

	public DropRoleCommand(String rolename, ISession caller) {
		super(caller);
		this.rolename = rolename;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		IRole role = um.findRole(rolename, getCaller());
		if (role != null) {
			um.deleteRole(role, getCaller());
		}
	}

}
