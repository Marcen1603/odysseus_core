package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class WaitForQueryCommand extends AbstractQueryCommand {

	private static final long serialVersionUID = 4983670569148411012L;

	private long testPeriod;
	private long maxWaitingTime;

	public WaitForQueryCommand(ISession caller, Resource queryName, long testPeriod, long maxWaitingTime) {
		super(caller, queryName);
		this.testPeriod = testPeriod;
		this.maxWaitingTime = maxWaitingTime;
	}

	@Override
	public synchronized void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		try {
			long start = System.currentTimeMillis();
			while ((executor.getQueryState(getQueryName(), getCaller()) != QueryState.INACTIVE
					&& executor.getQueryState(getQueryName(), getCaller()) != QueryState.UNDEF)
					&& !(maxWaitingTime > 0 && System.currentTimeMillis() > start + maxWaitingTime)) {
				this.wait(testPeriod);
			}
		} catch (Exception e) {

		}
	}

}
