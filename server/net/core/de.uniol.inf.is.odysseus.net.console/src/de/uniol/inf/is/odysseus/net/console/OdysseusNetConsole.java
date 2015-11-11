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

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.net.IOdysseusNetComponent;
import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetComponentStatus;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.config.OdysseusNetConfiguration;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnection;
import de.uniol.inf.is.odysseus.net.connect.IOdysseusNodeConnectionManager;
import de.uniol.inf.is.odysseus.net.ping.IPingMap;
import de.uniol.inf.is.odysseus.net.ping.IPingMapNode;
import de.uniol.inf.is.odysseus.net.resource.IOdysseusNodeResourceUsageManager;
import de.uniol.inf.is.odysseus.net.resource.IResourceUsage;

public class OdysseusNetConsole implements CommandProvider {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusNetConsole.class);
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();

	private static IServerExecutor executor;
	private static IOdysseusNodeManager nodeManager;
	private static IOdysseusNetStartupManager startupManager;
	private static IOdysseusNodeConnectionManager connectionManager;
	private static IPingMap pingMap;
	private static IOdysseusNodeResourceUsageManager resourceUsageManager;

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
		sb.append("    startOdysseusNet/stop...             - Starts/Stops OdysseusNet\n");
		sb.append("    showLocalNode                        - Shows data of own node\n");
		sb.append("\n");
		sb.append("    listNetConfiguration/ls...           - Prints the current odysseus net configuration\n");
		sb.append("    setNetConfiguration <key> <value>    - Sets key in the odysseus net configuration to spezified value\n");
		sb.append("    saveNetConfiguration                 - Saves thed current odysseus net configuration in the file\n");
		sb.append("    listNodes/ls..                       - Lists all found (!) nodes\n");
		sb.append("    listConnectedNodes/ls...             - Lists all connected nodes\n");
		sb.append("    isConnected <nodeID | nodeName>      - Checks, if the given node is connected to this node\n");
		sb.append("    ping                                 - Lists pings to all connected nodes\n");
		sb.append("    listPingPositions/ls...              - Lists the positions of the connected nodes in the ping map\n");
		sb.append("\n");
		sb.append("---Utility commands---\n");
		sb.append("    log <level> <text>             		- Creates a log statement\n");
		sb.append("    setLogger <logger> <level>     		- Sets the logging level of a specific logger\n");
		sb.append("    setLoggerOdysseus <logger> <level>	- Sets the logging level of a Odysseus-logger (de.uniol.inf.is.odysseus)\n");
		sb.append("    listLoggers/lsLoggers <filter> 		- Lists all known loggers by name\n");
		sb.append("\n");
		sb.append("    listSystemProperties/ls...     		- Lists all set system properties. Filter possible\n");
		sb.append("    setSystemProperty <name> <value>		- Sets system property.\n");
		sb.append("    listThreads/ls... <filter>     		- Lists all currently running threads. Filter possible\n");
		sb.append("\n");
		sb.append("    dumpPlan <queryid>                   - Prints the physical plan of the specified query.\n");
		sb.append("    dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis> - Prints the current stream of the specified operator.\n");
		sb.append("\n");

		return sb.toString();
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

	private static void setLoggerImpl(final CommandInterpreter ci, String loggerName, String logLevel, final int duration) {
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

			final DefaultStreamConnection<IStreamObject<?>> conn = new DefaultStreamConnection<IStreamObject<?>>(operator) {
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

		Collection<IPhysicalQuery> physicalQueries = executor.getExecutionPlan().getQueries();
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
		Collection<IPhysicalQuery> physicalQueries = executor.getExecutionPlan().getQueries();
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

		IPhysicalQuery query = executor.getExecutionPlan().getQueryById(queryID);
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

		for (AbstractPhysicalSubscription<? extends ISource<?>> source : sink.getSubscribedToSource()) {
			dumpPlan(source.getTarget(), depth + 1, b);
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
		ImmutableCollection<IOdysseusNode> foundNodes = nodeManager.getNodes();

		for (IOdysseusNode foundNode : foundNodes) {
			ci.print(foundNode);

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
		Collection<IOdysseusNode> selectedNodes = determineNode(node);
		if (selectedNodes.isEmpty()) {
			ci.println("There is no such node with '" + node + "'");
		} else if (selectedNodes.size() > 1) {
			ci.println("Identifiere is ambiguous:");
			for (IOdysseusNode selectedNode : selectedNodes) {
				ci.println("\t" + selectedNode);
			}
		} else {
			return Optional.of(selectedNodes.iterator().next());
		}

		return Optional.absent();
	}

	private static Collection<IOdysseusNode> determineNode(String nodeText) {
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
		ci.println("MEM: " + u.getMemFreeBytes() + " of " + u.getMemMaxBytes() + " Bytes free ( " + (((double) u.getMemFreeBytes() / u.getMemMaxBytes()) * 100.0) + " %)");
		ci.println("CPU: " + u.getCpuFree() + " of " + u.getCpuMax() + " free ( " + ((u.getCpuFree() / u.getCpuMax()) * 100.0) + " %)");
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
	
	public void _listOdysseusNetComponents( CommandInterpreter ci ) {
		if( !startupManager.isStarted() ) {
			ci.println("WARNING: OdysseusNet is not started!");
		}
		
		Collection<IOdysseusNetComponent> netComponents = startupManager.getComponents();
		List<String> output = Lists.newArrayList();
		for( IOdysseusNetComponent netComponent : netComponents ) {
			Optional<OdysseusNetComponentStatus> optStatus = startupManager.getComponentStatus(netComponent);
			if( optStatus.isPresent() ) {
				output.add(netComponent.getClass().getName() + ": " + optStatus.get());
			} else {
				output.add(netComponent.getClass().getName() + ": <unknown>");
			}
		}
		
		sortAndPrintList(ci, output);
	}
	
	public void _lsOdysseusNetComponents( CommandInterpreter ci ) {
		_listOdysseusNetComponents(ci);
	}
}