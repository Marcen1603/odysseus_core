package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class RunCommandCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -9216345132127032583L;
	private String command;

	public RunCommandCommand(String command, ISession caller){
		super(caller);
		this.command = command;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		executor.runCommand(command, getCaller());
	}

}
