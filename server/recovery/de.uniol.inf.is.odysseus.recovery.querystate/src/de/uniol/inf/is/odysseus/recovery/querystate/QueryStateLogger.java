package de.uniol.inf.is.odysseus.recovery.querystate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.AbstractQueryAddedInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.QueryAddedEntryInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.SourceAddedEntryInfo;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;

/**
 * Class to add the following actions to the system log: adding of a query (or
 * source definition) and changing the state of a query.
 * 
 * @author Michael Brand
 *
 */
public class QueryStateLogger implements IQueryAddedListener,
		IPlanModificationListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(QueryStateLogger.class);

	/**
	 * The system log, if bound.
	 */
	private static Optional<ISystemLog> cSystemLog = Optional.absent();

	/**
	 * Binds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to bind.
	 */
	public static void bindSystemLog(ISystemLog log) {
		cSystemLog = Optional.of(log);
		cLog.debug("Bound '{}' as an implementation of ISystemLog.", log
				.getClass().getSimpleName());
	}

	/**
	 * Unbinds an implementation of the system log.
	 * 
	 * @param log
	 *            The implementation to unbind.
	 */
	public static void unbindSystemLog(ISystemLog log) {
		if (cSystemLog.isPresent() && log == cSystemLog.get()) {
			cSystemLog = Optional.absent();
			cLog.debug("Unbound '{}' as an implementation of ISystemLog.", log
					.getClass().getSimpleName());
		}
	}

	/**
	 * The executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor;

	/**
	 * Binds an implementation of the executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindExecutor(IExecutor executor) {
		IServerExecutor serverExecutor = (IServerExecutor) executor;
		serverExecutor.addQueryAddedListener(this);
		serverExecutor.addPlanModificationListener(this);
		cExecutor = Optional.of(serverExecutor);
	}

	/**
	 * Unbinds an implementation of the executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public void unbindExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			IServerExecutor serverExecutor = (IServerExecutor) executor;
			serverExecutor.removeQueryAddedListener(this);
			serverExecutor.removePlanModificationListener(this);
			cExecutor = Optional.absent();
		}

	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds,
			String buildConfig, String parserID, ISession user, Context context) {
		AbstractQueryAddedInfo entry;
		if (queryIds.isEmpty()) {
			// Source definition
			entry = new SourceAddedEntryInfo();
			cLog.debug("Source added: " + query);
		} else {
			entry = new QueryAddedEntryInfo();
			((QueryAddedEntryInfo) entry).ids = Lists.newArrayList(queryIds);
			cLog.debug("Query added: " + query);
		}
		entry.parserId = parserID;
		entry.queryText = query;
		entry.buildConfiguration = buildConfig;
		entry.session = user;

		// Write to log
		if (cSystemLog.isPresent()) {
			cSystemLog.get().write(QueryStateUtils.TAG_QUERYADDED,
					System.nanoTime(), entry.toBase64Binary());
		}
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (!PlanModificationEventType.QUERY_ADDED.equals(eventArgs
				.getEventType())
				&& !PlanModificationEventType.QUERY_REOPTIMIZE.equals(eventArgs
						.getEventType())) {
			// Query state changed
			IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
			int queryId = query.getID();
			String comment;
			if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs
					.getEventType())) {
				comment = QueryStateUtils.COMMENT_QUERYREMOVED;
			} else {
				comment = query.getState().toString();
			}
			cLog.debug("Query state changed for query {}: {}", queryId, comment);
			if (cSystemLog.isPresent()) {
				cSystemLog.get().write(
						QueryStateUtils.getTagQueryChanged(queryId),
						System.nanoTime(), comment);
			}
		}
	}

}