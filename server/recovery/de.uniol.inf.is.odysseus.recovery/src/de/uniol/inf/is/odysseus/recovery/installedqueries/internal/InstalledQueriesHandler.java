package de.uniol.inf.is.odysseus.recovery.installedqueries.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractDropStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.AbstractQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.PartialQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.RemoveQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.ResumeQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StopQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.SuspendQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterRecoveryConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.recovery.crashdetector.ICrashDetector;
import de.uniol.inf.is.odysseus.recovery.installedqueries.IInstalledQueriesHandler;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateEventListener;
import de.uniol.inf.is.odysseus.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.systemlog.ISystemLogEntry;

/**
 * Component that backups and recovers installed sources, sinks and queries.
 * <br />
 * <br />
 * Note that sources, sinks and queries get automatically backed up and
 * installed, when this component is active. The changes of query states, e.g.
 * starting a query, will also be automatically backed up, but not automatically
 * restored. This is because in some cases, recovery strategies do some
 * processing between reinstallation and restarting a query. To restore the
 * query states, call {@link #recoverQueryStates()}.
 *
 * @author Michael Brand
 *
 */
public class InstalledQueriesHandler implements IInstalledQueriesHandler, ISystemStateEventListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(InstalledQueriesHandler.class);

	/**
	 * The system log.
	 */
	private ISystemLog syslog;

	/**
	 * Binds a system log implementation.
	 */
	public void bindSystemLog(ISystemLog log) {
		this.syslog = log;
	}

	/**
	 * Removes the binding of a system log implementation.
	 */
	public void unbindSystemLog(ISystemLog log) {
		if (log == this.syslog) {
			this.syslog = null;
		}
	}

	///////////////////////////
	// Information Retrieval //
	///////////////////////////

	/**
	 * The listeners for all known {@link DataDictionaryProvider}s.
	 */
	private final Set<DataDictionaryProviderListener> dictListeners = new HashSet<>();

	/**
	 * Subscribe to all known {@link DataDictionaryProvider}s in order to
	 * retrieve all new created {@link IDataDictionary}s and their changes (e.g.
	 * new source).
	 */
	private void subscribeForDataDictionaries() {
		for (ITenant tenant : UserManagementProvider.instance.getTenants()) {
			Optional<IDataDictionary> dd = Optional.fromNullable(DataDictionaryProvider.instance.getDataDictionary(tenant));
			DataDictionaryProviderListener listener = new DataDictionaryProviderListener(this);
			if (dd.isPresent()) {
				listener.newDatadictionary(dd.get());
			}
			DataDictionaryProvider.instance.subscribe(tenant, listener);
			this.dictListeners.add(listener);
		}
	}

	/**
	 * Unsubscribe from all known {@link DataDictionaryProvider}s.
	 */
	private void unsubscribeForDataDictionaries() {
		for (DataDictionaryProviderListener listener : this.dictListeners) {
			DataDictionaryProvider.instance.unsubscribe(listener);
		}
	}

	/**
	 * The listener for executed {@link IExecutorCommand}s
	 * ({@link CreateQueryCommand} and {@link AddQueryCommand}).
	 */
	private final ExecutorCommandListener executorCommandListener = new ExecutorCommandListener(this);

	/**
	 * The listener for fired {@link PlanModificationEvent}s (changes of query
	 * states and removal of queries).
	 */
	private final PlanModificationListener planModificationListener = new PlanModificationListener(this);

	/**
	 * The executor.
	 */
	private IServerExecutor executor;

	/**
	 * Binds an executor implementation and registers the listeners.
	 */
	public void bindExecutor(IExecutor exe) {
		if (exe instanceof IServerExecutor) {
			this.executor = (IServerExecutor) exe;
			this.executor.addExecutorCommandListener(this.executorCommandListener);
			this.executor.addPlanModificationListener(this.planModificationListener);
			subscribeForDataDictionaries();
		}
	}

	/**
	 * Removes the binding of an executor implementation and removes the
	 * listeners.
	 */
	public void unbindExecutor(IExecutor exe) {
		if (exe == this.executor) {
			this.executor.removeExecutorCommandListener(this.executorCommandListener);
			this.executor.removePlanModificationListener(this.planModificationListener);
			unsubscribeForDataDictionaries();
			this.executor = null;
		}
	}

	/**
	 * Registers this component as crash detection listener.
	 */
	public void bindCrashDetector(ICrashDetector detector) {
		detector.addListener(this);
	}

	/**
	 * Unregisters this component as crash detection listener.
	 */
	public void unbindCrashDetector(ICrashDetector detector) {
		detector.removeListener(this);
	}

	@Override
	public void onCrashDetected(long lastStartup) throws Throwable {
		new Thread("Recovery") {

			@Override
			public void run() {
				LOG.info("Starting recovery of sources, sinks and queries ...");
				List<ISystemLogEntry> entries = syslog.read(lastStartup);
				if (entries.isEmpty()) {
					LOG.debug("No system sources, sinks or queries to recover");
					return;
				}
				List<IExecutorCommand> commands = determineCommandsToRecover(entries);
				recover(commands);
				recoverQueryStates();
				LOG.info("Recovery of sources, sinks and queries finished");
			};

		}.start();
	}

	@Override
	public void onSystemStartup() throws Throwable {
		// Nothing to do
	}

	@Override
	public void onSystemShutdown() throws Throwable {
		// Nothing to do
	}

	////////////
	// Backup //
	////////////

	/**
	 * Backup of an {@link IExecutorCommand}.
	 *
	 * @param tag
	 *            The tag for the system log.
	 * @param cmd
	 *            The command to serialize and backup.
	 * @param ts
	 *            The timestamp for the system log.
	 */
	private void backup(QueryStateLogTag tag, IExecutorCommand cmd, long ts) {
		this.syslog.write(tag.toString(), ts, ExecutorCommandSerializer.serialize(cmd), InstalledQueriesHandler.class);
	}

	@Override
	public void backup(AbstractDropStreamOrViewCommand cmd, long ts) {
		backup(QueryStateLogTag.SOURCE_REMOVED, cmd, ts);
	}

	@Override
	public void backup(DropSinkCommand cmd, long ts) {
		backup(QueryStateLogTag.SINK_REMOVED, cmd, ts);
	}

	@Override
	public void backup(AddQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERY_ADDED, cmd, ts);
	}

	@Override
	public void backup(RemoveQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERY_REMOVED, cmd, ts);
	}

	@Override
	public void backup(PartialQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERYSTATE_CHANGED, cmd, ts);
	}

	@Override
	public void backup(StartQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERYSTATE_CHANGED, cmd, ts);
	}

	@Override
	public void backup(StopQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERYSTATE_CHANGED, cmd, ts);
	}

	@Override
	public void backup(SuspendQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERYSTATE_CHANGED, cmd, ts);
	}

	@Override
	public void backup(ResumeQueryCommand cmd, long ts) {
		backup(QueryStateLogTag.QUERYSTATE_CHANGED, cmd, ts);
	}

	//////////////
	// Recovery //
	//////////////

	/**
	 * Mapping of AbstractQueryCommands to the id of the related query.
	 */
	private final Map<Integer, List<AbstractQueryCommand>> queryCommandMap = new HashMap<>();

	/**
	 * Determines the system log entries that are relevant: add/removal of
	 * sources, sinks and queries, and query state changes. The latter will not
	 * be returned but stored in {@link #queryStateChangesToRecover}.
	 * FIXME: At he moment the execution order is exactly as within the log. This is
	 * because we use AddQueryCommands for sources, sinks and queries. That
	 * makes it impossible(?) to map for example DropSinkCommands to an
	 * AddQueryCommand.
	 */
	private List<IExecutorCommand> determineCommandsToRecover(List<ISystemLogEntry> log) {
		// Collect executor commands to be executed.
		List<IExecutorCommand> executionOrder = new ArrayList<>();

		for (ISystemLogEntry entry : log) {
			if (QueryStateLogTag.containsTag(entry.getTag())) {
				QueryStateLogTag tag = QueryStateLogTag.fromString(entry.getTag()).get();
				IExecutorCommand command = ExecutorCommandSerializer.deserialize(entry.getInformation().get());
				switch (tag) {
				case SOURCE_REMOVED:
				case SINK_REMOVED:
				case QUERY_REMOVED:
					executionOrder.add(command);
					break;
				case QUERY_ADDED:
					AddQueryCommand addQueryCommand = (AddQueryCommand) command;
					AddQueryCommand modifiedCommand = new AddQueryCommand(addQueryCommand.getQueryText(),
							addQueryCommand.getParserId(), addQueryCommand.getCaller(),
							addQueryCommand.getTransCfgName(), addQueryCommand.getContext(),
							setRecoveryNeeded(addQueryCommand.getAddSettings()), addQueryCommand.startQueries());
					executionOrder.add(modifiedCommand);
					break;
				default: /* QUERYSTATE_CHANGED */
					executionOrder.add(command);
					AbstractQueryCommand queryChangeCommand = (AbstractQueryCommand) command;
					int queryId = queryChangeCommand.getQueryId().get();
					if (!this.queryCommandMap.containsKey(queryId)) {
						this.queryCommandMap.put(queryId, new ArrayList<>());
					}
					this.queryCommandMap.get(queryId).add(queryChangeCommand);
				}
			}
		}
		return executionOrder;
	}

	/**
	 * Inserts "recoveryneeded=true" as parameter for the
	 * {@code RecoveryConfigKeyword}.
	 *
	 * @param addSettings
	 *            The original additional settings.
	 * @return The enhanced additional settings.
	 */
	private static List<IQueryBuildSetting<?>> setRecoveryNeeded(List<IQueryBuildSetting<?>> addSettings) {
		for (IQueryBuildSetting<?> setting : addSettings) {
			if (ParameterRecoveryConfiguration.class.isInstance(setting)) {
				((ParameterRecoveryConfiguration) setting).addConfiguration("recoveryneeded", "true");
				break;
			}
		}
		return addSettings;
	}

	/**
	 * Executes a given list of commands, but with a new session of the original
	 * user.
	 */
	private void recover(List<IExecutorCommand> commands) {
		if (commands.isEmpty()) {
			return;
		}
		for (IExecutorCommand command : commands) {
			IUserManagementWritable userManagement = (IUserManagementWritable) UserManagementProvider.instance
					.getUsermanagement(true);
			ISession newSession = SessionManagement.instance.loginAs(command.getCaller().getUser().getName(),
					command.getCaller().getTenant(), SessionManagement.instance.loginSuperUser(null));
			if (command instanceof AddQueryCommand) {
				AddQueryCommand addCommand = (AddQueryCommand) command;
				executor.addQuery(addCommand.getQueryText(), addCommand.getParserId(), newSession,
						addCommand.getTransCfgName(), addCommand.getContext(), addCommand.getAddSettings());
				backup(addCommand, System.currentTimeMillis());
			} else {
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider.instance
						.getDataDictionary(newSession.getTenant());
				command.setCaller(newSession);
				command.execute(dd, userManagement, executor);
			}
		}
	}

	/**
	 * Recovery of the query states that means the query changes.
	 */
	private void recoverQueryStates() {
		/*
		 * FIXME There should be a way to not recover all query state changes,
		 * but to use shortcuts within a query state graph. The problem is that
		 * no complete graph is known and that the combination of current state
		 * and following query state change (AbstractQueryCommand) is not
		 * unique.
		 */
		for (int queryId : this.queryCommandMap.keySet()) {
			recover(new ArrayList<IExecutorCommand>(this.queryCommandMap.get(queryId)));
		}
		this.queryCommandMap.clear();
	}

}