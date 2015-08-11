package de.uniol.inf.is.odysseus.recovery.querystate;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo.AbstractQueryStateInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo.QueryRemovedInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo.QueryStateChangedInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo.ScriptAddedInfo;
import de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo.SourceRemovedInfo;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ICrashDetectionListener;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.keyword.RecoveryConfigKeyword;

/**
 * The query state recovery component handles the backup and recovery of queries
 * (queries and their states, sources, sinks). <br />
 * <br />
 * 
 * Global recovery component without dependencies. Not to be called by an
 * {@link IRecoveryExecutor}. <br />
 * <br />
 * 
 * Note for logging of added queries/sources: For each Odysseus-Script, (1) the
 * inner queries are delivered with their parsers (e.g., PQL) and (2) the
 * complete Odysseus-Script is delivered. Only the latter will be logged,
 * because it contains all information.
 * 
 * @author Michael Brand
 *
 */
public class QueryStateRecoveryComponent implements IRecoveryComponent,
		IQueryAddedListener, ICrashDetectionListener, IDataDictionaryListener,
		IPlanModificationListener {

	/*
	 * XXX QueryStateRecoveryComponent-Optimization: For now, this component
	 * reads the relevant entries as a history and restores them ignoring the
	 * future. So a source may be added are directly dropped during recovery.
	 * The reason is, that the complete Odysseus script (may contain more than
	 * one query) is stored and recovered. To identify different log entries,
	 * which belong together (e.g., creation and drop), the restored script
	 * could be parsed.
	 */

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(QueryStateRecoveryComponent.class);

	/**
	 * The name for backup threads.
	 */
	private static final String cBackupThreadName = "QueryStateBackup";

	/**
	 * The name for backup recovery.
	 */
	private static final String cReoveryThreadName = "QueryStateRecovery";

	/**
	 * Recovery will be started.
	 */
	@Override
	public void onCrashDetected(final long lastStartup) throws Throwable {
		new Thread(cReoveryThreadName) {

			@Override
			public void run() {
				if (!cSystemLog.isPresent()) {
					cLog.error("Could not start recovery, because no system log is bound!");
				} else if (!cExecutor.isPresent()) {
					cLog.error("Could not start recovery, because no executor is bound!");
				} else {
					cLog.debug("Starting recovery...");
					recoverFromLog(cSystemLog.get().read(lastStartup),
							cExecutor.get());
					cLog.debug("Recovery finished.");
				}
			}

		}.start();

	}

	/**
	 * Only for Odysseus Script queries a backup will be created.
	 */
	@Override
	public void queryAddedEvent(final String query,
			final List<Integer> queryIds,
			final QueryBuildConfiguration buildConfig, final String parserId,
			final ISession user, final Context context) {
		if (parserId.equals(OdysseusScriptParser.PARSER_NAME)) {
			new Thread(cBackupThreadName) {

				@Override
				public void run() {
					if (!cSystemLog.isPresent()) {
						cLog.error("Could not start backup, because no system log is bound!");
					} else if (!cExecutor.isPresent()) {
						cLog.error("Could not start backup, because no executor is bound!");
					} else {
						cLog.debug("Starting backup...");
						backupScript(query, queryIds, parserId, user, context,
								cExecutor.get());
						cLog.debug("Backup finished.");
					}
				}

			}.start();
		}
	}

	/**
	 * The removal of a source will be logged.
	 */
	@Override
	public void removedViewDefinition(final IDataDictionary sender,
			final String name, ILogicalOperator op) {
		new Thread(cBackupThreadName) {

			@Override
			public void run() {
				if (!cSystemLog.isPresent()) {
					cLog.error("Could not start backup, because no system log is bound!");
				} else {
					cLog.debug("Starting backup...");
					backupSourceRemoval(name, cUsedDataDictionaries.get(sender));
					cLog.debug("Backup finished.");
				}
			}

		}.start();
	}

	/**
	 * The removal of a query and query state changes will be logged.
	 */
	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		PlanModificationEventType eventType = (PlanModificationEventType) eventArgs
				.getEventType();
		final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		final int queryId = query.getID();
		final ISession caller = query.getSession();
		switch (eventType) {
		case QUERY_REMOVE:
			new Thread(cBackupThreadName) {

				@Override
				public void run() {
					if (!cSystemLog.isPresent()) {
						cLog.error("Could not start backup, because no system log is bound!");
					} else {
						cLog.debug("Starting backup...");
						backupQueryRemoval(queryId, caller);
						cLog.debug("Backup finished.");
					}
				}

			}.start();
			break;
		case QUERY_PARTIAL:
		case QUERY_RESUME:
		case QUERY_START:
		case QUERY_STOP:
		case QUERY_SUSPEND:
			new Thread(cBackupThreadName) {

				@Override
				public void run() {
					if (!cSystemLog.isPresent()) {
						cLog.error("Could not start backup, because no system log is bound!");
					} else {
						cLog.debug("Starting backup...");
						backupQueryStateChange(queryId, query.getState(),
								query.getSheddingFactor(), caller);
						cLog.debug("Backup finished.");
					}
				}

			}.start();
			break;
		case QUERY_ADDED:
		case PLAN_REOPTIMIZE:
		case QUERY_REOPTIMIZE:
			// Nothing to do.
			break;
		}
	}

	/**
	 * No-Op. Already done by
	 * {@link #addedViewDefinition(IDataDictionary, String, ILogicalOperator)}.
	 */
	@Override
	public void addedViewDefinition(IDataDictionary sender, String name,
			ILogicalOperator op) {
		// Nothing to do.
	}

	/**
	 * No-Op, because the only logged events are source creations and removals.
	 */
	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// Nothing to do.
	}

	@Override
	public String getName() {
		return "Query State";
	}

	/**
	 * Global recovery component. Not to be called for certain queries.
	 */
	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries) {
		// Nothing to do.
		return queries;
	}

	/**
	 * Global recovery component. Not to be called for certain queries.
	 */
	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries) {
		// Nothing to do.
		return queries;
	}

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
		if (cSystemLog.isPresent() && cSystemLog.get() == log) {
			cSystemLog = Optional.absent();
		}
	}

	/**
	 * The server executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * All sessions used to retrieve data dictionary updates.
	 */
	private static Map<IDataDictionary, ISession> cUsedDataDictionaries = Maps
			.newConcurrentMap();

	/**
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			((IServerExecutor) executor).addQueryAddedListener(this);
			((IServerExecutor) executor).addPlanModificationListener(this);
			cExecutor = Optional.of((IServerExecutor) executor);
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
			((IServerExecutor) executor).removeQueryAddedListener(this);
			((IServerExecutor) executor).removePlanModificationListener(this);
			cExecutor = Optional.absent();
			for (IDataDictionary usedDict : cUsedDataDictionaries.keySet()) {
				usedDict.removeListener(this);
			}
			cUsedDataDictionaries.clear();
		}
	}

	/**
	 * Backups a new script (new source definition and/or query).
	 * 
	 * @param script
	 *            The script to backup.
	 * @param queryIds
	 *            The ids of the contained queries (source definitions have no
	 *            query id).
	 * @param parserId
	 *            The used parser.
	 * @param user
	 *            The caller.
	 * @param context
	 *            The context.
	 * @param executor
	 *            A present executor.
	 */
	private void backupScript(String script, List<Integer> queryIds,
			String parserId, ISession user, Context context,
			IServerExecutor executor) {
		ScriptAddedInfo info = new ScriptAddedInfo();
		info.setContext(context);
		info.setParserId(parserId);
		info.setQueryIds(queryIds);
		info.setQueryText(script);
		info.setSession(user);
		cSystemLog.get().write(QueryStateLogTag.SCRIPT_ADDED.toString(),
				System.currentTimeMillis(), info.toBase64Binary());

		// Get data dictionary news
		if (!cUsedDataDictionaries.values().contains(user)) {
			IDataDictionary dict = executor.getDataDictionary(user);
			dict.addListener(this);
			cUsedDataDictionaries.put(dict, user);
		}
	}

	/**
	 * Backups a source removal event. <br />
	 * Precondition: system log is bound.
	 * 
	 * @param name
	 *            The name of the removed source.
	 * @param user
	 *            The caller.
	 */
	private void backupSourceRemoval(String name, ISession user) {
		SourceRemovedInfo info = new SourceRemovedInfo();
		info.setSourceName(name);
		info.setSession(user);
		cSystemLog.get().write(QueryStateLogTag.SOURCE_REMOVED.toString(),
				System.currentTimeMillis(), info.toBase64Binary());
	}

	/**
	 * Backups a query removal event. <br />
	 * Precondition: system log is bound.
	 * 
	 * @param id
	 *            The id of the removed query.
	 * @param user
	 *            The caller.
	 */
	private void backupQueryRemoval(int id, ISession user) {
		QueryRemovedInfo info = new QueryRemovedInfo();
		info.setQueryId(id);
		info.setSession(user);
		cSystemLog.get().write(QueryStateLogTag.QUERY_REMOVED.toString(),
				System.currentTimeMillis(), info.toBase64Binary());
	}

	/**
	 * Backups a query state change event. <br />
	 * Precondition: system log is bound.
	 * 
	 * @param id
	 *            The id of the query.
	 * @param state
	 *            The new state of the query.
	 * @param sheddingFactor
	 *            The shedding factor of the query.
	 * @param user
	 *            The caller.
	 */
	private void backupQueryStateChange(int id, QueryState state,
			int sheddingFactor, ISession user) {
		QueryStateChangedInfo info = new QueryStateChangedInfo();
		info.setQueryId(id);
		info.setQueryState(state);
		info.setSheddingFactor(sheddingFactor);
		info.setSession(user);
		cSystemLog.get().write(QueryStateLogTag.QUERYSTATE_CHANGED.toString(),
				System.currentTimeMillis(), info.toBase64Binary());
	}

	/**
	 * Starts the recovery process.
	 * 
	 * @param log
	 *            All system log entries to recover.
	 * @param executor
	 *            A present executor.
	 */
	private static void recoverFromLog(List<ISysLogEntry> log,
			IServerExecutor executor) {
		// Mapping of the last seen log entry (the contained info, but not yet
		// recovered) for each query (state change or removal, not creation).
		Map<Integer, QueryStateChangedInfo> lastSeenEntries = Maps.newHashMap();
		for (ISysLogEntry entry : log) {
			if (QueryStateLogTag.containsTag(entry.getTag())) {
				cLog.debug("Try to recover '{}'...", entry);
				if (!entry.getComment().isPresent()) {
					cLog.error("Comment needed for '" + entry.getTag()
							+ "' system log entries!");
					continue;
				}
				QueryStateLogTag enumEntry = QueryStateLogTag.fromString(
						entry.getTag()).get();
				switch (enumEntry) {
				case SCRIPT_ADDED:
					recoverScript(entry, log, executor);
					break;
				case SOURCE_REMOVED:
					recoverSourceRemoval(entry, executor);
					break;
				case QUERY_REMOVED:
				case QUERYSTATE_CHANGED:
					QueryStateChangedInfo info = (QueryStateChangedInfo) AbstractQueryStateInfo
							.fromBase64Binary(entry.getComment().get());
					lastSeenEntries.put(info.getQueryId(), info);
					break;
				}
			}
		}
		for (int queryId : lastSeenEntries.keySet()) {
			recoverQueryStateChange(lastSeenEntries.get(queryId), executor);
		}
	}

	/**
	 * Recovers a script, which was backuped by
	 * {@link #backupScript(String, List, String, ISession, Context)}.
	 * 
	 * @param entry
	 *            The entry representing a backuped script.
	 * @param log
	 *            All system log entries to recover.
	 * @param executor
	 *            A present executor.
	 */
	private static void recoverScript(ISysLogEntry entry,
			List<ISysLogEntry> log, IServerExecutor executor) {
		ScriptAddedInfo info = (ScriptAddedInfo) AbstractQueryStateInfo
				.fromBase64Binary(entry.getComment().get());
		// XXX QueryBuildConfiguration and RecoveryNeeded: Problem is, that the
		// query build configuration is only used for the outer Odysseus-Script
		// query. But there, the RecoveryConfigKeyword
		// is not executed. Within the inner queries (e.g., PQL), the keyword is
		// executed, but the query build configuration is set to normal
		// workaround is to modify the script.
		String modifiedQueryText = insertRecoveryNeededArgument(info
				.getQueryText());
		executor.addQuery(modifiedQueryText, info.getParserId(),
				info.getSession(), info.getContext());
	}

	/**
	 * Inserts "recoveryneeded=true" as key value argument for the used recovery
	 * executor into the script to be executed.
	 * 
	 * @param queryText
	 *            The script.
	 * @return A modified script.
	 */
	private static String insertRecoveryNeededArgument(String queryText) {
		String toInsert = " " + AbstractRecoveryExecutor.RECOVERY_NEEDED_KEY
				+ "=true";
		int keywordIndex = queryText.indexOf(RecoveryConfigKeyword.getName());
		int endLineIndex = queryText.indexOf("\n", keywordIndex) - 1;
		if (keywordIndex != -1 && endLineIndex > keywordIndex) {
			StringBuffer out = new StringBuffer();
			out.append(queryText.substring(0, endLineIndex));
			out.append(toInsert);
			out.append(queryText.substring(endLineIndex));
			return out.toString();
		}
		return queryText;
	}

	/**
	 * Recovers a source removal event (removes the source again).
	 * 
	 * @param entry
	 *            The entry representing a source removal.
	 * @param executor
	 *            A present executor.
	 */
	private static void recoverSourceRemoval(ISysLogEntry entry,
			IServerExecutor executor) {
		SourceRemovedInfo info = (SourceRemovedInfo) AbstractQueryStateInfo
				.fromBase64Binary(entry.getComment().get());
		executor.removeViewOrStream(info.getSourceName(), info.getSession());
	}

	/**
	 * Recovers a query state change event (changes the query state again).
	 * 
	 * @param info
	 *            The info representing a query state change.
	 * @param executor
	 *            A present executor.
	 */
	private static void recoverQueryStateChange(QueryStateChangedInfo info,
			IServerExecutor executor) {
		int queryID = info.getQueryId();
		ISession caller = info.getSession();
		switch (info.getQueryState()) {
		case INACTIVE:
			executor.stopQuery(queryID, caller);
			break;
		case PARTIAL:
			executor.partialQuery(queryID, info.getSheddingFactor(), caller);
			break;
		case PARTIAL_SUSPENDED:
		case SUSPENDED:
			executor.suspendQuery(queryID, caller);
			break;
		case RUNNING:
			executor.startQuery(queryID, caller);
			break;
		case UNDEF:
			executor.removeQuery(queryID, caller);
		}
	}

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		return new QueryStateRecoveryComponent();
	}

}