package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.scheduler;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SetSchedulerCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -8776699006878697681L;
	final private String strategy;
	final private String scheduler;

	public SetSchedulerCommand(String scheduler, String strategy, ISession caller) {
		super(caller);
		this.scheduler = scheduler;
		this.strategy = strategy;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		executor.setScheduler(scheduler, strategy, getCaller());
	}

}
