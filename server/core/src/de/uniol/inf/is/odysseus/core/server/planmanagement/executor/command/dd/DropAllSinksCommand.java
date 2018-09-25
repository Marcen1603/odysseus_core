package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropAllSinksCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -3096747328228230340L;

	public DropAllSinksCommand(ISession caller) {
		super(caller);
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		List<SinkInformation> sinks = executor.getSinks(getCaller());
		for (SinkInformation si:sinks){
			executor.removeSink(si.getName(), getCaller());
		}
	}



}
