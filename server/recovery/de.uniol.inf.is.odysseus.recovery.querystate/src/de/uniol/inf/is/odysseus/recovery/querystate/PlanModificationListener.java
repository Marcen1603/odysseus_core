package de.uniol.inf.is.odysseus.recovery.querystate;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.PartialQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.RemoveQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.ResumeQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StopQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.SuspendQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Class to listen to {@code AbstractPlanModificationEvents}, to create
 * {@link IExecutorCommands} for events, which modified a query and to call
 * {@code QueryStateRecoveryComponent} to store them.
 * 
 * @author Michael Brand
 *
 */
public class PlanModificationListener implements IPlanModificationListener {

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		ISession caller = query.getSession();
		String queryName = query.getName();
		IExecutorCommand command = null;
		switch ((PlanModificationEventType) eventArgs.getEventType()) {
		case QUERY_PARTIAL:
			command = new PartialQueryCommand(caller, queryName, query.getSheddingFactor());
			break;
		case QUERY_REMOVE:
			command = new RemoveQueryCommand(caller, queryName);
			break;
		case QUERY_RESUME:
			command = new ResumeQueryCommand(caller, queryName);
			break;
		case QUERY_START:
			command = new StartQueryCommand(caller, queryName);
			break;
		case QUERY_STOP:
			command = new StopQueryCommand(caller, queryName);
			break;
		case QUERY_SUSPEND:
			command = new SuspendQueryCommand(caller, queryName);
			break;
		default:
			/*
			 * Nothing to do for: PLAN_REOPTIMIZE, QUERY_ADDED, QUERY_REOPTIMIZE
			 */
			return;
		}
		QueryStateRecoveryComponent.backupExecutorCommand(command, System.currentTimeMillis());
	}

}
