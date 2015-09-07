package de.uniol.inf.is.odysseus.recovery.querystate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferUtil;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractCreateStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractDropStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.AbstractQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.PartialQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.RemoveQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.ResumeQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StopQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.SuspendQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.executorcommand.IExecutorCommandListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterRecoveryConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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
public class QueryStateRecoveryComponent implements IExecutorCommandListener, ISystemStateEventListener,
		IPlanModificationListener, IDataDictionaryListener {

	/**
	 * The logger for this class.
	 */
	static final Logger cLog = LoggerFactory.getLogger(QueryStateRecoveryComponent.class);

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

	@Override
	public void executorCommandEvent(final IExecutorCommand command) {
		backupExecutorCommand(command, System.currentTimeMillis());
	}

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
		backupExecutorCommand(command, System.currentTimeMillis());
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		// Is done by executorCommandEvent
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		backupExecutorCommand(new DropStreamCommand(name, true, getSessionForDD(sender).get()),
				System.currentTimeMillis());
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		ISession caller = getSessionForDD(sender).get();
		Optional<String> sink = getNewSinkFromDD(sender, caller);
		if (sink.isPresent()) {
			// IExecutorCommand is also delivered by executorCommandEvent
		} else {
			sink = getRemovedSinkFromDD(sender, caller);
			if (sink.isPresent()) {
				backupExecutorCommand(new DropSinkCommand(sink.get(), true, caller), System.currentTimeMillis());
				cKnownSinks.get(caller).remove(sink.get());
			}
		}
	}

	/**
	 * A mapping of all known sinks for each caller.
	 */
	private static final Map<ISession, Set<String>> cKnownSinks = Maps.newHashMap();

	/**
	 * Checks, if a new sink is added to a given data dictionary.
	 * 
	 * @param sender
	 *            The {@code IDataDictionary} to check.
	 * @param caller
	 *            The caller.
	 * @return An {@code Optional} of the sink name or {@code Optional#absent()}
	 *         , if there is no new sink.
	 */
	private static Optional<String> getNewSinkFromDD(IDataDictionary dd, ISession caller) {
		if (cKnownSinks.containsKey(caller)) {
			for (Entry<Resource, ILogicalOperator> sink : dd.getSinks(caller)) {
				String sinkName = sink.getKey().getResourceName();
				if (!cKnownSinks.get(caller).contains(sinkName)) {
					return Optional.of(sinkName);
				}
			}
		}
		return Optional.absent();
	}

	/**
	 * Checks, if a sink is removed from a given data dictionary.
	 * 
	 * @param sender
	 *            The {@code IDataDictionary} to check.
	 * @param caller
	 *            The caller.
	 * @return An {@code Optional} of the sink name or {@code Optional#absent()}
	 *         , if there is no sink removed.
	 */
	private static Optional<String> getRemovedSinkFromDD(IDataDictionary dd, ISession caller) {
		if (cKnownSinks.containsKey(caller)) {
			boolean foundSink;
			for (String sinkName : cKnownSinks.get(caller)) {
				foundSink = false;
				for (Entry<Resource, ILogicalOperator> sink : dd.getSinks(caller)) {
					if (sink.getKey().getResourceName().equals(sinkName)) {
						foundSink = true;
						break;
					}
				}
				if (!foundSink) {
					return Optional.of(sinkName);
				}
			}
		}
		return Optional.absent();
	}

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
	 * All known {@code IDataDictionaries}.
	 */
	private static final Set<IPair<ISession, IDataDictionary>> cKnownDDs = Sets.newHashSet();

	/**
	 * Gets the {@code IDataDictionary} for a given caller.
	 * 
	 * @param caller
	 *            The given caller.
	 * @return An {@code Optional} of the {@code IDataDictionary} for
	 *         {@code caller}, or {@code Optional#absent()}, if there is none.
	 */
	private static Optional<IDataDictionary> getDataDictionaryForCaller(ISession caller) {
		for (IPair<ISession, IDataDictionary> pair : cKnownDDs) {
			if (pair.getE1().equals(caller)) {
				return Optional.of(pair.getE2());
			}
		}
		return Optional.absent();
	}

	/**
	 * Gets the {@code ISession} for a given data dictionary.
	 * 
	 * @param dd
	 *            The given {@code IDataDictionary}.
	 * @return An {@code Optional} of the {@code ISession} for {@code dd}, or
	 *         {@code Optional#absent()}, if there is none.
	 */
	private static Optional<ISession> getSessionForDD(IDataDictionary dd) {
		for (IPair<ISession, IDataDictionary> pair : cKnownDDs) {
			if (pair.getE2().equals(dd)) {
				return Optional.of(pair.getE1());
			}
		}
		return Optional.absent();
	}

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
			serverExe.addPlanModificationListener(this);
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
			IServerExecutor serverExe = (IServerExecutor) executor;
			serverExe.removeExecutorCommandListener(this);
			serverExe.removePlanModificationListener(this);
			cExecutor = Optional.absent();
			for (IPair<ISession, IDataDictionary> dd : cKnownDDs) {
				dd.getE2().removeListener(this);
			}
		}
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
	 * The identifier to set for an {@code AddQueryCommand}. This is the query
	 * name, source name or sink name of the CreateCommand, which is executed
	 * immediately before the {@code AddQueryCommand}.
	 */
	private String mIdentifierForAddQueryCommand = null;

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
	private void backupExecutorCommand(IExecutorCommand command, long timestamp) {
		ISession caller = command.getCaller();
		if (!getDataDictionaryForCaller(caller).isPresent()) {
			IDataDictionary dd = cExecutor.get().getDataDictionary(caller);
			dd.addListener(this);
			cKnownDDs.add(new Pair<>(caller, dd));
		}

		QueryStateLogTag tag = null;
		if (AbstractCreateStreamOrViewCommand.class.isInstance(command)) {
			// New source.
			String sourceName = new Resource(command.getCaller().getUser(),
					((AbstractCreateStreamOrViewCommand) command).getName()).toString();
			tag = QueryStateLogTag.SOURCE_ADDED;
			this.mIdentifierForAddQueryCommand = sourceName;
		} else if (AbstractDropStreamOrViewCommand.class.isInstance(command)) {
			// Source dropped
			tag = QueryStateLogTag.SOURCE_REMOVED;
		} else if (CreateSinkCommand.class.isInstance(command)) {
			// New sink.
			String sinkName = new Resource(command.getCaller().getUser(), ((CreateSinkCommand) command).getName())
					.toString();
			if (cKnownSinks.containsKey(caller)) {
				cKnownSinks.get(caller).add(sinkName);
			} else {
				cKnownSinks.put(caller, Sets.newHashSet(sinkName));
			}
			tag = QueryStateLogTag.SINK_ADDED;
			this.mIdentifierForAddQueryCommand = sinkName;
		} else if (DropSinkCommand.class.isInstance(command)) {
			// Sink dropped
			tag = QueryStateLogTag.SINK_REMOVED;
		} else if (CreateQueryCommand.class.isInstance(command)) {
			// New query.
			String queryName = ((CreateQueryCommand) command).getQuery().getName();
			tag = QueryStateLogTag.QUERY_ADDED;
			this.mIdentifierForAddQueryCommand = queryName;
		} else if (PartialQueryCommand.class.isInstance(command) || RemoveQueryCommand.class.isInstance(command)
				|| ResumeQueryCommand.class.isInstance(command) || StartQueryCommand.class.isInstance(command)
				|| StopQueryCommand.class.isInstance(command) || SuspendQueryCommand.class.isInstance(command)) {
			// Query state changed
			tag = QueryStateLogTag.QUERYSTATE_CHANGED;
		} else if (AddQueryCommand.class.isInstance(command)) {
			// New Odysseus Script.
			((AddQueryCommand) command).setIdentifier(this.mIdentifierForAddQueryCommand);
			tag = QueryStateLogTag.SCRIPT_ADDED;
			this.mIdentifierForAddQueryCommand = null;
		}

		if (tag != null) {
			cSystemLog.get().write(tag.toString(), timestamp, executorCommandToBase64Binary(command),
					QueryStateRecoveryComponent.class);
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
			command.setCaller(newSession);
			command.execute((IDataDictionaryWritable) DataDictionaryProvider.getDataDictionary(newSession.getTenant()),
					(IUserManagementWritable) UserManagementProvider.getUsermanagement(true), executor);
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
		Map<String, AddQueryCommand> addQueryCommands = Maps.newHashMap();
		Map<String, IExecutorCommand> commandsPerQuery = Maps.newHashMap();
		List<IExecutorCommand> executionOrder = Lists.newArrayList();
		for (ISysLogEntry entry : log) {
			if (QueryStateLogTag.containsTag(entry.getTag())) {
				if (!entry.getInformation().isPresent()) {
					cLog.error("Additional information needed for '" + entry.getTag() + "' system log entries!");
					continue;
				}
				IExecutorCommand command = executorCommandFromBase64Binary(entry.getInformation().get());
				QueryStateLogTag tag = QueryStateLogTag.fromString(entry.getTag()).get();
				switch (tag) {
				case SOURCE_REMOVED:
					// Source dropped -> No command needs to be recovered for
					// that source
					executionOrder.remove(addQueryCommands.get(((AbstractDropStreamOrViewCommand) command).getName()));
					break;
				case SINK_REMOVED:
					// Sink dropped -> No command needs to be recovered for that
					// sink
					executionOrder.remove(addQueryCommands.get(((DropSinkCommand) command).getName()));
					break;
				case QUERYSTATE_CHANGED:
					String queryName = ((AbstractQueryCommand) command).getQueryName();
					if (RemoveQueryCommand.class.isInstance(command)) {
						// Query removed -> No command needs to be recovered for
						// that query
						executionOrder.remove(addQueryCommands.remove(queryName));
						executionOrder.remove(commandsPerQuery.remove(queryName));
					} else {
						// Query state changed
						executionOrder.remove(commandsPerQuery.get(queryName));
						commandsPerQuery.put(queryName, command);
						executionOrder.add(command);
					}
					break;
				case SCRIPT_ADDED:
					AddQueryCommand cmd = insertRecoveryNeededArgument((AddQueryCommand) command);
					addQueryCommands.put(cmd.getIdentifier(), cmd);
					executionOrder.add(cmd);
					break;
				default:
					// SOURCE_ADDED, SINK_ADDED, QUERY_ADDED
					// Nothing to do
					break;
				}
			}
		}
		return executionOrder;
	}

	/**
	 * Inserts "recoveryneeded=true" as key value argument for the used recovery
	 * executor into the script to be executed.
	 * 
	 * @param command
	 *            The {@code AddQueryCommand} to be executed.
	 * @return A modified command.
	 */
	private static AddQueryCommand insertRecoveryNeededArgument(AddQueryCommand command) {
		List<IQueryBuildSetting<?>> addSettings = Lists.newArrayList(command.getAddSettings());
		for (IQueryBuildSetting<?> setting : addSettings) {
			if (ParameterRecoveryConfiguration.class.isInstance(setting)) {
				ParameterRecoveryConfiguration recoveryParameter = (ParameterRecoveryConfiguration) setting;
				recoveryParameter.addConfiguration(AbstractRecoveryExecutor.RECOVERY_NEEDED_KEY, "true");
				break;
			}
		}
		AddQueryCommand out = new AddQueryCommand(command.getQueryText(), command.getParserId(), command.getCaller(),
				command.getTransCfgName(), command.getContext(), addSettings, command.startQueries());
		out.setIdentifier(command.getIdentifier());
		return out;
	}

}