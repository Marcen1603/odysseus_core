package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This command is typically created from a parser that creates a logical query 
 * 
 * @author Marco Grawunder
 *
 */

public class CreateQueryCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -6389713977544787145L;
	
	final private ILogicalQuery query;

	public CreateQueryCommand(ILogicalQuery query, ISession caller) {
		super(caller);
		this.query = query;
	}
	
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		throw new RuntimeException("This method cannot be called! Use Executor to execute instead!");
		//return null;
	}
	
	public ILogicalQuery getQuery() {
		return query;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CREATE QUERY "+query;
	}
	

}
