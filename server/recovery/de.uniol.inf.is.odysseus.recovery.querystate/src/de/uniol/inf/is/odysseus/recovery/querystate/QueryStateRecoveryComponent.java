package de.uniol.inf.is.odysseus.recovery.querystate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferUtil;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractCreateStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractDropStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.AbstractQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.PartialQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.RemoveQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.ResumeQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StopQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.SuspendQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.executorcommand.IExecutorCommandListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterRecoveryConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ISystemStateEventListener;

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
@SuppressWarnings(value = { "nls" })
public class QueryStateRecoveryComponent implements IExecutorCommandListener, ISystemStateEventListener {

	/**
	 * The logger for this class.
	 */
	static final Logger cLog = LoggerFactory.getLogger(QueryStateRecoveryComponent.class);

	/**
	 * The system log, if bound.
	 */
	static Optional<ISystemLog> cSystemLog = Optional.absent();

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
	 * The {@code PlanModificationListener} to use.
	 */
	private static final PlanModificationListener cPlanModListener = new PlanModificationListener();

	/**
	 * All subscribed {@code DataDictionaryProviderListeners}.
	 */
	private static final Set<DataDictionaryProviderListener> cDDProviderListeners = Sets.newHashSet();

	/**
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			IServerExecutor serverExe = (IServerExecutor) executor;
			serverExe.addExecutorCommandListener(this);
			serverExe.addPlanModificationListener(cPlanModListener);
			cExecutor = Optional.of((IServerExecutor) executor);
			subscribeForDataDictionaries();
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
			IServerExecutor serverExe = (IServerExecutor) executor;
			serverExe.removeExecutorCommandListener(this);
			serverExe.removePlanModificationListener(cPlanModListener);
			cExecutor = Optional.absent();
			unsubscribeForDataDictionaries();
		}
	}

	/**
	 * Subscribes {@code DataDictionaryProvider} for all known {@code ITenants}.
	 */
	private static void subscribeForDataDictionaries() {
		for (ITenant tenant : UserManagementProvider.getTenants()) {
			Optional<IDataDictionary> dd = Optional.fromNullable(DataDictionaryProvider.getDataDictionary(tenant));
			DataDictionaryProviderListener listener;
			if (dd.isPresent()) {
				listener = new DataDictionaryProviderListener(dd.get());
			} else {
				listener = new DataDictionaryProviderListener();
			}
			DataDictionaryProvider.subscribe(tenant, listener);
			cDDProviderListeners.add(listener);
		}
	}

	/**
	 * Unsubscribes all {@code DataDictionaryProviderListeners}.
	 */
	private static void unsubscribeForDataDictionaries() {
		for (DataDictionaryProviderListener listener : cDDProviderListeners) {
			DataDictionaryProvider.unsubscribe(listener);
		}
	}

	///////////////////
	// Backup mode //
	///////////////////

	@Override
	public void executorCommandEvent(final IExecutorCommand command) {
		if (AbstractCreateStreamOrViewCommand.class.isInstance(command)
				|| AbstractDropStreamOrViewCommand.class.isInstance(command)
				|| CreateSinkCommand.class.isInstance(command) || DropSinkCommand.class.isInstance(command)) {
			// Done by DataDictionaryListener
			return;
		}
		backupExecutorCommand(command, System.currentTimeMillis());
	}

	/**
	 * Encode an {@code IExecutorCommand} to a Base64Binary.
	 * 
	 * @param command
	 *            The command to encode.
	 * @return A string representing the binary.
	 */
	private static String executorCommandToBase64Binary(IExecutorCommand command) {
		try {
			return new String(DatatypeConverter.printBase64Binary(ByteBufferUtil.toByteArray(command)));
		} catch (IOException e) {
			cLog.error("Info is not serializable!", e);
			return null;
		}
	}

	/**
	 * The last seen {@code CreateQueryCommand} needed for the upcoming
	 * {@code AddQueryCommand}.
	 */
	/*
	 * XXX Problem with recovering queries: CreateQueryCommand has the logical
	 * query, where recovery settings could be set in backup mode. But they can
	 * not be executed. So all settings are lost, which are not within the query
	 * text.
	 */
	private static Optional<CreateQueryCommand> cLastCreateQueryCommand = Optional.absent();

	/**
	 * Backups a new {@code IExecutorCommand}, which can be an added source,
	 * sink or query, or a removed source or sink, or the change of a query
	 * state.
	 * 
	 * @param command
	 *            The {@code IExecutorCommand} to save.
	 * @param timestamp
	 *            The time stamp for the log entry.
	 */
	static void backupExecutorCommand(IExecutorCommand command, long timestamp) {
		QueryStateLogTag tag = null;
		if (AbstractCreateStreamOrViewCommand.class.isInstance(command)) {
			// New source.
			tag = QueryStateLogTag.SOURCE_ADDED;
		} else if (AbstractDropStreamOrViewCommand.class.isInstance(command)) {
			// Source dropped
			tag = QueryStateLogTag.SOURCE_REMOVED;
		} else if (CreateSinkCommand.class.isInstance(command)) {
			// New sink.
			tag = QueryStateLogTag.SINK_ADDED;
		} else if (DropSinkCommand.class.isInstance(command)) {
			// Sink dropped
			tag = QueryStateLogTag.SINK_REMOVED;
		} else if (CreateQueryCommand.class.isInstance(command)) {
			// New query.
			cLastCreateQueryCommand = Optional.of((CreateQueryCommand) command);
		} else if (AddQueryCommand.class.isInstance(command) && cLastCreateQueryCommand.isPresent()) {
			// New query.
			((AddQueryCommand) command).setIdentifier(cLastCreateQueryCommand.get().getQuery().getName());
			tag = QueryStateLogTag.QUERY_ADDED;
			cLastCreateQueryCommand = Optional.absent();
		} else if (PartialQueryCommand.class.isInstance(command) || RemoveQueryCommand.class.isInstance(command)
				|| ResumeQueryCommand.class.isInstance(command) || StartQueryCommand.class.isInstance(command)
				|| StopQueryCommand.class.isInstance(command) || SuspendQueryCommand.class.isInstance(command)) {
			// Query state changed
			tag = QueryStateLogTag.QUERYSTATE_CHANGED;
		}

		if (tag != null) {
			cSystemLog.get().write(tag.toString(), timestamp, executorCommandToBase64Binary(command),
					QueryStateRecoveryComponent.class);
		}
	}

	///////////////////
	// Recovery mode //
	///////////////////

	/**
	 * Recovery will be started.
	 */
	@Override
	public void onCrashDetected(final long lastStartup) throws Throwable {
		if (!cSystemLog.isPresent()) {
			cLog.error("Could not start recovery, because no system log is bound!");
		} else if (!cExecutor.isPresent()) {
			cLog.error("Could not start recovery, because no executor is bound!");
		} else {
			cLog.debug("Starting recovery...");
			recoverFromLog(cSystemLog.get().read(lastStartup), cExecutor.get());
			cLog.debug("Recovery finished.");
		}
	}

	@Override
	public void onSystemStartup() throws Throwable {
		// Nothing to do.
	}

	@Override
	public void onSystemShutdown() throws Throwable {
		// Nothing to do.
	}

	/**
	 * Decode an {@code IExecutorCommand} from a Base64Binary.
	 * 
	 * @param str
	 *            A string representing the binary.
	 * @return The decoded command.
	 */
	private static IExecutorCommand executorCommandFromBase64Binary(String str) {
		try {
			return (IExecutorCommand) ByteBufferUtil.fromByteArray(DatatypeConverter.parseBase64Binary(str));
		} catch (IOException e) {
			cLog.error("Executor command is not serializable!", e);
			return null;
		} catch (ClassNotFoundException e) {
			cLog.error("Unknown class for decoding!", e);
			return null;
		}
	}

	/**
	 * Starts the recovery process.
	 * 
	 * @param log
	 *            All system log entries to recover.
	 * @param executor
	 *            A present executor.
	 */
	private static void recoverFromLog(List<ISysLogEntry> log, IServerExecutor executor) {
		List<IExecutorCommand> executionOrder = determineExecutionOrder(log);
		for (IExecutorCommand command : executionOrder) {
			// Session is expired. Need a new session for the user of the
			// original command
			IUserManagementWritable userManagement = (IUserManagementWritable) UserManagementProvider
					.getUsermanagement(true);
			ISession newSession = userManagement.getSessionManagement().loginAs(command.getCaller().getUser().getName(),
					command.getCaller().getTenant(), userManagement.getSessionManagement().loginSuperUser(null));

			if (AddQueryCommand.class.isInstance(command)) {
				// If the command would be executed normally, there would be no
				// CreateQueryCommand and backupExecutorCommand could not
				// identify the query for the AddQueryCommand
				AddQueryCommand addCommand = (AddQueryCommand) command;
				executor.addQuery(addCommand.getQueryText(), addCommand.getParserId(), newSession,
						addCommand.getTransCfgName(), addCommand.getContext(), addCommand.getAddSettings());
				backupExecutorCommand(addCommand, System.currentTimeMillis());
			} else {
				IDataDictionaryWritable dd = (IDataDictionaryWritable) DataDictionaryProvider
						.getDataDictionary(newSession.getTenant());
				command.setCaller(newSession);
				command.execute(dd, userManagement, executor);
			}
		}
	}

	/**
	 * Determines those {@code IExecutorCommands}, which have to be recovered
	 * and their order.
	 * 
	 * @param log
	 *            All {@code ISysLogEntries} since the last startup.
	 * @return A list of {@code IExecutorCommands}, which have to be recovered.
	 *         Order is important.
	 */
	private static List<IExecutorCommand> determineExecutionOrder(List<ISysLogEntry> log) {
		/*
		 * Not all events have to be recovered, e.g., several state changes of
		 * the same query. Sort commands for each query, source, sink.
		 */
		Map<Resource, Pair<IExecutorCommand, IExecutorCommand>> commandsPerQuery = Maps.newHashMap();
		Map<String, IExecutorCommand> createCommandPerSource = Maps.newHashMap();
		Map<String, IExecutorCommand> createCommandPerSink = Maps.newHashMap();
		List<IExecutorCommand> executionOrder = Lists.newArrayList();
		for (ISysLogEntry entry : log) {
			if (QueryStateLogTag.containsTag(entry.getTag())) {
				if (!entry.getInformation().isPresent()) {
					cLog.error("Additional information needed for '" + entry.getTag() + "' system log entries!");
					continue;
				}
				IExecutorCommand command = executorCommandFromBase64Binary(entry.getInformation().get());
				QueryStateLogTag tag = QueryStateLogTag.fromString(entry.getTag()).get();
				String resourceName;
				switch (tag) {
				case SOURCE_ADDED:
					// Source added
					// Source name is not the full qualified resource name,
					// because that is not allowed for create commands
					resourceName = new Resource(command.getCaller().getUser(),
							((AbstractCreateStreamOrViewCommand) command).getName()).toString();
					createCommandPerSource.put(resourceName, command);
					executionOrder.add(command);
					break;
				case SOURCE_REMOVED:
					// Source dropped -> No command needs to be recovered for
					// that source
					resourceName = ((AbstractDropStreamOrViewCommand) command).getName();
					executionOrder.remove(createCommandPerSource.remove(resourceName));
					break;
				case SINK_ADDED:
					// Sink added
					resourceName = ((CreateSinkCommand) command).getName();
					createCommandPerSink.put(resourceName, command);
					executionOrder.add(command);
					break;
				case SINK_REMOVED:
					// Sink dropped -> No command needs to be recovered for that
					// sink
					resourceName = ((DropSinkCommand) command).getName();
					executionOrder.remove(createCommandPerSink.remove(resourceName));
					break;
				case QUERY_ADDED:
					// Query added
					AddQueryCommand addCommand = (AddQueryCommand) command;
					AddQueryCommand modifiedCommand = new AddQueryCommand(addCommand.getQueryText(),
							addCommand.getParserId(), addCommand.getCaller(), addCommand.getTransCfgName(),
							addCommand.getContext(), setRecoveryNeeded(addCommand.getAddSettings()),
							addCommand.startQueries());
					modifiedCommand.setIdentifier(addCommand.getIdentifier());
					commandsPerQuery.put(modifiedCommand.getIdentifier(),
							new Pair<IExecutorCommand, IExecutorCommand>(modifiedCommand, null));
					executionOrder.add(modifiedCommand);
					break;
				case QUERYSTATE_CHANGED:
					Resource queryName = ((AbstractQueryCommand) command).getQueryName();
					if (!commandsPerQuery.containsKey(queryName)) {
						// FIXME There is a probblem, if the Odysseus Script
						// keyword #RUNQUERY is used. Because than the following
						// commands are provided by the executor in the
						// following order: (1) CreateQuery, (2) StartQuery, (3)
						// AddQuery. For recovery, it is a problem, that Start
						// comes before Add. The reason for that order is, that
						// the StartQuery command is created and executed as a
						// part of the AddQueryCommand, which is fired after its
						// execution.
						cLog.error("Could not recovery query state change '{}'", command);
						break;
					}
					if (commandsPerQuery.get(queryName).getE2() != null) {
						executionOrder.remove(commandsPerQuery.get(queryName).getE2());
					}
					if (RemoveQueryCommand.class.isInstance(command)) {
						// Query removed -> No command needs to be recovered for
						// that query
						executionOrder.remove(commandsPerQuery.get(queryName).getE1());
						commandsPerQuery.remove(queryName);
					} else {
						// Query state changed
						commandsPerQuery.get(queryName).setE2(command);
						executionOrder.add(command);
					}
					break;
				default:
					// Unknown tag
					break;
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

}