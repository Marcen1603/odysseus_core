package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class DropUserCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -7974727723727945330L;
	
	private String userName;

	public DropUserCommand(String userName, ISession caller) {
		super(caller);
		this.userName = userName;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		IUser user = um.findUser(userName, getCaller());
		um.deleteUser(user, getCaller());
	}

}
