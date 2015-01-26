package de.uniol.inf.is.odysseus.peer.console;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
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
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.IPeerReachabilityService;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.PeerReachabilityInfo;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.config.InetAddressUtil;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.JxtaLoggingDestinations;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.peer.update.PeerUpdatePlugIn;

public class PeerConsole implements CommandProvider, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerConsole.class);
	private static final Collection<CommandProvider> COMMAND_PROVIDERS = Lists.newArrayList();
	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);

	private static IP2PDictionary p2pDictionary;
	private static IPeerDictionary peerDictionary;
	private static IPeerResourceUsageManager peerResourceUsageManager;
	private static IPingMap pingMap;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IServerExecutor executor;
	private static IPeerReachabilityService peerReachabilityService;

	private final Collection<PeerID> loggedInPeers = Lists.newArrayList();
	private final Collection<PeerID> loggedToPeers = Lists.newArrayList();

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		peerResourceUsageManager = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerResourceUsageManager(IPeerResourceUsageManager serv) {
		if (peerResourceUsageManager == serv) {
			peerResourceUsageManager = null;
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
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;

		peerCommunicator.registerMessageType(CommandMessage.class);
		peerCommunicator.registerMessageType(CommandOutputMessage.class);
		peerCommunicator.registerMessageType(LoginMessage.class);
		peerCommunicator.registerMessageType(LoginOKMessage.class);
		peerCommunicator.registerMessageType(LogoutMessage.class);
		peerCommunicator.registerMessageType(LogoutOKMessage.class);
		peerCommunicator.addListener(this, CommandMessage.class);
		peerCommunicator.addListener(this, CommandOutputMessage.class);
		peerCommunicator.addListener(this, LoginMessage.class);
		peerCommunicator.addListener(this, LoginOKMessage.class);
		peerCommunicator.addListener(this, LogoutMessage.class);
		peerCommunicator.addListener(this, LogoutOKMessage.class);
	}

	// called by OSGi-DS
	public void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator.removeListener(this, CommandMessage.class);
			peerCommunicator.removeListener(this, CommandOutputMessage.class);
			peerCommunicator.removeListener(this, LoginMessage.class);
			peerCommunicator.removeListener(this, LoginOKMessage.class);
			peerCommunicator.removeListener(this, LogoutMessage.class);
			peerCommunicator.removeListener(this, LogoutOKMessage.class);
			peerCommunicator.unregisterMessageType(CommandMessage.class);
			peerCommunicator.unregisterMessageType(CommandOutputMessage.class);
			peerCommunicator.unregisterMessageType(LoginMessage.class);
			peerCommunicator.unregisterMessageType(LoginOKMessage.class);
			peerCommunicator.unregisterMessageType(LogoutMessage.class);
			peerCommunicator.unregisterMessageType(LogoutOKMessage.class);

			peerCommunicator = null;
		}
	}

	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}

	// called by OSGi-DS
	public static void bindCommandProvider(CommandProvider serv) {
		COMMAND_PROVIDERS.add(serv);
	}

	// called by OSGi-DS
	public static void unbindCommandProvider(CommandProvider serv) {
		COMMAND_PROVIDERS.remove(serv);
	}
	
	// called by OSGi-DS
	public static void bindPeerReachabilityService(IPeerReachabilityService serv) {
		peerReachabilityService = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerReachabilityService(IPeerReachabilityService serv) {
		if (peerReachabilityService == serv) {
			peerReachabilityService = null;
		}
	}

	// called by OSGi-DS
	public void activate() {
		LOG.debug("Peer console activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		logoutFromRemotePeers();

		LOG.debug("Peer console deactivated");
	}

	private void logoutFromRemotePeers() {
		LOG.debug("Sending logout to remote peers due to shutdown");
		for (PeerID pid : loggedToPeers) {
			sendLogoutMessage(pid, "<remotePeer>");
		}

		waitSomeTime();

		loggedToPeers.clear();
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Peer commands---\n");
		sb.append("    listPeers/lsPeers              		- Lists all known peers with their ids\n");
		sb.append("    listPeerAddresses/ls...              - Lists all peer addresses (ips)\n");
		sb.append("    resourceStatus                 		- Current status of local MEM, CPU, NET\n");
		sb.append("    ping                           		- Lists the current latencies to known peers\n");
		sb.append("    findPeers <groupfilter>              - Find other peers with UDP broadcast\n");
		sb.append("    listPingPositions/ls...        		- Lists the current position of known peers in the ping map\n");
		sb.append("    peerStatus                     		- Summarizes the current peer status (peerName, ids, etc.)\n");
		sb.append("    listEndpointConnections/ls...  		- Lists all peers which have a true endpoint connection\n");
		sb.append("    listExportedSources/ls...      		- Lists all exported source names\n");
		sb.append("    listImportedSources/ls...      		- Lists all exported source names\n");
		sb.append("    exportSource <sourceName>      		- Exports a local source into the p2p network\n");
		sb.append("    unexportSource <sourceName>    		- Undo export of a local source\n");
		sb.append("    importSource <sourceName>     		- Imports a source from the p2p network\n");
		sb.append("    unimportSource <sourceName>    		- Undo import of a source\n");
		sb.append("    listAvailableSources <filter>/ls...	- Lists known sources from the p2p network\n");
		sb.append("    remoteUpdateAll                		- Sends update signals to all remote peers\n");
		sb.append("    remoteRestartAll                		- Sends restart signals to all remote peers\n");
		sb.append("    listAdvertisements/ls.. <filter>     - Lists all advertisements (class names)\n");
		sb.append("    refreshAdvertisements/ls.. <filter>  - Forces to get remote advertisements again\n");
		sb.append("\n");
		sb.append("    loginPeer <peername> <username> <passw> - Login to remote peer to execute osgi-commands there\n");
		sb.append("    logoutPeer <peername> 			    - Logout from remote peer\n");
		sb.append("    listLoggedInPeers/ls...              - Lists all remote peers which are logged in here\n");
		sb.append("    listLoggedToPeers/ls...              - Lists all remote peers which we are logged in to\n");
		sb.append("    revokeLogin <peername>               - Removes login of remote peer\n");
		sb.append("    executeCommand <peerName> <cmd>      - Remotely execute a command on a peer. Outputs are send back. Login needed!\n");
		sb.append("\n");
		sb.append("    log <level> <text>             		- Creates a log statement\n");
		sb.append("    setLogger <logger> <level>     		- Sets the logging level of a specific logger\n");
		sb.append("    setLoggerOdysseus <logger> <level>	- Sets the logging level of a Odysseus-logger (de.uniol.inf.is.odysseus)\n");
		sb.append("    listLoggers/lsLoggers <filter> 		- Lists all known loggers by name\n");
		sb.append("    jxtaLogDestinations           		- Lists all peers to send log messages to\n");
		sb.append("    listSystemProperties/ls...     		- Lists all set system properties. Filter possible\n");
		sb.append("    setSystemProperty <name> <value>		- Sets system property.\n");
		sb.append("    listThreads/ls... <filter>     		- Lists all currently running threads. Filter possible\n");
		sb.append("\n");
		sb.append("    dumpPlan <queryid>                   - Prints the physical plan of the specified query.\n");
		sb.append("    dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis> - Prints the current stream of the specified operator.\n");
		return sb.toString();
	}

	public void _listPeers(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = peerDictionary.getRemotePeerIDs();
		ci.println("Remote peers known: " + remotePeerIDs.size());

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			output.add(peerDictionary.getRemotePeerName(remotePeerID) + " = " + remotePeerID);
		}

		sortAndPrintList(ci, output);
	}

	public void _lsPeers(CommandInterpreter ci) {
		_listPeers(ci);
	}

	public void _resourceStatus(CommandInterpreter ci) {
		IResourceUsage u = peerResourceUsageManager.getLocalResourceUsage();

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

	public void _ping(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = peerDictionary.getRemotePeerIDs();
		ci.println("Current known ping(s):");

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<Double> optPing = pingMap.getPing(remotePeerID);
			if (optPing.isPresent()) {
				output.add(peerDictionary.getRemotePeerName(remotePeerID) + " : " + optPing.get());
			} else {
				output.add(peerDictionary.getRemotePeerName(remotePeerID) + " : <unknown>");
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _lsPingPositions(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = peerDictionary.getRemotePeerIDs();
		ci.println("Current known ping position(s):");

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<IPingMapNode> optNode = pingMap.getNode(remotePeerID);
			if (optNode.isPresent()) {
				output.add(peerDictionary.getRemotePeerName(remotePeerID) + " : " + toString(optNode.get().getPosition()));
			} else {
				output.add(peerDictionary.getRemotePeerName(remotePeerID) + " : <unknown>");
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _listPingPositions(CommandInterpreter ci) {
		_lsPingPositions(ci);
	}

	private static String toString(Vector3D v) {
		return v.getX() + " / " + v.getY() + " / " + v.getZ();
	}

	public void _peerStatus(CommandInterpreter ci) {
		ci.println("Peername: " + p2pNetworkManager.getLocalPeerName());
		ci.println("PeerID: " + p2pNetworkManager.getLocalPeerID());
		ci.println("Peergroup: " + p2pNetworkManager.getLocalPeerGroupName());
		ci.println("PeergroupID: " + p2pNetworkManager.getLocalPeerGroupID());

		Optional<String> optAddress = InetAddressUtil.getRealInetAddress();
		if (optAddress.isPresent()) {
			ci.println("Address: " + optAddress.get());
		} else {
			ci.println("Address: <not available>");
		}

		ci.println("Port: " + p2pNetworkManager.getPort());
		Optional<String> optHostname = InetAddressUtil.getHostName();
		if (optHostname.isPresent()) {
			ci.println("Hostname: " + optHostname.get());
		} else {
			ci.println("Hostname : <not known>");
		}
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

	public void _jxtaLogDestinations(CommandInterpreter ci) {
		if (JXTALoggingPlugIn.isLogging()) {
			ci.println("Local peer receives log messages.");
		}
		Collection<PeerID> destinations = JxtaLoggingDestinations.getDestinations();

		if (!destinations.isEmpty()) {
			List<String> output = Lists.newLinkedList();
			for (PeerID destination : destinations) {
				output.add(peerDictionary.getRemotePeerName(destination));
			}

			sortAndPrintList(ci, output);
		} else {
			ci.println("No destination set.");
		}
	}

	public void _listEndpointConnections(CommandInterpreter ci) {
		Collection<PeerID> connectedPeers = peerDictionary.getRemotePeerIDs();

		ci.println("Connected peers count: " + connectedPeers.size());
		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : connectedPeers) {
			output.add(peerDictionary.getRemotePeerName(remotePeerID) + " = " + remotePeerID);
		}

		sortAndPrintList(ci, output);
	}

	private static void sortAndPrintList(CommandInterpreter ci, List<String> list) {
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (String line : list) {
				ci.println("\t" + line);
			}
		}
	}

	public void _lsEndpointConnections(CommandInterpreter ci) {
		_listEndpointConnections(ci);
	}

	public void _lsExportedSources(CommandInterpreter ci) {
		ImmutableList<SourceAdvertisement> exportedSources = p2pDictionary.getExportedSources();
		List<String> output = Lists.newArrayList();

		for (SourceAdvertisement exportedSource : exportedSources) {
			output.add(exportedSource.getName() + " " + sourceTypeString(exportedSource));
		}

		sortAndPrintList(ci, output);
	}

	private static String sourceTypeString(SourceAdvertisement adv) {
		if (adv.isStream()) {
			return "[stream]";
		}
		return "[view]";
	}

	public void _listExportedSources(CommandInterpreter ci) {
		_lsExportedSources(ci);
	}

	public void _lsImportedSources(CommandInterpreter ci) {
		ImmutableList<SourceAdvertisement> importedSources = p2pDictionary.getImportedSources();
		List<String> output = Lists.newArrayList();

		for (SourceAdvertisement importedSource : importedSources) {
			output.add(importedSource.getName() + " " + sourceTypeString(importedSource) + " (from " + peerDictionary.getRemotePeerName(importedSource.getPeerID()) + ")");
		}

		sortAndPrintList(ci, output);
	}

	public void _listImportedSources(CommandInterpreter ci) {
		_lsImportedSources(ci);
	}

	public void _exportSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			ci.println("usage: exportSource <sourceName>");
			return;
		}

		try {
			SourceAdvertisement adv = p2pDictionary.exportSource(sourceName);
			ci.println("Source '" + sourceName + "' exported as " + sourceTypeString(adv));
		} catch (PeerException e) {
			ci.println("Export failed: " + e.getMessage());
		}
	}

	public void _importSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			ci.println("usage: importSource <availableSourceName>");
			return;
		}

		try {
			Collection<SourceAdvertisement> sources = p2pDictionary.getSources(sourceName);
			if (sources.size() > 1) {
				ci.println("Source '" + sourceName + "' is ambiguous. Currently not supported.");
				return;
			}
			if (sources.isEmpty()) {
				ci.println("No such source '" + sourceName + "' available");
				return;
			}
			SourceAdvertisement adv = sources.iterator().next();
			p2pDictionary.importSource(adv, sourceName);
			ci.println("Source '" + sourceName + "' imported as " + sourceTypeString(adv));
		} catch (InvalidP2PSource | PeerException e) {
			ci.println("Could not import source '" + sourceName + "': " + e.getMessage());
		}
	}

	public void _unexportSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			ci.println("usage: unexportSource <sourceName>");
			return;
		}

		if (p2pDictionary.isExported(sourceName)) {
			p2pDictionary.removeSourceExport(sourceName);
			ci.println("Source '" + sourceName + "' not exported now.");
		} else {
			ci.println("Source '" + sourceName + "' is currently not exported");
		}
	}

	public void _unimportSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			ci.println("usage: unimportSource <sourceName>");
			return;
		}

		if (p2pDictionary.isImported(sourceName)) {
			Collection<SourceAdvertisement> adv = p2pDictionary.getSources(sourceName);

			if (!adv.isEmpty()) {
				p2pDictionary.removeSourceImport(adv.iterator().next());
				ci.println("Source '" + sourceName + "' not imported now.");
			}
		} else {
			ci.println("Source '" + sourceName + "' is currently not imported");
		}
	}

	public void _lsAvailableSources(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		Collection<SourceAdvertisement> sources = p2pDictionary.getSources();
		List<String> output = Lists.newArrayList();

		for (SourceAdvertisement src : sources) {
			String txt = src.getName() + " " + sourceTypeString(src);
			txt += " from " + peerDictionary.getRemotePeerName(src.getPeerID());

			if (Strings.isNullOrEmpty(filter) || txt.contains(filter)) {
				output.add(txt);
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _listAvailableSources(CommandInterpreter ci) {
		_lsAvailableSources(ci);
	}

	public void _remoteUpdateAll(CommandInterpreter ci) {
		PeerUpdatePlugIn.sendUpdateMessageToRemotePeers();

		ci.println("Send update message to remote peers");
	}

	public void _remoteRestartAll(CommandInterpreter ci) {
		PeerUpdatePlugIn.sendRestartMessageToRemotePeers();

		ci.println("Send restart message to remote peers");
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

	public void _listPeerAddresses(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = peerDictionary.getRemotePeerIDs();
		ci.println("Remote peers known: " + remotePeerIDs.size());

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<String> optAddress = peerDictionary.getRemotePeerAddress(remotePeerID);
			output.add(peerDictionary.getRemotePeerName(remotePeerID) + " : " + (optAddress.isPresent() ? optAddress.get() : "<unknown>"));
		}

		sortAndPrintList(ci, output);
	}

	public void _lsPeerAddresses(CommandInterpreter ci) {
		_listPeerAddresses(ci);
	}

	public void _lsAdvertisements(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		Collection<Advertisement> advs = jxtaServicesProvider.getLocalAdvertisements();
		advs.addAll(jxtaServicesProvider.getPeerAdvertisements());

		List<String> output = Lists.newLinkedList();
		for (Advertisement adv : advs) {
			String txt = adv.getClass().getName();
			if (Strings.isNullOrEmpty(filter) || txt.contains(filter)) {
				output.add(txt);
			}
		}

		sortAndPrintList(ci, output);
	}

	public void _listAdvertisements(CommandInterpreter ci) {
		_lsAdvertisements(ci);
	}

	public void _refreshAdvertisements(CommandInterpreter ci) {
		jxtaServicesProvider.getRemoteAdvertisements();
		ci.println("Refresh done.");
	}

	public void _loginPeer(CommandInterpreter ci) {
		String peername = ci.nextArgument();
		if (Strings.isNullOrEmpty(peername)) {
			ci.println("usage: login <peername> <username> <password>");
			return;
		}

		String username = ci.nextArgument();
		if (Strings.isNullOrEmpty(username)) {
			ci.println("usage: login <peername> <username> <password>");
			return;
		}

		String password = ci.nextArgument();
		if (Strings.isNullOrEmpty(password)) {
			ci.println("usage: login <peername> <username> <password>");
			return;
		}

		LoginMessage loginMsg = new LoginMessage(username, password);

		Optional<PeerID> optPeerID = determinePeerID(peername);
		if (optPeerID.isPresent()) {
			try {
				peerCommunicator.send(optPeerID.get(), loginMsg);
				ci.println("Send login to peer '" + peername + "'");
			} catch (PeerCommunicationException e) {
				ci.println("Could not send login to peer '" + peername + "': " + e.getMessage());
			}
		} else {
			ci.println("Peer '" + peername + "' not known.");
		}
	}

	public void _logoutPeer(CommandInterpreter ci) {
		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {
			ci.println("usage: logout <peername>");
			return;
		}

		Optional<PeerID> optPid = determinePeerID(peerName);
		if (optPid.isPresent()) {
			PeerID pid = optPid.get();
			if (loggedToPeers.contains(pid)) {
				sendLogoutMessage(pid, peerName);
			} else {
				ci.println("Not logged in peer '" + peerName + "'");
			}
		}
	}

	private static void sendLogoutMessage(PeerID pid, String peername) {
		LogoutMessage msg = new LogoutMessage();
		try {
			peerCommunicator.send(pid, msg);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send logout to peer '" + peername + "': " + e.getMessage());
		}
	}

	public void _executeCommand(CommandInterpreter ci) {
		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {
			ci.println("usage: executeCommand <peername> <command>");
			return;
		}

		String command = ci.nextArgument();
		if (Strings.isNullOrEmpty(command)) {
			ci.println("usage: executeCommand <peername> <command>");
			return;
		}

		Optional<PeerID> optPID = determinePeerID(peerName);
		if (optPID.isPresent()) {
			PeerID pid = optPID.get();
			if (loggedToPeers.contains(pid)) {
				sendCommandMessage(ci, peerName, pid, command);
			} else {
				ci.println("Not logged in peer '" + peerName + "'");
			}
		} else {
			ci.println("Peername '" + peerName + "' not known.");
		}
	}

	private static void sendCommandMessage(CommandInterpreter ci, String peerName, PeerID pid, String command) {
		CommandMessage cmd = new CommandMessage(command);
		try {
			peerCommunicator.send(pid, cmd);
			ci.println("Command send to '" + peerName + "'");
		} catch (PeerCommunicationException e) {
			ci.println("Could not send command to peer named '" + peerName + "': " + e.getMessage());
		}
	}

	private static Optional<PeerID> determinePeerID(String peerName) {
		for (PeerID pid : peerDictionary.getRemotePeerIDs()) {
			if (peerDictionary.getRemotePeerName(pid).equals(peerName)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}

	@Override
	public void receivedMessage(IPeerCommunicator communicator, PeerID senderPeer, IMessage message) {
		if (message instanceof CommandMessage) {
			if (!loggedInPeers.contains(senderPeer)) {
				return;
			}

			CommandMessage cmd = (CommandMessage) message;
			processCommandMessage(senderPeer, cmd);
		} else if (message instanceof CommandOutputMessage) {
			CommandOutputMessage cmd = (CommandOutputMessage) message;
			processCommandOutputMessage(communicator, senderPeer, cmd);
		} else if (message instanceof LoginMessage) {
			if (loggedInPeers.contains(senderPeer)) {
				sendLoginOKMessage(senderPeer);
				LOG.debug("Peer {} already logged in", peerDictionary.getRemotePeerName(senderPeer));
				return;
			}

			processLoginMessage(senderPeer, message);
		} else if (message instanceof LoginOKMessage) {
			loggedToPeers.add(senderPeer);
			LOG.error("Login to peer '" + peerDictionary.getRemotePeerName(senderPeer) + "' ok");
		} else if (message instanceof LogoutMessage) {
			loggedInPeers.remove(senderPeer);
			LOG.debug("Peer '{}' logged out", peerDictionary.getRemotePeerName(senderPeer));

			sendLogoutOKMessage(senderPeer);
		} else if (message instanceof LogoutOKMessage) {
			loggedToPeers.remove(senderPeer);
			LOG.error("Logout from peer '" + peerDictionary.getRemotePeerName(senderPeer) + "' ok");
		}
	}

	private static void sendLogoutOKMessage(PeerID senderPeer) {
		LogoutOKMessage msg = new LogoutOKMessage();
		try {
			peerCommunicator.send(senderPeer, msg);
		} catch (PeerCommunicationException e) {
			LOG.debug("Could not send logoutOK message to peer '{}'", peerDictionary.getRemotePeerName(senderPeer), e);
		}
	}

	private void processLoginMessage(PeerID senderPeer, IMessage message) {
		LoginMessage loginMsg = (LoginMessage) message;
		ISession session = executor.login(loginMsg.getUsername(), loginMsg.getPassword().getBytes());
		if (session != null) {
			loggedInPeers.add(senderPeer);
			sendLoginOKMessage(senderPeer);
		}
	}

	private static void sendLoginOKMessage(PeerID senderPeer) {
		LoginOKMessage okMsg = new LoginOKMessage();
		try {
			peerCommunicator.send(senderPeer, okMsg);
		} catch (PeerCommunicationException e) {
			LOG.error("Could not send ok message to peer {}", peerDictionary.getRemotePeerName(senderPeer), e);
		}
	}

	private void processCommandMessage(PeerID senderPeer, CommandMessage cmd) {
		String[] splitted = cmd.getCommandString().split("\\ ", 2);
		String command = splitted[0];
		String parameters = splitted.length > 1 ? splitted[1] : null;
		LOG.debug("Got command message: " + command);

		StringBuilderCommandInterpreter delegateCi = new StringBuilderCommandInterpreter(parameters != null ? parameters.split("\\ ") : new String[0]);
		if (command.equals("help")) {
			printHelp(delegateCi);
		} else {
			Optional<Method> optMethod = determineCommandMethod(command);
			Optional<CommandProvider> optProvider = determineProvider(command);

			if (!optMethod.isPresent()) {
				delegateCi.println("No such command: " + command);
			} else {
				try {
					optMethod.get().invoke(optProvider.get(), delegateCi);
					// delegateCi contains output of command now
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOG.error("Could not execute command {}", command, e);
					delegateCi.println("Could not execute command " + command + " : " + e.getMessage());
				}
			}
		}

		CommandOutputMessage out = new CommandOutputMessage(delegateCi.getText());
		try {
			LOG.debug("Command executed. Send results back");
			peerCommunicator.send(senderPeer, out);
		} catch (PeerCommunicationException e) {
			LOG.debug("Could not send console output", e);
		}
	}

	private static void printHelp(CommandInterpreter ci) {
		for (CommandProvider provider : COMMAND_PROVIDERS) {
			ci.println(provider.getHelp());
		}
	}

	private static Optional<Method> determineCommandMethod(String command) {
		for (CommandProvider provider : COMMAND_PROVIDERS) {
			try {
				return Optional.of(provider.getClass().getMethod("_" + command, CommandInterpreter.class));
			} catch (NoSuchMethodException e) {
			}
		}

		return Optional.absent();
	}

	private static Optional<CommandProvider> determineProvider(String command) {
		for (CommandProvider provider : COMMAND_PROVIDERS) {
			try {
				provider.getClass().getMethod("_" + command, CommandInterpreter.class);
				return Optional.of(provider);
			} catch (NoSuchMethodException e) {
			}
		}

		return Optional.absent();
	}

	private void processCommandOutputMessage(IPeerCommunicator communicator, PeerID senderPeer, CommandOutputMessage cmd) {
		LOG.error("Output from '" + peerDictionary.getRemotePeerName(senderPeer) + "':");
		LOG.error(cmd.getOutput());
	}

	public void _execCommand(CommandInterpreter ci) {
		_executeCommand(ci);
	}

	public void _execCmd(CommandInterpreter ci) {
		_executeCommand(ci);
	}

	public void _lsLoggedInPeers(CommandInterpreter ci) {
		List<String> output = Lists.newArrayList();
		for (PeerID loggedInPeer : loggedInPeers) {
			output.add(peerDictionary.getRemotePeerName(loggedInPeer));
		}

		ci.println("Following remote peers are logged in here:");
		sortAndPrintList(ci, output);
	}

	public void _listLoggedInPeers(CommandInterpreter ci) {
		_lsLoggedInPeers(ci);
	}

	public void _lsLoggedToPeers(CommandInterpreter ci) {
		List<String> output = Lists.newArrayList();
		for (PeerID loggedInPeer : loggedToPeers) {
			output.add(peerDictionary.getRemotePeerName(loggedInPeer));
		}

		ci.println("Following remote peers we are logged in:");
		sortAndPrintList(ci, output);
	}

	public void _listLoggedToPeers(CommandInterpreter ci) {
		_lsLoggedToPeers(ci);
	}

	public void _revokeLogin(CommandInterpreter ci) {
		String peername = ci.nextArgument();
		if (Strings.isNullOrEmpty(peername)) {
			ci.println("usage: revokeLogin <peername>");
			return;
		}

		Optional<PeerID> optPid = determinePeerID(peername);
		if (optPid.isPresent()) {
			PeerID pid = optPid.get();
			if (loggedInPeers.contains(pid)) {
				loggedInPeers.remove(pid);
				sendLogoutOKMessage(pid);
				ci.println("Login of peer '" + peername + "' revoked");
			} else {
				ci.println("Peer '" + peername + "' is not logged in here");
			}
		} else {
			ci.println("Peer '" + peername + "' not known");
		}
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
	
	public void _findPeers( CommandInterpreter ci ) {
		_cryForPeers(ci);
	}

	public void _cryForPeers(final CommandInterpreter ci) {
		Collection<PeerID> reachablePeers = peerReachabilityService.getReachablePeers();
		
		for( PeerID reachablePeer : reachablePeers ) {
			Optional<PeerReachabilityInfo> optInfo = peerReachabilityService.getReachabilityInfo(reachablePeer);
			if( optInfo.isPresent() ) {
				PeerReachabilityInfo info = optInfo.get();
				
				ci.println(info.getAddress().getHostAddress() + ":" + info.getJxtaPort() + " = " + info.getPeerName() + "{" + info.getPeerGroupName() + "} (" + info.getPeerID().toString() + ")");
			}
		}
	}
}
