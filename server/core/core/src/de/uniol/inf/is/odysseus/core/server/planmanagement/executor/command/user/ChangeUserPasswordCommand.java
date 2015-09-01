package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class ChangeUserPasswordCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 3195756655622930842L;
	
	private String username;
	private String password;

	public ChangeUserPasswordCommand(String username, String password,
			ISession caller) {
		super(caller);
		this.username = username;
		this.password = password;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		IUser user = um.findUser(username, getCaller());
		if (user != null) {
			um.changePassword(user, password.getBytes(), getCaller());
		}
	}

}
