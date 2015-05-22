package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class StartQueryCommand extends AbstractExecutorCommand {

	private String queryName;

	public StartQueryCommand(ISession caller, String queryName) {
		super(caller);
		this.queryName = queryName;
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um,
			IServerExecutor executor) {
		if (queryName == null){
			executor.startAllClosedQueries(getCaller());
		}else{
			executor.startQuery(queryName, getCaller());
		}
	}

}
