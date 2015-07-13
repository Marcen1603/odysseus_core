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
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ICrashDetectionListener;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

/**
 * The query state recovery component handles the backup and recovery of queries
 * (queries and their states, sources, sinks). <br />
 * 
 * Note for logging of added queries: For each Odysseus-Script, (1) the inner
 * queries are delivered with their parsers (e.g., PQL) and (2) the complete
 * Odysseus-Script is delivered. Only the latter will be logged, because it
 * contains all information.
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
	 * The current session, if retrieved.
	 */
	private static Optional<ISession> cSession = Optional.absent();

	/**
	 * Gets the current session.
	 * 
	 * @return {@link ISessionManagement#loginSuperUser(Object, String)}
	 */
	private static ISession getSession() {
		if (!cSession.isPresent() || !cSession.get().isValid()) {
			cSession = Optional
					.of(UserManagementProvider
							.getUsermanagement(true)
							.getSessionManagement()
							.loginSuperUser(
									null,
									UserManagementProvider.getDefaultTenant()
											.getName()));
		}
		return cSession.get();
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
		if (!cExecutor.isPresent()) {
			cEntriesForRecovery.addAll(log);
			return;
		}
		cLog.debug("Begin of recovery...");
		for (ISysLogEntry entry : log) {
			if (entry.getTag().equals(QueryStateUtils.TAG_QUERYADDED)
					|| entry.getTag().startsWith(
							QueryStateUtils.TAG_QUERYSTATECHANGED)) {
				cLog.debug("Try to recover {}", entry);
				if (!entry.getComment().isPresent()) {
					cLog.error("No comment found for {}!", entry);
					continue;
				}

				if (entry.getTag().equals(QueryStateUtils.TAG_QUERYADDED)) {
					recoverQueryAddEvent(entry);
				} else if (entry.getComment().get()
						.equals(QueryStateUtils.COMMENT_QUERYREMOVED)) {
					recoverQueryRemovedEvent(entry);
				} else {
					recoverQueryStateChangedEvent(entry);
				}
			}
		}
		cLog.debug("Finished QueryStateRecovery.");
	}

	/**
	 * Recovers added queries (and sources).
	 * 
	 * @param entry
	 *            A system log entry with an {@link QueryAddedInfo} as comment.
	 */
	private void recoverQueryAddEvent(ISysLogEntry entry) {
		QueryAddedInfo info = QueryAddedInfo.fromBase64Binary(entry
				.getComment().get());
		cExecutor.get().addQuery(info.getQueryText(), info.getParserId(),
				getSession(), info.getContext());
		cLog.debug("Added query.");
		if (info.getRecoveryExecutor().isPresent()) {
			try {
				info.getRecoveryExecutor().get().recover(info.getQueryIds());
			} catch (Exception e) {
				cLog.error("Error while calling recovery executor for queries "
						+ info.getQueryText(), e);
			}
		}
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
				.startsWith(QueryState.PARTIAL.toString())) {
			// <state> + blank + <sheddingFactor>
			int sheddingFactor = Integer.parseInt(entry.getComment().get()
					.replace(QueryState.PARTIAL.toString(), "").trim());
			;
			cExecutor.get().partialQuery(queryId, sheddingFactor, getSession());
			cLog.debug("Set query to be partial.");
		} else if (entry.getComment().get()
				.startsWith(QueryState.PARTIAL_SUSPENDED.toString())
				|| entry.getComment().get()
						.equals(QueryState.SUSPENDED.toString())) {
			cExecutor.get().suspendQuery(queryId, getSession());
			cLog.debug("Suspended query.");
		} else if (entry.getComment().get()
				.startsWith(QueryState.RUNNING.toString())) {
			cExecutor.get().startQuery(queryId, getSession());
			cLog.debug("Started query.");
		} else {
			cLog.error("Unknown query state: {}", entry.getComment().get());
		}
	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds,
			String buildConfig, String parserID, ISession user, Context context) {
		/*
		 * Note: For each Odysseus-Script, (1) the inner queries are delivered
		 * with their parsers (e.g., PQL) and (2) the complete Odysseus-Script
		 * is delivered. Only the latter will be logged, because it contains all
		 * information.
		 */
		if (!parserID.equals(OdysseusScriptParser.PARSER_NAME)) {
			return;
		}

		QueryAddedInfo info = new QueryAddedInfo();
		info.setParserId(parserID);
		info.setQueryText(query);
		info.setContext(context);
		// TODO read IRecoveryExecutor from Odysseus Script
		if (queryIds.isEmpty()) {
			// Source definition
			cLog.debug("Source added.");
		} else {
			// "Normal" query
			info.setQueryIds(queryIds);
			cLog.debug("Query added.");
		}

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
				// The shedding factor is needed for partial queries
				comment = query.getState().toString() + " "
						+ query.getSheddingFactor();
			}
			cLog.debug("Query state changed for query {}: {}", queryId, comment);
			if (cSystemLog.isPresent()) {
				cSystemLog.get().write(
						QueryStateUtils.getTagQueryChanged(queryId),
						System.currentTimeMillis(), comment);
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
	public void onCrashDetected(final long lastStartup) throws Throwable {
		if (!cSystemLog.isPresent()) {
			cLog.error("No system log bound!");
		} else {
			new Thread() {

				@Override
				public void run() {
					try {
						recover(cSystemLog.get().read(lastStartup));
					} catch (Throwable t) {
						cLog.error("Could not recover!", t);
					}
				}
			}.start();
		}
	}

}