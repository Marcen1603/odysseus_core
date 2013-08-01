package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateQueryCommand extends AbstractExecutorCommand {

	final private ILogicalQuery query;

	public CreateQueryCommand(ILogicalQuery query, ISession caller) {
		super(caller);
		this.query = query;
	}
	
	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd) {
		throw new RuntimeException("This method cannot be called! Use Executor to execute instead!");
		//return null;
	}
	
	public ILogicalQuery getQuery() {
		return query;
	}
	

}
