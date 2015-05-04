package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlanModificationActionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ACQueryParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PlanModificationActionPO extends AbstractSink<Tuple<?>> {

	private static final Logger LOG = LoggerFactory.getLogger(PlanModificationActionPO.class);
	
	private final IServerExecutor executor;
	private final SDFAttribute commandAttribute;
	private final SDFAttribute queryIDAttribute;
	
	private int commandAttributeIndex;
	private int queryIDAttributeIndex;

	private ISession caller;
	
	public PlanModificationActionPO(PlanModificationActionAO ao, IServerExecutor executor) {
		Preconditions.checkNotNull(executor, "ServerExecutor must not be null!");
		Preconditions.checkNotNull(ao, "PlanModificationActionAO must not be null!");
		
		this.executor = executor;
		this.commandAttribute = ao.getCommandAttribute();
		this.queryIDAttribute = ao.getQueryIDAttribute();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		SDFSchema schema = getOutputSchema();
		
		// AO made sure that these attribute are valid
		commandAttributeIndex = schema.indexOf(commandAttribute);
		queryIDAttributeIndex = schema.indexOf(queryIDAttribute);
		
		List<ISession> callers = getSessions();
		
		if (callers.size() != 1){
			throw new OpenFailedException("This operator cannot be sharded");
		}
		
		this.caller = callers.get(0);

	}
	
	@Override
	protected void process_next(Tuple<?> object, int port) {
		// AO made sure that this attributes are of types string and numeric
		String command = object.getAttribute(commandAttributeIndex);
		Integer queryID = object.getAttribute(queryIDAttributeIndex);
		
		if( !Strings.isNullOrEmpty(command) && queryID != null) {
			tryExecuteCommand(command, queryID);
		}
	}

	private void tryExecuteCommand(String command, Integer queryID) {
		IPhysicalQuery physicalQuery = executor.getExecutionPlan().getQueryById(queryID);
		if( physicalQuery == null ) {
			LOG.error("Query id {} does not exist", queryID);
			return;
		}
		
		// avoid controlling rule-queries
		Object parameter = physicalQuery.getLogicalQuery().getParameter(ACQueryParameter.class.getSimpleName());
		if( parameter != null && parameter instanceof ACQueryParameter ) {
			if( ((ACQueryParameter)parameter).getValue() ) {
				return;
			}
		}
		
		
		try {
			switch( command.toUpperCase() ) {
			case "QUERY_ADD":
			case "QUERY_ADDED":
				LOG.error("Command QUERY_ADDED not supported, yet.");
				break;
				
			case "QUERY_REMOVE":
				executor.removeQuery(queryID, caller);
				break;
				
			case "QUERY_START":
				executor.startQuery(queryID, caller);
				break;
				
			case "QUERY_STOP":
				executor.stopQuery(queryID, caller);
				break;
				
			case "QUERY_SUSPEND":
				executor.suspendQuery(queryID, caller);
				break;
				
			case "QUERY_PARTIAL":
				executor.partialQuery(queryID, 50, caller);
				break;
				
			case "QUERY_FULL":
				executor.partialQuery(queryID, 100, caller);
				break;
				
			case "QUERY_RESUME":
				executor.resumeQuery(queryID, caller);
				break;
				
			default:
				LOG.error("Unknown command '{}' for query id {}", command, queryID);
			}
		} catch( Throwable t ) {
			LOG.error("Could not execute command '{}' for query id {}", new Object[]{ command, queryID, t});
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// ignore
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}
}
