package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class RevokeRoleCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -1687779312244535476L;
	
	private String userName;
	private List<String> roles;

	public RevokeRoleCommand(String userName, List<String> roles,
			ISession caller) {
		super(caller);
		this.userName = userName;
		this.roles = roles;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		IUser user = um.findUser(userName, getCaller());
		for (String rolename : roles) {
			IRole role = um.getRole(rolename, getCaller());
			um.revokeRole(user, role, getCaller());
		}
	}

}
