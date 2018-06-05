package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class GrantRoleCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -8161792315196569321L;
	
	private String userName;
	private List<String> roles;

	public GrantRoleCommand(String userName, List<String> roles, ISession caller) {
		super(caller);
		this.userName = userName;
		this.roles = roles;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		IUser user = um.findUser(userName, getCaller());
		if (user != null) {
			for (String rolename : roles) {
				IRole role = um.getRole(rolename, getCaller());
				if (role != null) {
					um.grantRole(user, role, getCaller());
				} else {
					throw new QueryParseException("Role " + rolename
							+ " not defined!");
				}
			}
		} else {
			throw new QueryParseException("User " + userName + " not found");
		}
	}

}
