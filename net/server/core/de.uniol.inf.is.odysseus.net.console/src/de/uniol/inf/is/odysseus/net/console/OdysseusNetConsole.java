package de.uniol.inf.is.odysseus.net.console;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.console.executor.ConsoleCommandExecutionException;
import de.uniol.inf.is.odysseus.console.executor.ConsoleCommandNotFoundException;
import de.uniol.inf.is.odysseus.console.executor.IConsoleCommandExecutor;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentStatus;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.console.message.CommandMessage;
import de.uniol.inf.is.odysseus.net.console.message.CommandOutputMessage;
import de.uniol.inf.is.odysseus.net.console.message.LoginMessage;
import de.uniol.inf.is.odysseus.net.console.message.LoginOKMessage;
import de.uniol.inf.is.odysseus.net.console.message.LogoutMessage;
import de.uniol.inf.is.odysseus.net.console.message.LogoutOKMessage;
import de.uniol.inf.is.odysseus.net.ping.IPingMap;
import de.uniol.inf.is.odysseus.net.ping.IPingMapNode;
import de.uniol.inf.is.odysseus.net.resource.IOdysseusNodeResourceUsageManager;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.net.update.OdysseusNodeUpdater;

public class OdysseusNetConsole implements CommandProvider, IOdysseusNodeCommunicatorListener {

	static private final ISession currentUser = SessionManagement.instance.loginSuperUser(null);

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConsole.class);
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

	private static IServerExecutor executor;
	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNetStartupManager startupManager;
	private static IOdysseusNodeConnectionManager connectionManager;
	private static IPingMap pingMap;
	private static IOdysseusNodeResourceUsageManager resourceUsageManager;
	private static IOdysseusNodeCommunicator nodeCommunicator;
	private static IConsoleCommandExecutor consoleCommandExecutor;

	private final Collection<IOdysseusNode> loggedInNodes = Lists.newArrayList();
	private final Collection<IOdysseusNode> loggedToNodes = Lists.newArrayList();

	// called by OSGi-DS
	public static void bindServerExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindServerExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNetStartupManager(IOdysseusNetStartupManager serv) {
		startupManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNetStartupManager(IOdysseusNetStartupManager serv) {
		if (startupManager == serv) {
			startupManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		connectionManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeConnectionManager(IOdysseusNodeConnectionManager serv) {
		if (connectionManager == serv) {
			connectionManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindPingMap(IPingMap serv) {
		pingMap = serv;
	}

	// called by OSGi-DS
	public static void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
		}
	}

	// called by OSGi-DS
	public static void bindOdysseusNodeResourceUsageManager(IOdysseusNodeResourceUsageManager serv) {
		resourceUsageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeResourceUsageManager(IOdysseusNodeResourceUsageManager serv) {
		if (resourceUsageManager == serv) {
			resourceUsageManager = null;
		}
	}

	// called by OSGi-DS
	public void bindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		nodeCommunicator = serv;

		nodeCommunicator.registerMessageType(LoginMessage.class);
		nodeCommunicator.registerMessageType(LoginOKMessage.class);
		nodeCommunicator.registerMessageType(LogoutMessage.class);
		nodeCommunicator.registerMessageType(LogoutOKMessage.class);
		nodeCommunicator.registerMessageType(CommandMessage.class);
		nodeCommunicator.registerMessageType(CommandOutputMessage.class);
		nodeCommunicator.addListener(this, LoginMessage.class);
		nodeCommunicator.addListener(this, LoginOKMessage.class);
		nodeCommunicator.addListener(this, LogoutMessage.class);
		nodeCommunicator.addListener(this, LogoutOKMessage.class);
		nodeCommunicator.addListener(this, CommandMessage.class);
		nodeCommunicator.addListener(this, CommandOutputMessage.class);
	}

	// called by OSGi-DS
	public void unbindOdysseusNodeCommunicator(IOdysseusNodeCommunicator serv) {
		if (nodeCommunicator == serv) {
			nodeCommunicator.removeListener(this, CommandMessage.class);
			nodeCommunicator.removeListener(this, CommandOutputMessage.class);
			nodeCommunicator.removeListener(this, LoginMessage.class);
			nodeCommunicator.removeListener(this, LoginOKMessage.class);
			nodeCommunicator.removeListener(this, LogoutMessage.class);
			nodeCommunicator.removeListener(this, LogoutOKMessage.class);
			nodeCommunicator.unregisterMessageType(LoginMessage.class);
			nodeCommunicator.unregisterMessageType(LoginOKMessage.class);
			nodeCommunicator.unregisterMessageType(LogoutMessage.class);
			nodeCommunicator.unregisterMessageType(LogoutOKMessage.class);
			nodeCommunicator.unregisterMessageType(CommandMessage.class);
			nodeCommunicator.unregisterMessageType(CommandOutputMessage.class);

			nodeCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindConsoleCommandExecutor(IConsoleCommandExecutor serv) {
		consoleCommandExecutor = serv;
	}

	// called by OSGi-DS
	public static void unbindConsoleCommandExecutor(IConsoleCommandExecutor serv) {
		if (consoleCommandExecutor == serv) {
			consoleCommandExecutor = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Odysseus net console activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Odysseus net console deactivated");
	}

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();

		sb.append("---Odysseus net commands---\n");
		sb.append("    helpNet                              - Shows this help text.\n");
		sb.append("    startOdysseusNet/stop...             - Starts/Stops OdysseusNet\n");
		sb.append("    isOdysseusNetStarted                 - Shows if OdysseusNet is currently started\n");
		sb.append("    showLocalNode                        - Shows data of own node\n");
		sb.append("\n");
		sb.append("    listNetConfiguration/ls...           - Prints the current odysseus net configuration\n");
		sb.append(
				"    setNetConfiguration <key> <value>    - Sets key in the odysseus net configuration to spezified value\n");
		sb.append(
				"    saveNetConfiguration                 - Saves thed current odysseus net configuration in the file\n");
		sb.append("    listNodes/ls..                       - Lists all found (!) nodes\n");
		sb.append("    listConnectedNodes/ls...             - Lists all connected nodes\n");
		sb.append("    describeNode/descNode <nodeid|nodename> - Shows detailed info about the specified node\n");
		sb.append("    isConnected <nodeID | nodeName>      - Checks, if the given node is connected to this node\n");
		sb.append("    ping                                 - Lists pings to all connected nodes\n");
		sb.append(
				"    listPingPositions/ls...              - Lists the positions of the connected nodes in the ping map\n");
		sb.append("    resourceStatus                       - Prints current resource usage information\n");
		sb.append("    listOdysseusNetComponents/ls...      - Prints a list of all known OdysseusNet-components\n");
		sb.append("\n");
		sb.append("    loginNode <nodeID|nodename> <usr> <pass> - Logs in to specified node\n");
		sb.append("    logoutNode <nodeID|nodename> <usr> <pass> - Logs out from specified node\n");
		sb.append("    listLoggedInNodes/ls...              - Prints a list of all nodes which are logged in here\n");
		sb.append("    listLoggedToNodes/ls...              - Prints a list of all nodes which we are logged in to\n");
		sb.append("    revokeLogin <nodeID|nodename>        - Revokes the login of specified remote node\n");
		sb.append(
				"    loginStatus                          - Prints lists from 'lsLoggedInNodes' and 'lsLoggedToNodes'\n");
		sb.append(
				"    executeCommand <peerName> <cmd>      - Remotely execute a command on a peer. Outputs are send back. Login needed!\n");
		sb.append("    execCommand <peerName> <cmd>         - See executeCommand\n");
		sb.append("    execCmd <peerName> <cmd>             - See executeCommand\n");
		sb.append("\n");
		sb.append("---Utility commands---\n");
		sb.append("    log <level> <text>             		- Creates a log statement\n");
		sb.append("    setLogger <logger> <level>     		- Sets the logging level of a specific logger\n");
		sb.append(
				"    setLoggerOdysseus <logger> <level>	- Sets the logging level of a Odysseus-logger (de.uniol.inf.is.odysseus)\n");
		sb.append("    listLoggers/lsLoggers <filter> 		- Lists all known loggers by name\n");
		sb.append("\n");
		sb.append("    listSystemProperties/ls...     		- Lists all set system properties. Filter possible\n");
		sb.append("    setSystemProperty <name> <value>		- Sets system property.\n");
		sb.append("    listThreads/ls... <filter>     		- Lists all currently running threads. Filter possible\n");
		sb.append("\n");
		sb.append("    dumpPlan <queryid>                   - Prints the physical plan of the specified query.\n");
		sb.append(
				"    dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis> - Prints the current stream of the specified operator.\n");
		sb.append("\n");

		return sb.toString();
	}

	public void _helpNet(CommandInterpreter ci) {
		ci.println(getHelp());
	}

	public void _log(CommandInterpreter ci) {
		String logLevel = ci.nextArgument();
		if (Strings.isNullOrEmpty(logLevel)) {
			ci.println("usage: log <logLevel> <message>");
			return;
		}

		String text = ci.nextArgument();
		if (Strings.isNullOrEmpty(text)) {
			ci.println("usage: log <loglevel> <message>");
			return;
		}

		if (logLevel.equalsIgnoreCase("debug")) {
			LOG.debug(text);
		} else if (logLevel.equalsIgnoreCase("warn")) {
			LOG.warn(text);
		} else if (logLevel.equalsIgnoreCase("error")) {
			LOG.error(text);
		} else if (logLevel.equalsIgnoreCase("trace")) {
			LOG.trace(text);
		} else if (logLevel.equalsIgnoreCase("info")) {
			LOG.trace(text);
		} else {
			ci.println("Unknown loglevel! Valid: trace, info, debug, warn, error");
		}
	}

	public void _setLogger(CommandInterpreter ci) {
		String loggerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(loggerName)) {
			ci.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		String logLevel = ci.nextArgument();
		if (Strings.isNullOrEmpty(logLevel)) {
			ci.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		final int duration = tryToInt(ci.nextArgument());

		setLoggerImpl(ci, loggerName, logLevel, duration);
	}

	public void _setLoggerOdysseus(CommandInterpreter ci) {
		String loggerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(loggerName)) {
			ci.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		String logLevel = ci.nextArgument();
		if (Strings.isNullOrEmpty(logLevel)) {
			ci.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		final int duration = tryToInt(ci.nextArgument());

		setLoggerImpl(ci, "de.uniol.inf.is.odysseus." + loggerName, logLevel, duration);
	}

	private static void setLoggerImpl(final CommandInterpreter ci, String loggerName, String logLevel,
			final int duration) {
		org.apache.log4j.Level level = null;
		try {
			level = org.apache.log4j.Level.toLevel(logLevel.toUpperCase());
		} catch (Throwable t) {
			ci.println("Level '" + logLevel + "' is invalid.");
			return;
		}

		final org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(loggerName);
		final org.apache.log4j.Level prevLevel = logger.getLevel();
		logger.setLevel(level);

		ci.println("Set level of logger '" + loggerName + "' to '" + level.toString() + "'");

		if (duration > 0) {
			ci.println("Level will be set back after " + duration + " ms");

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(duration);
					} catch (InterruptedException e) {
					}

					logger.setLevel(prevLevel);
					ci.println("Set level of logger '" + logger.getName() + "' back");
				}
			});
			t.setDaemon(true);
			t.setName("Logger reset waiting");
			t.start();
		}
	}

	private static int tryToInt(String time) {
		if (Strings.isNullOrEmpty(time)) {
			return 0;
		}

		try {
			return Integer.valueOf(time);
		} catch (Throwable t) {
			return -1;
		}
	}

	public void _listLoggers(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		Enumeration<?> loggerNames = org.apache.log4j.LogManager.getCurrentLoggers();
		List<String> output = Lists.newLinkedList();
		while (loggerNames.hasMoreElements()) {
			org.apache.log4j.Logger elem = (org.apache.log4j.Logger) loggerNames.nextElement();

			if (elem.getLevel() != null) {
				if (Strings.isNullOrEmpty(filter) || (elem.getName().contains(filter))) {
					output.add(elem.getName() + " = " + elem.getLevel());
				}
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _lsLoggers(CommandInterpreter ci) {
		_listLoggers(ci);
	}

	public void _lsSystemProperties(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		Enumeration<Object> keys = System.getProperties().keys();
		List<String> output = Lists.newLinkedList();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			String keyName = key.toString();

			if (Strings.isNullOrEmpty(filter) || keyName.contains(filter)) {
				output.add(keyName + " = " + System.getProperty(keyName));
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _listSystemProperties(CommandInterpreter ci) {
		_lsSystemProperties(ci);
	}

	public void _setSystemProperty(CommandInterpreter ci) {
		String propName = ci.nextArgument();
		if (Strings.isNullOrEmpty(propName)) {
			ci.println("usage: setSystemProperty <propName> <value>");
			return;
		}

		String value = ci.nextArgument();
		if (Strings.isNullOrEmpty(value)) {
			ci.println("usage: setSystemProperty <propName> <value>");
			return;
		}

		System.setProperty(propName, value);
		ci.println("SystemProperty '" + propName + "' set to '" + value + "'");
	}

	public void _lsThreads(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		int threadCount = Thread.activeCount();
		Thread[] tarray = new Thread[threadCount];
		Thread.enumerate(tarray);

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();

		List<String> output = Lists.newLinkedList();
		for (Thread t : tarray) {
			long threadTimeNanos = bean.getThreadCpuTime(t.getId());
			String text = t.getName() + ": " + t.getState() + "[ " + threadTimeNanos + " ns ]";
			if (Strings.isNullOrEmpty(filter) || text.contains(filter)) {
				output.add(text);
			}
		}

		ci.println("Thread count: " + threadCount);
		sortAndPrintList(ci, output);
	}

	public void _listThreads(CommandInterpreter ci) {
		_lsThreads(ci);
	}

	public void _dumpStream(final CommandInterpreter ci) {
		String operatorHashString = ci.nextArgument();
		if (Strings.isNullOrEmpty(operatorHashString)) {
			ci.println("usage: dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis>");
			return;
		}

		int operatorHash = 0;
		String operatorName = "";
		try {
			operatorHash = Integer.valueOf(operatorHashString);
		} catch (Throwable t) {
			operatorName = operatorHashString;
		}

		String timeMillisString = ci.nextArgument();
		if (Strings.isNullOrEmpty(timeMillisString)) {
			ci.println("usage: dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis>");
			return;
		}

		int timeMillis = 0;
		try {
			timeMillis = Integer.valueOf(timeMillisString);
		} catch (Throwable t) {
			ci.println("usage: dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis>");
			return;
		}

		Optional<IPhysicalOperator> optOperator = findOperatorByHash(operatorHash);
		if (!optOperator.isPresent()) {
			optOperator = findOperatorByName(ci, operatorName);
		}

		if (optOperator.isPresent()) {
			final IPhysicalOperator operator = optOperator.get();
			ci.println("Connecting to physical operator " + operator);

			final DefaultStreamConnection<IStreamObject<?>> conn = new DefaultStreamConnection<IStreamObject<?>>(
					operator) {
				@Override
				public void process(IStreamObject<?> element, int port) {
					ci.println(element);
				}
			};
			conn.connect();

			final int ms = timeMillis;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
					}

					conn.disconnect();
					ci.println("Disconnect from physical operator " + operator);
				}
			});
			t.setDaemon(true);
			t.setName("Operatorconnection disconnect waiting");
			t.start();
		} else {
			ci.println("No physical operator with hash or name '" + operatorHash + "' found");
		}
	}

	private static Optional<IPhysicalOperator> findOperatorByName(CommandInterpreter ci, String operatorName) {
		int queryID = -1;

		int splitPos = operatorName.indexOf(":");
		if (splitPos != -1) {
			try {
				queryID = Integer.valueOf(operatorName.substring(0, splitPos));
				operatorName = operatorName.substring(splitPos + 1);
			} catch (Throwable t) {
			}
		}

		Collection<IPhysicalQuery> physicalQueries = executor.getExecutionPlan(currentUser).getQueries(currentUser);
		Collection<IPhysicalOperator> foundOperators = Lists.newArrayList();
		for (IPhysicalQuery physicalQuery : physicalQueries) {
			if (queryID == -1 || physicalQuery.getID() == queryID) {
				List<IPhysicalOperator> physicalOperators = physicalQuery.getPhysicalChilds();
				for (IPhysicalOperator physicalOperator : physicalOperators) {
					if (physicalOperator.getName().equals(operatorName)) {
						foundOperators.add(physicalOperator);
					}
				}
			}
		}

		if (foundOperators.size() == 1) {
			return Optional.of(foundOperators.iterator().next());
		} else if (!foundOperators.isEmpty()) {
			ci.println("Warning: Found multiple operators with name '" + operatorName + "': " + foundOperators);
			ci.println("Selecting first one");
			return Optional.of(foundOperators.iterator().next());
		}

		return Optional.absent();
	}

	private static Optional<IPhysicalOperator> findOperatorByHash(int operatorHash) {
		Collection<IPhysicalQuery> physicalQueries = executor.getExecutionPlan(currentUser).getQueries(currentUser);
		for (IPhysicalQuery physicalQuery : physicalQueries) {
			List<IPhysicalOperator> physicalOperators = physicalQuery.getPhysicalChilds();
			for (IPhysicalOperator physicalOperator : physicalOperators) {
				if (physicalOperator.hashCode() == operatorHash) {
					return Optional.of(physicalOperator);
				}
			}
		}

		return Optional.absent();
	}

	public void _dumpPlan(CommandInterpreter ci) {
		String queryIDString = ci.nextArgument();
		if (Strings.isNullOrEmpty(queryIDString)) {
			ci.println("usage: dumpPlan <queryid>");
			return;
		}

		int queryID = 0;
		try {
			queryID = Integer.valueOf(queryIDString);
		} catch (Throwable t) {
			ci.println("usage: dumpPlan <queryid>");
		}

		IPhysicalQuery query = executor.getExecutionPlan(currentUser).getQueryById(queryID, currentUser);
		if (query != null) {
			for (int i = 0; i < query.getRoots().size(); i++) {
				IPhysicalOperator curRoot = query.getRoots().get(i);

				StringBuffer sb = new StringBuffer();
				if (curRoot.isSink()) {
					dumpPlan((ISink<?>) curRoot, 0, sb);
				} else {
					dumpPlan((ISource<?>) curRoot, 0, sb);
				}
				ci.println("Physical plan of query: " + queryID);
				ci.println(sb.toString());
			}
		} else {
			ci.println("Query not found.");
		}
	}

	private static void dumpPlan(ISink<?> sink, int depth, StringBuffer b) {
		if (sink == null) {
			return;
		}
		for (int i = 0; i < depth; ++i) {
			b.append("\t");
		}

		b.append(sink.getClass().getSimpleName());
		b.append("(").append(sink.hashCode()).append(")");
		b.append("[").append(sink.getName()).append("]\n");

		for (AbstractPhysicalSubscription<? extends ISource<?>,?> source : sink.getSubscribedToSource()) {
			dumpPlan(source.getSource(), depth + 1, b);
		}
	}

	private static void dumpPlan(ISource<?> source, int depth, StringBuffer b) {
		if (source == null) {
			return;
		}
		if (source.isSink()) {
			dumpPlan((ISink<?>) source, depth, b);
		} else {
			for (int i = 0; i < depth; ++i) {
				b.append("\t");
			}
			b.append(source.getClass().getSimpleName());
			b.append("(").append(source.hashCode()).append(")");
			b.append("[").append(source.getName()).append("]\n");
		}
	}

	public void _lsNetConfiguration(CommandInterpreter ci) {
		Collection<String> configKeys = OdysseusNetConfiguration.getKeys();
		List<String> output = Lists.newLinkedList();
		for (String configKey : configKeys) {
			Optional<String> optValue = OdysseusNetConfiguration.get(configKey);
			if (optValue.isPresent()) {
				output.add(configKey + " = " + optValue.get());
			} else {
				output.add(configKey + " = <no value>");
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _listNetConfiguration(CommandInterpreter ci) {
		_lsNetConfiguration(ci);
	}

	private static void sortAndPrintList(CommandInterpreter ci, List<String> list) {
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (String line : list) {
				ci.println("\t" + line);
			}
		}
	}

	public void _setNetConfiguration(CommandInterpreter ci) {
		String key = ci.nextArgument();
		if (Strings.isNullOrEmpty(key)) {
			ci.println("usage: setNetConfiguration <key> <value>");
			return;
		}

		String value = ci.nextArgument();
		if (Strings.isNullOrEmpty(value)) {
			ci.println("usage: setNetConfiguration <key> <value>");
			return;
		}

		OdysseusNetConfiguration.set(key, value);
		ci.println("Set key '" + key + "' to '" + value + "'");
	}

	public void _saveNetConfiguration(CommandInterpreter ci) {
		OdysseusNetConfiguration.save();
		ci.println("Saved odysseus net configuration");
	}

	public void _lsNodes(CommandInterpreter ci) {
		String verboseArg = ci.nextArgument();
		boolean verbose;
		if (Strings.isNullOrEmpty(verboseArg)) {
			verbose = false;
		} else if (verboseArg.contentEquals("verbose")) {
			verbose = true;
		} else {
			verbose = Boolean.parseBoolean(verboseArg);
		}

		ImmutableCollection<IOdysseusNode> foundNodes = nodeManager.getNodes();

		for (IOdysseusNode foundNode : foundNodes) {
			ci.print(foundNode.toString(verbose));

			if (connectionManager.isConnected(foundNode)) {
				ci.println(" (connected)");
			} else {
				ci.println();
			}
		}
	}

	public void _listNodes(CommandInterpreter ci) {
		_lsNodes(ci);
	}

	public void _describeNode(CommandInterpreter ci) {
		String nodeText = ci.nextArgument();
		if (Strings.isNullOrEmpty(nodeText)) {
			ci.println("usage: describeNode <nodename | nodeid>");
			return;
		}

		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeText);
		if (optNode.isPresent()) {
			IOdysseusNode node = optNode.get();

			ci.println("Name                : " + node.getName());
			ci.println("ID                  : " + node.getID());
			ci.println("Connected           : " + connectionManager.isConnected(node));
			ci.println("Logged in from node : " + loggedInNodes.contains(node));
			ci.println("Logged to node      : " + loggedToNodes.contains(node));
			Optional<Double> optPing = pingMap.getPing(node);
			if (optPing.isPresent()) {
				ci.println("Ping                : " + optPing.get());
			} else {
				ci.println("No ping info available");
			}
			ci.println("Properties");
			for (String key : node.getProperyKeys()) {
				Optional<String> optValue = node.getProperty(key);
				if (optValue.isPresent()) {
					ci.println("\t" + key + " = " + optValue.get());
				} else {
					ci.println("\t" + key + " not set");
				}
			}

			try {
				Future<Optional<IResourceUsage>> futureOptUsage = resourceUsageManager.getRemoteResourceUsage(node,
						true);
				Optional<IResourceUsage> optUsage = futureOptUsage.get();
				if (optUsage.isPresent()) {
					IResourceUsage usage = optUsage.get();

					ci.println("Resource usage");
					ci.println("\tCPU               : Free : " + usage.getCpuMax() + " Max: " + usage.getCpuMax());
					ci.println("\tMEM               : Free : " + usage.getMemFreeBytes() + " Max: "
							+ usage.getMemMaxBytes());
					ci.println("\tNET               : In : " + usage.getNetInputRate() + " Out: "
							+ usage.getNetOutputRate() + " Max: " + usage.getNetBandwidthMax());
					ci.println("\tKnown remote nodes: " + usage.getRemoteNodeCount());
					ci.println("\tStartup ts        : " + usage.getStartupTimestamp() + " ["
							+ new Date(usage.getStartupTimestamp()) + "]");
					ci.println("\tRunning queries   : " + usage.getRunningQueriesCount());
					ci.println("\tStopped queries   : " + usage.getStoppedQueriesCount());
					ci.println("\tVersion           : " + toVersionString(usage.getVersion()));
				} else {
					ci.println("No resource usage info available");
				}
			} catch (InterruptedException | ExecutionException e) {
				ci.println("Could not determine resource usage");
			}

		}
	}

	public void _descNode(CommandInterpreter ci) {
		_describeNode(ci);
	}

	public void _startOdysseusNet(CommandInterpreter ci) {
		try {
			startupManager.start();
			ci.println("OdysseusNet started by console");
		} catch (OdysseusNetException e) {
			ci.println("Could not start OdysseusNet");
			ci.println(e);
		}
	}

	public void _stopOdysseusNet(CommandInterpreter ci) {
		startupManager.stop();

		ci.println("OdysseusNet stopped by console");
	}

	public void _isOdysseusNetStarted(CommandInterpreter ci) {
		if (startupManager.isStarted()) {
			ci.println("OdysseusNet is started");
		} else {
			ci.println("OdysseusNet is NOT started");
		}
	}

	public void _showLocalNode(CommandInterpreter ci) {
		try {
			IOdysseusNode localNode = startupManager.getLocalOdysseusNode();
			ci.println("NodeName\t= " + localNode.getName());
			ci.println("NodeID  \t= " + localNode.getID());
			for (String propertyKey : localNode.getProperyKeys()) {
				Optional<String> optPropertyValue = localNode.getProperty(propertyKey);
				ci.println(propertyKey + "\t= " + optPropertyValue.get());
			}
		} catch (OdysseusNetException e) {
			ci.println("OdysseusNet is not started");
		}
	}

	public void _listConnectedNodes(CommandInterpreter ci) {
		Collection<IOdysseusNodeConnection> connections = connectionManager.getConnections();
		for (IOdysseusNodeConnection connection : connections) {
			ci.println(connection);
		}
	}

	public void _lsConnectedNodes(CommandInterpreter ci) {
		_listConnectedNodes(ci);
	}

	public void _isConnected(CommandInterpreter ci) {
		String node = ci.nextArgument();
		if (Strings.isNullOrEmpty(node)) {
			ci.println("usage: isConnected <nodeName | nodeID>");
			return;
		}

		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, node);
		if (!optNode.isPresent()) {
			return;
		}

		if (connectionManager.isConnected(optNode.get())) {
			ci.println("Node " + optNode.get() + " is connected to us");
		} else {
			ci.println("Node " + optNode.get() + " is NOT connected to us");
		}
	}

	private static Optional<IOdysseusNode> determineFirstSelectedNode(CommandInterpreter ci, String node) {
		if (!startupManager.isStarted()) {
			ci.println("OdysseusNet not started");
			return Optional.absent();
		}

		Collection<IOdysseusNode> selectedNodes = determineNodes(node);
		if (selectedNodes.isEmpty()) {
			ci.println("There is no such node with '" + node + "'");
		} else if (selectedNodes.size() > 1) {
			ci.println("Identifier is ambiguous:");
			for (IOdysseusNode selectedNode : selectedNodes) {
				ci.println("\t" + selectedNode);
			}
		} else {
			return Optional.of(selectedNodes.iterator().next());
		}

		return Optional.absent();
	}

	private static Collection<IOdysseusNode> determineNodes(String nodeText) {
		Collection<IOdysseusNode> selectedNodes = Lists.newArrayList();
		ImmutableCollection<IOdysseusNode> nodes = nodeManager.getNodes();

		for (IOdysseusNode node : nodes) {
			if (node.getID().toString().equals(nodeText) || node.getName().equals(nodeText)) {
				selectedNodes.add(node);
			}
		}
		return selectedNodes;
	}

	public void _ping(CommandInterpreter ci) {
		Collection<IOdysseusNode> nodes = pingMap.getOdysseusNodes();
		ci.println("Current known ping(s):");

		List<String> output = Lists.newLinkedList();
		for (IOdysseusNode node : nodes) {
			Optional<Double> optPing = pingMap.getPing(node);
			if (optPing.isPresent()) {
				output.add(node + " : " + optPing.get());
			} else {
				output.add(node + " : <unknown>");
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _lsPingPositions(CommandInterpreter ci) {
		Collection<IPingMapNode> pingNodes = pingMap.getPingNodes();
		ci.println("Current known ping position(s):");

		List<String> output = Lists.newLinkedList();
		for (IPingMapNode pingNode : pingNodes) {
			output.add(pingNode.toString());
		}

		sortAndPrintList(ci, output);
	}

	public void _listPingPositions(CommandInterpreter ci) {
		_lsPingPositions(ci);
	}

	public void _resourceStatus(CommandInterpreter ci) {
		IResourceUsage u = resourceUsageManager.getLocalResourceUsage();

		ci.println("Version " + toVersionString(u.getVersion()));
		ci.println("Startup timestamp is " + convertTimestampToDate(u.getStartupTimestamp()));
		ci.println("MEM: " + u.getMemFreeBytes() + " of " + u.getMemMaxBytes() + " Bytes free ( "
				+ (((double) u.getMemFreeBytes() / u.getMemMaxBytes()) * 100.0) + " %)");
		ci.println("CPU: " + u.getCpuFree() + " of " + u.getCpuMax() + " free ( "
				+ ((u.getCpuFree() / u.getCpuMax()) * 100.0) + " %)");
		ci.println("NET: Max   = " + u.getNetBandwidthMax());
		ci.println("NET: Input = " + u.getNetInputRate());
		ci.println("NET: Output= " + u.getNetOutputRate());
		ci.println(u.getStoppedQueriesCount() + " queries stopped");
		ci.println(u.getRunningQueriesCount() + " queries running");
	}

	private static String toVersionString(int[] version) {
		return version[0] + "." + version[1] + "." + version[2] + "." + version[3];
	}

	private static String convertTimestampToDate(long startupTimestamp) {
		return DATE_FORMAT.format(new Date(startupTimestamp));
	}

	public void _listOdysseusNetComponents(CommandInterpreter ci) {
		if (!startupManager.isStarted()) {
			ci.println("WARNING: OdysseusNet is not started!");
		}

		Collection<IOdysseusNetComponent> netComponents = startupManager.getComponents();
		List<String> output = Lists.newArrayList();
		for (IOdysseusNetComponent netComponent : netComponents) {
			Optional<OdysseusNetComponentStatus> optStatus = startupManager.getComponentStatus(netComponent);
			if (optStatus.isPresent()) {
				output.add(netComponent.getClass().getName() + ": " + optStatus.get());
			} else {
				output.add(netComponent.getClass().getName() + ": <unknown>");
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _lsOdysseusNetComponents(CommandInterpreter ci) {
		_listOdysseusNetComponents(ci);
	}

	public void _reinstallNode(CommandInterpreter ci) {
		OdysseusNodeUpdater.doReinstall();
	}

	public void _updateNode(CommandInterpreter ci) {
		OdysseusNodeUpdater.doUpdate();
	}

	public void _restartNode(CommandInterpreter ci) {
		OdysseusNodeUpdater.doRestart();
	}

	public void _loginNode(CommandInterpreter ci) {
		String nodeText = ci.nextArgument();
		if (Strings.isNullOrEmpty(nodeText)) {
			ci.println("usage: login <nodename | nodeid> <username> <password>");
			return;
		}

		String username = ci.nextArgument();
		if (Strings.isNullOrEmpty(username)) {
			ci.println("usage: login <nodename | nodeid> <username> <password>");
			return;
		}

		String password = ci.nextArgument();
		if (Strings.isNullOrEmpty(password)) {
			ci.println("usage: login <nodename | nodeid> <username> <password>");
			return;
		}

		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeText);
		if (optNode.isPresent()) {
			doLogin(ci, username, password, optNode.get());
		}
	}

	private void doLogin(CommandInterpreter ci, String username, String password, IOdysseusNode node) {
		try {
			LoginMessage loginMsg = new LoginMessage(username, password);
			nodeCommunicator.send(node, loginMsg);
			ci.println("Send login to node " + node);
		} catch (OdysseusNodeCommunicationException e) {
			ci.println("Could not send login to node " + node + ": " + e.getMessage());
		}
	}

	public void _logoutNode(CommandInterpreter ci) {
		String nodeText = ci.nextArgument();
		if (Strings.isNullOrEmpty(nodeText)) {
			ci.println("usage: logout <nodename | nodeid>");
			return;
		}

		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeText);
		if (optNode.isPresent()) {
			doLogout(ci, optNode.get());
		}
	}

	private void doLogout(CommandInterpreter ci, IOdysseusNode node) {
		if (loggedToNodes.contains(node)) {
			try {
				nodeCommunicator.send(node, new LogoutMessage());
			} catch (OdysseusNodeCommunicationException e) {
				LOG.warn("Could not send logout message to node {}", node, e);
			}
		} else {
			ci.println("Not logged in node " + node);
		}
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		if (message instanceof CommandMessage) {
			if (!loggedInNodes.contains(senderNode)) {
				return;
			}

			CommandMessage cmd = (CommandMessage) message;
			processCommandMessage(senderNode, cmd);
		} else if (message instanceof CommandOutputMessage) {
			CommandOutputMessage cmd = (CommandOutputMessage) message;
			processCommandOutputMessage(senderNode, cmd);
		} else if (message instanceof LoginMessage) {
			if (loggedInNodes.contains(senderNode)) {
				sendLoginOKMessage(senderNode);
				LOG.debug("Node {} already logged in", senderNode);
				return;
			}

			processLoginMessage(senderNode, message);
		} else if (message instanceof LoginOKMessage) {
			loggedToNodes.add(senderNode);
			LOG.info("Login to node '" + senderNode + "' ok");
		} else if (message instanceof LogoutMessage) {
			loggedInNodes.remove(senderNode);
			LOG.debug("Node '{}' logged out", senderNode);

			sendLogoutOKMessage(senderNode);
		} else if (message instanceof LogoutOKMessage) {
			loggedToNodes.remove(senderNode);
			LOG.info("Logout from node '" + senderNode + "' ok");
		}
	}

	private static void sendLogoutOKMessage(IOdysseusNode senderNode) {
		LogoutOKMessage msg = new LogoutOKMessage();
		try {
			nodeCommunicator.send(senderNode, msg);
		} catch (OdysseusNodeCommunicationException e) {
			LOG.error("Could not send logoutOK message to node '{}'", senderNode, e);
		}
	}

	private void processLoginMessage(IOdysseusNode senderNode, IMessage message) {
		LoginMessage loginMsg = (LoginMessage) message;
		ISession session = executor.login(loginMsg.getUsername(), loginMsg.getPassword().getBytes());
		if (session != null) {
			loggedInNodes.add(senderNode);
			sendLoginOKMessage(senderNode);
		}
	}

	private static void sendLoginOKMessage(IOdysseusNode senderNode) {
		LoginOKMessage okMsg = new LoginOKMessage();
		try {
			nodeCommunicator.send(senderNode, okMsg);
		} catch (OdysseusNodeCommunicationException e) {
			LOG.error("Could not send ok message to node {}", senderNode, e);
		}
	}

	private void processCommandMessage(IOdysseusNode senderNode, CommandMessage cmd) {
		try {
			String outputString = consoleCommandExecutor.executeConsoleCommand(cmd.getCommandString());
			CommandOutputMessage out = new CommandOutputMessage(outputString);
			try {
				LOG.debug("Command executed. Send results back");
				nodeCommunicator.send(senderNode, out);
			} catch (OdysseusNodeCommunicationException e) {
				LOG.debug("Could not send console output", e);
			}
		} catch (ConsoleCommandNotFoundException | ConsoleCommandExecutionException e1) {
		}
	}

	private void processCommandOutputMessage(IOdysseusNode senderNode, CommandOutputMessage cmd) {
		System.out.println("Output from '" + senderNode + "':");
		System.out.println(cmd.getOutput());
	}

	public void _lsLoggedInNodes(CommandInterpreter ci) {
		if (loggedInNodes.isEmpty()) {
			ci.println("No remote nodes are logged in here");
			return;
		}

		List<String> output = Lists.newArrayList();
		for (IOdysseusNode loggedInNode : loggedInNodes) {
			output.add(loggedInNode.toString());
		}

		ci.println("Following remote nodes are logged in here:");
		sortAndPrintList(ci, output);
	}

	public void _listLoggedInNodes(CommandInterpreter ci) {
		_lsLoggedInNodes(ci);
	}

	public void _lsLoggedToNodes(CommandInterpreter ci) {
		if (loggedToNodes.isEmpty()) {
			ci.println("We are not logged in to any remote node");
			return;
		}

		List<String> output = Lists.newArrayList();
		for (IOdysseusNode loggedInNode : loggedToNodes) {
			output.add(loggedInNode.toString());
		}

		ci.println("Following remote nodes we are logged in:");
		sortAndPrintList(ci, output);
	}

	public void _listLoggedToNodes(CommandInterpreter ci) {
		_lsLoggedToNodes(ci);
	}

	public void _revokeLogin(CommandInterpreter ci) {
		String nodeText = ci.nextArgument();
		if (Strings.isNullOrEmpty(nodeText)) {
			ci.println("usage: revokeLogin <nodeid | nodename>");
			return;
		}

		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeText);
		if (optNode.isPresent()) {
			IOdysseusNode node = optNode.get();
			if (loggedInNodes.contains(node)) {
				loggedInNodes.remove(node);
				sendLogoutOKMessage(node);
				ci.println("Login of node '" + node + "' revoked");
			} else {
				ci.println("Node '" + node + "' is not logged in here");
			}
		}
	}

	public void _loginStatus(CommandInterpreter ci) {
		_lsLoggedInNodes(ci);
		_lsLoggedToNodes(ci);
	}

	public void _executeCommand(CommandInterpreter ci) {
		String nodeText = ci.nextArgument();
		if (Strings.isNullOrEmpty(nodeText)) {
			ci.println("usage: executeCommand <nodeID | nodename> <command>");
			return;
		}

		String command = ci.nextArgument();
		if (Strings.isNullOrEmpty(command)) {
			ci.println("usage: executeCommand <nodeID | nodename> <command>");
			return;
		}

		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeText);
		if (optNode.isPresent()) {
			remoteExecuteCommand(ci, command, optNode.get());
		} else {
			ci.println("Node '" + nodeText + "' not known.");
		}
	}

	private void remoteExecuteCommand(CommandInterpreter ci, String command, IOdysseusNode node) {
		if (loggedToNodes.contains(node)) {
			sendCommandMessage(ci, node, command);
		} else {
			ci.println("Not logged in node '" + node + "'");
		}
	}

	public void _execCommand(CommandInterpreter ci) {
		_executeCommand(ci);
	}

	public void _execCmd(CommandInterpreter ci) {
		_executeCommand(ci);
	}

	public void _execCmdAll(CommandInterpreter ci) {
		String username = ci.nextArgument();
		if (Strings.isNullOrEmpty(username)) {
			ci.println("usage: execCmdAll <username> <password> <cmd>");
			return;
		}

		String password = ci.nextArgument();
		if (Strings.isNullOrEmpty(password)) {
			ci.println("usage: execCmdAll <username> <password> <cmd>");
			return;
		}

		String command = ci.nextArgument();
		if (Strings.isNullOrEmpty(command)) {
			ci.println("usage: execCmdAll <username> <password> <cmd>");
			return;
		}

		Collection<IOdysseusNode> nodes = nodeCommunicator.getDestinationNodes();

		ci.println("Sending logins...");
		for (IOdysseusNode node : nodes) {
			ci.println("Login to " + node);
			doLogin(ci, username, password, node);
		}

		ci.println("Waiting 2 seconds...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		ci.println("Executing command");
		for (IOdysseusNode node : nodes) {
			remoteExecuteCommand(ci, command, node);
		}

		ci.println("Waiting 2 seconds...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		ci.println("Sending logins...");
		for (IOdysseusNode node : nodes) {
			ci.println("Logout from " + node);
			doLogout(ci, node);
		}

		ci.println("ExecCmdAll finished");
	}

	private static void sendCommandMessage(CommandInterpreter ci, IOdysseusNode destinationNode, String command) {
		CommandMessage cmd = new CommandMessage(command);
		try {
			nodeCommunicator.send(destinationNode, cmd);
			ci.println("Command send to node '" + destinationNode + "'");
		} catch (OdysseusNodeCommunicationException e) {
			ci.println("Could not send command to node '" + destinationNode + "': " + e.getMessage());
		}
	}

	public void _remoteUpdate(CommandInterpreter ci) {
		String nodeStr = ci.nextArgument();
		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeStr);
		if (optNode.isPresent()) {
			OdysseusNodeUpdater.sendUpdateMessageToRemoteNodes(Lists.newArrayList(optNode.get()));
		}
	}

	public void _remoteUpdateAll(CommandInterpreter ci) {
		OdysseusNodeUpdater.sendUpdateMessageToRemoteNodes();
	}

	public void _remoteReinstall(CommandInterpreter ci) {
		String nodeStr = ci.nextArgument();
		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeStr);
		if (optNode.isPresent()) {
			OdysseusNodeUpdater.sendUpdateMessageToRemoteNodes(Lists.newArrayList(optNode.get()));
		}
	}

	public void _remoteReinstallAll(CommandInterpreter ci) {
		OdysseusNodeUpdater.sendReinstallMessageToRemoteNodes();
	}

	public void _remoteRestart(CommandInterpreter ci) {
		String nodeStr = ci.nextArgument();
		Optional<IOdysseusNode> optNode = determineFirstSelectedNode(ci, nodeStr);
		if (optNode.isPresent()) {
			OdysseusNodeUpdater.sendRestartMessageToRemoteNodes(Lists.newArrayList(optNode.get()));
		}
	}

	public void _remoteRestartAll(CommandInterpreter ci) {
		OdysseusNodeUpdater.sendRestartMessageToRemoteNodes();
	}
}
