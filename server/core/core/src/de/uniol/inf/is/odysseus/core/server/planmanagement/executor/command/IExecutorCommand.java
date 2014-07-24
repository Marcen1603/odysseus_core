package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IExecutorCommand {

	Collection<Integer> execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor);
	ISession getCaller();

}
