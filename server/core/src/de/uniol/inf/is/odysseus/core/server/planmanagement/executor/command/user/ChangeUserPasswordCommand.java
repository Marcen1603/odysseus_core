package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class ChangeUserPasswordCommand extends AbstractExecutorCommand {

	private String username;
	private String password;

	public ChangeUserPasswordCommand(String username, String password,
			ISession caller) {
		super(caller);
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd,
			IUserManagementWritable um) {
		IUser user = um.findUser(username, getCaller());
		if (user != null) {
			um.changePassword(user, password.getBytes(), getCaller());
		}
		return getEmptyCollection();
	}

}
