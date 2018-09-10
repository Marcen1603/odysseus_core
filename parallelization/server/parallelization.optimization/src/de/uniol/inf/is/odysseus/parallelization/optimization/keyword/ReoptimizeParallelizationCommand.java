/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.optimization.keyword;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.optimization.ParallelizationOptimizer;

/**
 * @author Dennis Nowak
 *
 */
public class ReoptimizeParallelizationCommand extends AbstractExecutorCommand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5413068863041478713L;
	
	private String queryName;
	private int queryId;
	private final boolean useId;

	public ReoptimizeParallelizationCommand(ISession caller, String queryName) {
		super(caller);
		this.queryName = queryName;
		this.useId = false;
	}
	
	public ReoptimizeParallelizationCommand(ISession caller, int queryId) {
		super(caller);
		this.queryId = queryId;
		this.useId = true;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand#execute(de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable, de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable, de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor)
	 */
	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		IPhysicalQuery query;
		if(useId) {
			query = executor.getExecutionPlan(getCaller()).getQueryById(queryId, getCaller());
		} else {
			Resource queryResource = new Resource(getCaller().getUser(), queryName);
			query= executor.getExecutionPlan(getCaller()).getQueryByName(queryResource, getCaller());
		}
		ParallelizationOptimizer.getInstance().reoptimizeQuery(query, getCaller());

	}

}
