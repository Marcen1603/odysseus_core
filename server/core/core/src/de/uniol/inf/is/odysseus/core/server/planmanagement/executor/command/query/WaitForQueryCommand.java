package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class WaitForQueryCommand extends AbstractExecutorCommand {

	private String queryName;
	private long testPeriod;

	public WaitForQueryCommand(ISession caller, String queryName, long testPeriod) {
		super(caller);
		this.queryName = queryName;
		this.testPeriod = testPeriod;
	}

	@Override
	public synchronized void execute(IDataDictionaryWritable dd, IUserManagementWritable um,
			IServerExecutor executor) {
		try {
			while (executor.getQueryState(queryName) != QueryState.INACTIVE && executor.getQueryState(queryName) != QueryState.UNDEF) {
				this.wait(testPeriod);
			}
		} catch (Exception e) {

		}
	}

}
