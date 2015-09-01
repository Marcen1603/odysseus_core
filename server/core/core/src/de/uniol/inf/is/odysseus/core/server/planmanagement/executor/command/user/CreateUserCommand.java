package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class CreateUserCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 1185066985125334970L;
	
	private String username;
	private String password;

	public CreateUserCommand(String username, String password, ISession caller) {
		super(caller);
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		IUser user = um.createUser(username, getCaller());
		if (user == null)
			throw new QueryParseException("User cannot be created.");
		um.changePassword(user, password.getBytes(), getCaller());
		um.activateUser(user, getCaller());
	}

}
