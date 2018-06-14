package de.uniol.inf.is.odysseus.recovery.checkpointing;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Component that uses {@link ICheckpointManager} to manage checkpoints. Other
 * {@link IRecoveryComponent} may implement {@link ICheckpointListener} to get
 * notified, when a checkpoint is reached and the query execution is suspended.
 * 
 * @author Michael Brand
 *
 */
public class CheckpointingRecoveryComponent implements IRecoveryComponent, IPlanModificationListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CheckpointingRecoveryComponent.class);

	/**
	 * The checkpoint manager to use.
	 */
	private ICheckpointManager manager;

	/**
	 * All queries (their ids), for which the checkpoint manager is already set.
	 */
	private final Set<Integer> processedQueries = new HashSet<>();

	/**
	 * Registers this component as {@link IPlanModificationListener}.
	 */
	public void bindExecutor(IExecutor executor) {
		if (executor instanceof IServerExecutor) {
			((IServerExecutor) executor).addPlanModificationListener(this);
		}
	}

	/**
	 * Unregisters this component as {@link IPlanModificationListener}.
	 */
	public void unbindExecutor(IExecutor executor) {
		if (executor instanceof IServerExecutor) {
			((IServerExecutor) executor).removePlanModificationListener(this);
		}
	}

	@Override
	public void initialize(Properties config) {
		try {
			this.manager = CheckpointManagerRegistry.createFromConfig(config);
		} catch (Exception e) {
			LOG.error("Could not create checkpoint manager!", e);
		}
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		for (ILogicalQuery query : queries) {
			CheckpointManagerRegistry.setCheckpointManager(query.getID(), this.manager);
			this.processedQueries.add(query.getID());
		}
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		for (ILogicalQuery query : queries) {
			if (!this.processedQueries.contains(query.getID())) {
				CheckpointManagerRegistry.setCheckpointManager(query.getID(), this.manager);
			}
		}
		return queries;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType) eventArgs.getEventType();
		final int queryId = ((IPhysicalQuery) eventArgs.getValue()).getID();

		// This method is only called once, because OSGi knows only one
		// instance of this components. All concrete instances are created
		// with newInstance. So in this method, mManager is null.
		ICheckpointManager manager = CheckpointManagerRegistry.getInstance(queryId);
		if (manager != null) {
			switch (eventType) {
			case QUERY_START:
			case QUERY_RESUME:
				manager.start();
				break;
			case QUERY_STOP:
			case QUERY_SUSPEND:
				manager.stop();
				break;
			default:
				// Nothing to do.
				break;
			}
		}
	}

	/**
	 * Adds a listener to the checkpoint manager.
	 */
	public void addCheckpointManagerListener(ICheckpointListener listener) {
		this.manager.addListener(listener);
	}

	/**
	 * Removes a listener from the checkpoint manager.
	 */
	public void removeCheckpointManagerListener(ICheckpointListener listener) {
		this.manager.removeListener(listener);
	}

}