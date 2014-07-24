package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class GetQueryCommand extends AbstractExecutorCommand {

	private int queryID;

	public GetQueryCommand(int queryID, ISession caller) {
		super(caller);
		this.queryID = queryID;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		throw new RuntimeException("This method cannot be called! Use Executor to execute instead!");
		// return null;
	}

	public int getQueryID() {
		return queryID;
	}

}
