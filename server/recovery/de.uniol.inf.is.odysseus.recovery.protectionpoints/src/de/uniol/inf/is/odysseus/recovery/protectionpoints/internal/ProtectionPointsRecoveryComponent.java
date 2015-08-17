package de.uniol.inf.is.odysseus.recovery.protectionpoints.internal;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

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
import de.uniol.inf.is.odysseus.recovery.protectionpoints.IProtectionPointManager;
import de.uniol.inf.is.odysseus.recovery.protectionpoints.ProtectionPointManagerRegistry;

/**
 * The incoming elements recovery component handles the protection points for a
 * set of queries. <br />
 * <br />
 * The component uses an {@link IProtectionPointManager}, which notifies other
 * recovery components, if a protection point is reached.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class ProtectionPointsRecoveryComponent implements IRecoveryComponent, IPlanModificationListener {

	@Override
	public String getName() {
		return "Protection Points";
	}

	/**
	 * The server executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			cExecutor = Optional.of((IServerExecutor) executor);
			((IServerExecutor) executor).addPlanModificationListener(this);
		}
	}

	/**
	 * Unbinds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public void unbindServerExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			cExecutor = Optional.absent();
			((IServerExecutor) executor).removePlanModificationListener(this);
		}
	}

	/**
	 * The protection point manager to use.
	 */
	private IProtectionPointManager mManager;

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		ProtectionPointsRecoveryComponent component = new ProtectionPointsRecoveryComponent();
		component.mManager = ProtectionPointManagerRegistry.createFromConfig(config);
		return component;
	}
	
	/**
	 * All queries (their ids), for which the protection point manager is already set.
	 * @see ProtectionPointManagerRegistry#setProtectionPointManager(int, IProtectionPointManager)
	 */
	private final Set<Integer> mProcessedQueries = Sets.newHashSet();

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		for (ILogicalQuery query : queries) {
			ProtectionPointManagerRegistry.setProtectionPointManager(query.getID(), this.mManager);
			this.mProcessedQueries.add(new Integer(query.getID()));
		}
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		for (ILogicalQuery query : queries) {
			if(!this.mProcessedQueries.contains(new Integer(query.getID()))) {
				ProtectionPointManagerRegistry.setProtectionPointManager(query.getID(), this.mManager);
			}
		}
		return queries;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType) eventArgs.getEventType();
		final int queryId = ((IPhysicalQuery) eventArgs.getValue()).getID();
		switch (eventType) {
		case QUERY_START:
		case QUERY_RESUME:
			// This method is only called once, because OSGi knows only one
			// instance of this components. All concrete instances are created
			// with newInstance. So in this method, mManager is null.
			ProtectionPointManagerRegistry.getInstance(queryId).start();
			break;
		default:
			// Nothing to do.
			// FIXME stop, if query is stopped or paused.
			break;
		}
	}

}