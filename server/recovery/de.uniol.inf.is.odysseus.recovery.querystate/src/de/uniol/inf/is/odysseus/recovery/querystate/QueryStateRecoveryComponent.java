package de.uniol.inf.is.odysseus.recovery.querystate;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.AbstractQueryAddedInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.QueryAddedEntryInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.SourceAddedEntryInfo;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ICrashDetectionListener;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

/**
 * The query state recovery component handles the backup and recovery of queries
 * (queries and their states, sources, sinks).
 * 
 * @author Michael Brand
 *
 */
public class QueryStateRecoveryComponent implements IRecoveryComponent,
		IQueryAddedListener, IPlanModificationListener, ICrashDetectionListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(QueryStateRecoveryComponent.class);

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
		}
	}

	/**
	 * The list of system log entries for recovery, if crash has been detected
	 * before an executor is bound.
	 */
	private static List<ISysLogEntry> cEntriesForRecovery = Lists
			.newArrayList();

	/**
	 * The executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

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
		if (!cEntriesForRecovery.isEmpty()) {
			try {
				recover(cEntriesForRecovery);
			} catch (Exception e) {
				cLog.error("Could not recover!", e);
			}
			cEntriesForRecovery.clear();
		}
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

	/**
	 * Gets the current session.
	 * 
	 * @return {@link ISessionManagement#loginSuperUser(Object, String)}
	 */
	private static ISession getSession() {
		return UserManagementProvider.getSessionmanagement().loginSuperUser(
				null, UserManagementProvider.getDefaultTenant().getName());
	}

	@Override
	public String getName() {
		return "QueryStateRecoveryComponent";
	}

	/**
	 * No dependencies.
	 */
	@Override
	public ImmutableCollection<String> getDependencies() {
		Set<String> dependencies = Sets.newHashSet();
		return ImmutableSet.copyOf(dependencies);
	}

	@Override
	public void recover(List<ISysLogEntry> log) throws Exception {
		// TODO to be tested 
		if (!cExecutor.isPresent()) {
			cEntriesForRecovery.addAll(log);
			return;
		}
		cLog.debug("Begin of recovery...");
		for (ISysLogEntry entry : log) {
			if (entry.getTag().equals(QueryStateUtils.TAG_QUERYADDED)
					|| entry.getTag().equals(
							QueryStateUtils.TAG_QUERYSTATECHANGED)) {
				cLog.debug("Try to recover {}", entry);
				if (!entry.getComment().isPresent()) {
					cLog.error("No comment found for {}!", entry);
					continue;
				}

				if (entry.getTag().equals(QueryStateUtils.TAG_QUERYADDED)) {
					recoverQueryAddEvent(entry);
					// TODO call IRecoveryExecutor
				} else if (entry.getComment().get()
						.equals(QueryStateUtils.COMMENT_QUERYREMOVED)) {
					recoverQueryRemovedEvent(entry);
				} else {
					recoverQueryStateChangedEvent(entry);
				}
			}
		}
	}

	/**
	 * Recovers added queries (and sources).
	 * 
	 * @param entry
	 *            A system log entry with an {@link AbstractQueryAddedInfo} as
	 *            comment.
	 */
	private void recoverQueryAddEvent(ISysLogEntry entry) {
		AbstractQueryAddedInfo info = AbstractQueryAddedInfo
				.fromBase64Binary(entry.getComment().get());
		// TODO GetSession does not work/is not ready
		cExecutor.get().addQuery(info.queryText, info.parserId, getSession(),
				info.getContext());
		cLog.debug("Added query.");
	}

	/**
	 * Recovers removed queries (removes them again).
	 * 
	 * @param entry
	 *            A system log entry representing the remove event.
	 */
	private void recoverQueryRemovedEvent(ISysLogEntry entry) {
		int queryId = QueryStateUtils.getQueryId(entry.getTag());
		cExecutor.get().removeQuery(queryId, getSession());
		cLog.debug("Removed query.", queryId);
	}

	/**
	 * Recovers query state change (makes the change again).
	 * 
	 * @param entry
	 *            A system log entry representing the change event.
	 */
	private void recoverQueryStateChangedEvent(ISysLogEntry entry) {
		int queryId = QueryStateUtils.getQueryId(entry.getTag());
		if (entry.getComment().get().equals(QueryState.INACTIVE.toString())) {
			cExecutor.get().stopQuery(queryId, getSession());
			cLog.debug("Stopped query.");
		} else if (entry.getComment().get()
				.equals(QueryState.PARTIAL.toString())) {
			// TODO need a shedding factor
			int sheddingFactor = 0;
			cExecutor.get().partialQuery(queryId, sheddingFactor, getSession());
			cLog.debug("Set query to be partial.");
		} else if (entry.getComment().get()
				.equals(QueryState.PARTIAL_SUSPENDED.toString())
				|| entry.getComment().get()
						.equals(QueryState.SUSPENDED.toString())) {
			cExecutor.get().suspendQuery(queryId, getSession());
			cLog.debug("Suspended query.");
		} else if (entry.getComment().get()
				.equals(QueryState.RUNNING.toString())) {
			cExecutor.get().startQuery(queryId, getSession());
			cLog.debug("Started query.");
		} else {
			cLog.error("Unknown query state: {}", entry.getComment().get());
		}
	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds,
			String buildConfig, String parserID, ISession user, Context context) {
		AbstractQueryAddedInfo info;
		if (queryIds.isEmpty()) {
			// Source definition
			/*
			 * Note: For a source defintion each source is added using PQL and
			 * afterwards the complete script is added with odysseus script.
			 */
			if (!parserID.equals(OdysseusScriptParser.PARSER_NAME)) {
				return;
			}
			info = new SourceAddedEntryInfo();
			cLog.debug("Source added.");
		} else {
			info = new QueryAddedEntryInfo();
			((QueryAddedEntryInfo) info).ids = Lists.newArrayList(queryIds);
			cLog.debug("Query added.");
		}

		info.parserId = parserID;
		info.queryText = query;
		info.setContext(context);
		// TODO read IRecoveryExecutor from Odysseus Script

		// Write to log
		if (cSystemLog.isPresent()) {
			cSystemLog.get().write(QueryStateUtils.TAG_QUERYADDED,
					System.currentTimeMillis(), info.toBase64Binary());
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

	/**
	 * Global recovery component which is always active.
	 */
	@Override
	public void activateBackup(List<Integer> queryIds) {
		// Nothing to do.
	}

	/**
	 * Global recovery component which is always active.
	 */
	@Override
	public void deactivateBackup(List<Integer> queryIds) {
		// Nothing to do.
	}

	/**
	 * Global recovery component which has no dependencies. Therefor it can
	 * directly react to crashes.
	 */
	@Override
	public void onCrashDetected(long lastStartup) throws Throwable {
		if (!cSystemLog.isPresent()) {
			cLog.error("No system log bound!");
		} else {
			recover(cSystemLog.get().read(lastStartup));
		}
	}

}