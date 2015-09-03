package de.uniol.inf.is.odysseus.recovery.querystate;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
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

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferUtil;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractDropStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
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
import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISystemLog;
import de.uniol.inf.is.odysseus.recovery.systemstatelogger.ICrashDetectionListener;
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
@SuppressWarnings(value = { "nls" })
public class QueryStateRecoveryComponent implements IExecutorCommandListener, ICrashDetectionListener,
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
		// Is done by executorCommandEvent
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
		if (AbstractDropStreamOrViewCommand.class.isInstance(command)) {
			// Source dropped
			tag = QueryStateLogTag.SOURCE_REMOVED;
		} else if (CreateQueryCommand.class.isInstance(command)) {
			// New query
			// Note: There is also an AddQueryCommand, but the
			// CreateQueryCommand is the one returned by the parsers.
			tag = QueryStateLogTag.QUERY_ADDED;
		} else if (CreateSinkCommand.class.isInstance(command)) {
			// New sink
			tag = QueryStateLogTag.SINK_ADDED;
			String sinkName = ((CreateSinkCommand) command).getName();
			if (cKnownSinks.containsKey(caller)) {
				cKnownSinks.get(caller).add(sinkName);
			} else {
				cKnownSinks.put(caller, Sets.newHashSet(sinkName));
			}
		} else if (CreateStreamCommand.class.isInstance(command) || CreateViewCommand.class.isInstance(command)) {
			// New source
			tag = QueryStateLogTag.SOURCE_ADDED;
		} else if (DropSinkCommand.class.isInstance(command)) {
			// Sink dropped
			tag = QueryStateLogTag.SINK_REMOVED;
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

			if (CreateQueryCommand.class.isInstance(command)) {
				// it is not allowed to execute them directly
				recoverCreateQueryCommand((CreateQueryCommand) command, newSession, executor);
			} else {
				command.setCaller(newSession);
				command.execute(
						(IDataDictionaryWritable) DataDictionaryProvider.getDataDictionary(newSession.getTenant()),
						(IUserManagementWritable) UserManagementProvider.getUsermanagement(true), executor);
			}
		}
	}

	/**
	 * Recovers a {@code CreateQueryCommand}. <br />
	 * It needs to be handled in a special way, because it is not allowed to
	 * execute it direct.
	 * 
	 * @param command
	 *            The {@code CreateQueryCommand} to execute.
	 * @param caller
	 *            The new caller object for the command.
	 * @param executor
	 *            A present executor.
	 */
	private static void recoverCreateQueryCommand(CreateQueryCommand command, ISession caller,
			IServerExecutor executor) {
		String queryText = insertRecoveryNeededArgument(command.getQuery().getQueryText());
		executor.addQuery(queryText, command.getQuery().getParserId(), caller, Context.empty());
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
		 * the same query. Sort commands for each query, source, sink. Key =
		 * related query Value.e1 = first command (creation of query) Value.e2 =
		 * last command (last state change)
		 */
		Map<ILogicalQuery, Pair<IExecutorCommand, IExecutorCommand>> commandsPerQuery = Maps.newHashMap();
		Map<String, IExecutorCommand> commandPerSource = Maps.newHashMap();
		Map<String, IExecutorCommand> commandPerSink = Maps.newHashMap();
		List<IExecutorCommand> executionOrder = Lists.newArrayList();
		for (ISysLogEntry entry : log) {
			if (QueryStateLogTag.containsTag(entry.getTag())) {
				if (!entry.getInformation().isPresent()) {
					cLog.error("Additional information needed for '" + entry.getTag() + "' system log entries!");
					continue;
				}
				IExecutorCommand command = executorCommandFromBase64Binary(entry.getInformation().get());
				if (AbstractDropStreamOrViewCommand.class.isInstance(command)) {
					// Source dropped -> No command needs to be recovered for
					// that source
					String sourcename = ((AbstractDropStreamOrViewCommand) command).getName();
					IExecutorCommand commandToRemove = commandPerSource.get(sourcename);
					commandPerSource.remove(sourcename);
					executionOrder.remove(commandToRemove);
				} else if (CreateQueryCommand.class.isInstance(command)) {
					// New query -> First command for that query
					// Note: There is also an AddQueryCommand, but the
					// CreateQueryCommand is the one returned by the parsers.
					commandsPerQuery.put(((CreateQueryCommand) command).getQuery(),
							new Pair<IExecutorCommand, IExecutorCommand>(command, null));
					executionOrder.add(command);
				} else if (CreateSinkCommand.class.isInstance(command)) {
					// New sink -> First command for that sink
					commandPerSink.put(((CreateSinkCommand) command).getName(), command);
					executionOrder.add(command);
				} else if (CreateStreamCommand.class.isInstance(command)) {
					// New source -> First command for that source
					LinkedList<IExecutorCommand> cmds = new LinkedList<>();
					cmds.add(command);
					commandPerSource.put(((CreateStreamCommand) command).getName(), command);
					executionOrder.add(command);
				} else if (CreateViewCommand.class.isInstance(command)) {
					// New source -> First command for that source
					LinkedList<IExecutorCommand> cmds = new LinkedList<>();
					cmds.add(command);
					commandPerSource.put(((CreateViewCommand) command).getName(), command);
					executionOrder.add(command);
				} else if (DropSinkCommand.class.isInstance(command)) {
					// Sink dropped -> No command needs to be recovered for that
					// sink
					String sinkname = ((DropSinkCommand) command).getName();
					IExecutorCommand commandToRemove = commandPerSink.get(sinkname);
					commandPerSink.remove(sinkname);
					executionOrder.remove(commandToRemove);
				} else if (RemoveQueryCommand.class.isInstance(command)) {
					// Query removed -> No command needs to be recovered for
					// that
					// query
					Optional<ILogicalQuery> removedQuery = findQueryByName(commandsPerQuery.keySet(),
							((RemoveQueryCommand) command).getQueryName());
					if (removedQuery.isPresent()) {
						Optional<IExecutorCommand> firstCommand = Optional
								.fromNullable(commandsPerQuery.get(removedQuery.get()).getE1());
						if (firstCommand.isPresent()) {
							executionOrder.remove(firstCommand.get());
						}
						Optional<IExecutorCommand> lastCommand = Optional
								.fromNullable(commandsPerQuery.get(removedQuery.get()).getE2());
						if (lastCommand.isPresent()) {
							executionOrder.remove(lastCommand.get());
						}
						commandsPerQuery.remove(removedQuery.get());
					}
				} else if (AbstractQueryCommand.class.isInstance(command)) {
					// Query state changed
					Optional<ILogicalQuery> modifiedQuery = findQueryByName(commandsPerQuery.keySet(),
							((AbstractQueryCommand) command).getQueryName());
					if (modifiedQuery.isPresent()) {
						Optional<IExecutorCommand> lastCommand = Optional
								.fromNullable(commandsPerQuery.get(modifiedQuery.get()).getE2());
						if (lastCommand.isPresent()) {
							executionOrder.remove(lastCommand.get());
						}
						commandsPerQuery.get(modifiedQuery.get()).setE2(command);
						executionOrder.add(command);
					}
				}
			}
		}
		return executionOrder;
	}

	/**
	 * Find a query by its name.
	 * 
	 * @param queries
	 *            A collection of queries to search within.
	 * @param queryName
	 *            The name of the wanted query.
	 * @return An {@code Optional} of a query, which is within {@code queries}
	 *         and which's name is {@code queryName}, or
	 *         {@code Optional#absent()}, if there is none.
	 */
	private static Optional<ILogicalQuery> findQueryByName(Collection<ILogicalQuery> queries, String queryName) {
		for (ILogicalQuery query : queries) {
			if (query.getName().equals(queryName)) {
				return Optional.of(query);
			}
		}
		return Optional.absent();
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
		String toInsert = " " + AbstractRecoveryExecutor.RECOVERY_NEEDED_KEY + "=true";
		int keywordIndex = -1;
		String modifiedQueryText = queryText;
		while ((keywordIndex = modifiedQueryText.indexOf(RecoveryConfigKeyword.getName(), keywordIndex + 1)) != -1) {
			int endLineIndex = modifiedQueryText.indexOf("\n", keywordIndex) - 1;
			if (endLineIndex <= keywordIndex) {
				break;
			}
			StringBuffer out = new StringBuffer();
			out.append(modifiedQueryText.substring(0, endLineIndex));
			out.append(toInsert);
			out.append(modifiedQueryText.substring(endLineIndex));
			modifiedQueryText = out.toString();
		}
		return modifiedQueryText;
	}

}