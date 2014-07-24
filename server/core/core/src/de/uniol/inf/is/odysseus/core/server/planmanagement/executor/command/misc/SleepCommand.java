package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.misc;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class SleepCommand extends AbstractExecutorCommand {

	private long millis;

	public SleepCommand(long millis, ISession caller) {
		super(caller);
		this.millis = millis;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		try {
			Thread.sleep(millis, 0);
		} catch (InterruptedException e) {
		}
	}


}
