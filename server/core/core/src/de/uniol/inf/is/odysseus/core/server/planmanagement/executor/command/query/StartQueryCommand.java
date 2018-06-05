package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class StartQueryCommand extends AbstractQueryCommand {

	private static final long serialVersionUID = -885993999524932571L;

	public StartQueryCommand(ISession caller, Resource queryName) {
		super(caller, queryName);
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		if (getQueryName() == null) {
			executor.startAllClosedQueries(getCaller());
		} else {
			executor.startQuery(getQueryName(), getCaller());
		}
	}

}
