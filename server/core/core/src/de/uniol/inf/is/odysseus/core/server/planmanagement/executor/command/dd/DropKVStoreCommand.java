package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropKVStoreCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 9178099993534874528L;
	private String name;

	public DropKVStoreCommand(String name, ISession caller) {
		super(caller);
		this.name = name;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		if (!dd.containsStore(name, getCaller())){
			throw new QueryParseException("Store with name "+name+" does not exist.");
		}
		dd.removeStore(name, getCaller());
	}

}
