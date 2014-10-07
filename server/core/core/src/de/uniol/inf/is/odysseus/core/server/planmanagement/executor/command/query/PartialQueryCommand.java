package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PartialQueryCommand extends AbstractExecutorCommand {

	private String queryName;
	private int factor;

	public PartialQueryCommand(ISession caller, String queryName, int factor) {
		super(caller);
		this.queryName = queryName;
		this.factor = factor;
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um,
			IServerExecutor executor) {
		executor.partialQuery(queryName, factor, getCaller());
	}

}
