package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;

public interface IExecutorCommand {

	Collection<Integer> execute(IDataDictionaryWritable dd, IUserManagementWritable um);

}
