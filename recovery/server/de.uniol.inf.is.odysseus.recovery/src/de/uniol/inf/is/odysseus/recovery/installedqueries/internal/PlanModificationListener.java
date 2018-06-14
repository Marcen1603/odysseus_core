package de.uniol.inf.is.odysseus.recovery.installedqueries.internal;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.AbstractQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.PartialQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.RemoveQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.ResumeQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StopQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.SuspendQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.installedqueries.IInstalledQueriesHandler;

/**
 * A listener for fired {@link PlanModificationEvent}s (changes of query states
 * and removal of queries).
 * 
 * @author Michael Brand
 *
 */
public class PlanModificationListener implements IPlanModificationListener {

	/**
	 * The {@link IInstalledQueriesHandler} that handles the backup.
	 */
	private IInstalledQueriesHandler handler;

	/**
	 * Creates a new listener.
	 * 
	 * @param handler
	 *            The {@link IInstalledQueriesHandler} that handles the backup.
	 */
	public PlanModificationListener(IInstalledQueriesHandler handler) {
		this.handler = handler;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		ISession caller = query.getSession();
		Resource queryName = query.getName();
		int queryId = query.getID();
		AbstractQueryCommand cmd;
		switch ((PlanModificationEventType) eventArgs.getEventType()) {
		case QUERY_REMOVE:
			cmd = new RemoveQueryCommand(caller, queryName);
			cmd.setQueryId(queryId);
			this.handler.backup((RemoveQueryCommand) cmd, System.currentTimeMillis());
			break;
		case QUERY_START:
			cmd = new StartQueryCommand(caller, queryName);
			cmd.setQueryId(queryId);
			this.handler.backup((StartQueryCommand) cmd, System.currentTimeMillis());
			break;
		case QUERY_PARTIAL:
			cmd = new PartialQueryCommand(caller, queryName, query.getSheddingFactor());
			cmd.setQueryId(queryId);
			this.handler.backup((PartialQueryCommand) cmd, System.currentTimeMillis());
			break;
		case QUERY_STOP:
			cmd = new StopQueryCommand(caller, queryName);
			cmd.setQueryId(queryId);
			this.handler.backup((StopQueryCommand) cmd, System.currentTimeMillis());
			break;
		case QUERY_SUSPEND:
			cmd = new SuspendQueryCommand(caller, queryName);
			cmd.setQueryId(queryId);
			this.handler.backup((SuspendQueryCommand) cmd, System.currentTimeMillis());
			break;
		case QUERY_RESUME:
			cmd = new ResumeQueryCommand(caller, queryName);
			cmd.setQueryId(queryId);
			this.handler.backup((ResumeQueryCommand) cmd, System.currentTimeMillis());
			break;
		default:
			/*
			 * Nothing to do for: PLAN_REOPTIMIZE, QUERY_ADDED, QUERY_REOPTIMIZE
			 */
			return;
		}
	}

}