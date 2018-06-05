package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PartialQueryCommand extends AbstractQueryCommand {

	private static final long serialVersionUID = -6197401518614427761L;

	private int factor;

	public PartialQueryCommand(ISession caller, Resource queryName, int factor) {
		super(caller, queryName);
		this.factor = factor;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		executor.partialQuery(getQueryName(), factor, getCaller());
	}

}
