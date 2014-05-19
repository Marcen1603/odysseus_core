package de.uniol.inf.is.odysseus.peer.console;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import net.jxta.document.Advertisement;
import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.console.OdysseusConsole;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IMessage;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicatorListener;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerCommunicationException;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.JxtaLoggingDestinations;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.peer.update.PeerUpdatePlugIn;

public class PeerConsole implements CommandProvider, IPeerCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(PeerConsole.class);

	private static IP2PDictionary p2pDictionary;
	private static IPeerResourceUsageManager peerResourceUsageManager;
	private static IPingMap pingMap;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IJxtaServicesProvider jxtaServicesProvider;
	private static IServerExecutor executor;

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
		sb.append("    login <peername> <username> <passw>  - Login to remote peer to execute osgi-commands there\n");
		sb.append("    logout <peername> 			        - Logout from remote peer\n");
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
		sb.append("    listThreads/ls... <filter>     		- Lists all currently running thread. Filter possible\n");
		return sb.toString();
	}

	public void _listPeers(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();
		System.out.println("Remote peers known: " + remotePeerIDs.size());

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " = " + remotePeerID);
		}

		sortAndPrintList(output);
	}

	public void _lsPeers(CommandInterpreter ci) {
		_listPeers(ci);
	}

	public void _resourceStatus(CommandInterpreter ci) {
		IResourceUsage u = peerResourceUsageManager.getLocalResourceUsage();

		System.out.println("Version " + toVersionString(u.getVersion()));
		System.out.println("MEM: " + u.getMemFreeBytes() + " of " + u.getMemMaxBytes() + " Bytes free ( " + (((double) u.getMemFreeBytes() / u.getMemMaxBytes()) * 100.0) + " %)");
		System.out.println("CPU: " + u.getCpuFree() + " of " + u.getCpuMax() + " free ( " + ((u.getCpuFree() / u.getCpuMax()) * 100.0) + " %)");
		System.out.println("NET: Max   = " + u.getNetBandwidthMax());
		System.out.println("NET: Input = " + u.getNetInputRate());
		System.out.println("NET: Output= " + u.getNetOutputRate());
		System.out.println(u.getStoppedQueriesCount() + " queries stopped");
		System.out.println(u.getRunningQueriesCount() + " queries running");
	}

	private static String toVersionString(int[] version) {
		return version[0] + "." + version[1] + "." + version[2] + "." + version[3];
	}

	public void _ping(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();
		System.out.println("Current known ping(s):");

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<Double> optPing = pingMap.getPing(remotePeerID);
			if (optPing.isPresent()) {
				output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " : " + optPing.get());
			} else {
				output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " : <unknown>");
			}
		}

		sortAndPrintList(output);
	}

	public void _peerStatus(CommandInterpreter ci) {
		System.out.println("Peername: " + p2pNetworkManager.getLocalPeerName());
		System.out.println("PeerID: " + p2pNetworkManager.getLocalPeerID());
		System.out.println("Peergroup: " + p2pNetworkManager.getLocalPeerGroupName());
		System.out.println("PeergroupID: " + p2pNetworkManager.getLocalPeerGroupID());
		try {
			System.out.println("Address: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
		}
		System.out.println("Port: " + p2pNetworkManager.getPort());
	}

	public void _log(CommandInterpreter ci) {
		String logLevel = ci.nextArgument();
		if (Strings.isNullOrEmpty(logLevel)) {
			System.out.println("usage: log <logLevel> <message>");
			return;
		}

		String text = ci.nextArgument();
		if (Strings.isNullOrEmpty(text)) {
			System.out.println("usage: log <loglevel> <message>");
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
			System.out.println("Unknown loglevel! Valid: trace, info, debug, warn, error");
		}
	}

	public void _setLogger(CommandInterpreter ci) {
		String loggerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(loggerName)) {
			System.out.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		String logLevel = ci.nextArgument();
		if (Strings.isNullOrEmpty(logLevel)) {
			System.out.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		final int duration = tryToInt(ci.nextArgument());

		setLoggerImpl(loggerName, logLevel, duration);
	}

	public void _setLoggerOdysseus(CommandInterpreter ci) {
		String loggerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(loggerName)) {
			System.out.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		String logLevel = ci.nextArgument();
		if (Strings.isNullOrEmpty(logLevel)) {
			System.out.println("usage: setlog <loggerName> <logLevel>");
			return;
		}

		final int duration = tryToInt(ci.nextArgument());

		setLoggerImpl("de.uniol.inf.is.odysseus." + loggerName, logLevel, duration);
	}

	private static void setLoggerImpl(String loggerName, String logLevel, final int duration) {
		org.apache.log4j.Level level = null;
		try {
			level = org.apache.log4j.Level.toLevel(logLevel.toUpperCase());
		} catch (Throwable t) {
			System.out.println("Level '" + logLevel + "' is invalid.");
			return;
		}

		final org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(loggerName);
		final org.apache.log4j.Level prevLevel = logger.getLevel();
		logger.setLevel(level);

		System.out.println("Set level of logger '" + loggerName + "' to '" + level.toString() + "'");

		if (duration > 0) {
			System.out.println("Level will be set back after " + duration + " ms");

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(duration);
					} catch (InterruptedException e) {
					}

					logger.setLevel(prevLevel);
					System.out.println("Set level of logger '" + logger.getName() + "' back");
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

		sortAndPrintList(output);
	}

	public void _lsLoggers(CommandInterpreter ci) {
		_listLoggers(ci);
	}

	public void _jxtaLogDestinations(CommandInterpreter ci) {
		if (JXTALoggingPlugIn.isLogging()) {
			System.out.println("Local peer receives log messages.");
		}
		Collection<PeerID> destinations = JxtaLoggingDestinations.getDestinations();

		if (!destinations.isEmpty()) {
			List<String> output = Lists.newLinkedList();
			for (PeerID destination : destinations) {
				output.add(p2pDictionary.getRemotePeerName(destination));
			}

			sortAndPrintList(output);
		} else {
			System.out.println("No destination set.");
		}
	}

	public void _listEndpointConnections(CommandInterpreter ci) {
		Collection<PeerID> connectedPeers = p2pDictionary.getRemotePeerIDs();

		System.out.println("Connected peers count: " + connectedPeers.size());
		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : connectedPeers) {
			output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " = " + remotePeerID);
		}

		sortAndPrintList(output);
	}

	private static void sortAndPrintList(List<String> list) {
		if (list != null && !list.isEmpty()) {
			Collections.sort(list);
			for (String line : list) {
				System.out.println("\t" + line);
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

		sortAndPrintList(output);
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
			output.add(importedSource.getName() + " " + sourceTypeString(importedSource) + " (from " + p2pDictionary.getRemotePeerName(importedSource.getPeerID()) + ")");
		}

		sortAndPrintList(output);
	}

	public void _listImportedSources(CommandInterpreter ci) {
		_lsImportedSources(ci);
	}

	public void _exportSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			System.out.println("usage: exportSource <sourceName>");
			return;
		}

		try {
			SourceAdvertisement adv = p2pDictionary.exportSource(sourceName);
			System.out.println("Source '" + sourceName + "' exported as " + sourceTypeString(adv));
		} catch (PeerException e) {
			System.out.println("Export failed: " + e.getMessage());
		}
	}

	public void _importSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			System.out.println("usage: importSource <availableSourceName>");
			return;
		}

		try {
			Collection<SourceAdvertisement> sources = p2pDictionary.getSources(sourceName);
			if (sources.size() > 1) {
				System.out.println("Source '" + sourceName + "' is ambiguous. Currently not supported.");
				return;
			}
			if (sources.isEmpty()) {
				System.out.println("No such source '" + sourceName + "' available");
				return;
			}
			SourceAdvertisement adv = sources.iterator().next();
			p2pDictionary.importSource(adv, sourceName);
			System.out.println("Source '" + sourceName + "' imported as " + sourceTypeString(adv));
		} catch (InvalidP2PSource | PeerException e) {
			System.out.println("Could not import source '" + sourceName + "': " + e.getMessage());
		}
	}

	public void _unexportSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			System.out.println("usage: unexportSource <sourceName>");
			return;
		}

		if (p2pDictionary.isExported(sourceName)) {
			p2pDictionary.removeSourceExport(sourceName);
			System.out.println("Source '" + sourceName + "' not exported now.");
		} else {
			System.out.println("Source '" + sourceName + "' is currently not exported");
		}
	}

	public void _unimportSource(CommandInterpreter ci) {
		String sourceName = ci.nextArgument();
		if (Strings.isNullOrEmpty(sourceName)) {
			System.out.println("usage: unimportSource <sourceName>");
			return;
		}

		if (p2pDictionary.isImported(sourceName)) {
			Collection<SourceAdvertisement> adv = p2pDictionary.getSources(sourceName);

			if (!adv.isEmpty()) {
				p2pDictionary.removeSourceImport(adv.iterator().next());
				System.out.println("Source '" + sourceName + "' not imported now.");
			}
		} else {
			System.out.println("Source '" + sourceName + "' is currently not imported");
		}
	}

	public void _lsAvailableSources(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		Collection<SourceAdvertisement> sources = p2pDictionary.getSources();
		List<String> output = Lists.newArrayList();

		for (SourceAdvertisement src : sources) {
			String txt = src.getName() + " " + sourceTypeString(src);
			txt += " from " + p2pDictionary.getRemotePeerName(src.getPeerID());

			if (Strings.isNullOrEmpty(filter) || txt.contains(filter)) {
				output.add(txt);
			}
		}

		sortAndPrintList(output);
	}

	public void _listAvailableSources(CommandInterpreter ci) {
		_lsAvailableSources(ci);
	}

	public void _remoteUpdateAll(CommandInterpreter ci) {
		PeerUpdatePlugIn.sendUpdateMessageToRemotePeers();

		System.out.println("Send update message to remote peers");
	}
	
	public void _remoteRestartAll( CommandInterpreter ci ) {
		PeerUpdatePlugIn.sendRestartMessageToRemotePeers();
		
		System.out.println("Send restart message to remote peers");
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

		sortAndPrintList(output);
	}

	public void _listSystemProperties(CommandInterpreter ci) {
		_lsSystemProperties(ci);
	}

	public void _setSystemProperty(CommandInterpreter ci) {
		String propName = ci.nextArgument();
		if (Strings.isNullOrEmpty(propName)) {
			System.out.println("usage: setSystemProperty <propName> <value>");
			return;
		}

		String value = ci.nextArgument();
		if (Strings.isNullOrEmpty(value)) {
			System.out.println("usage: setSystemProperty <propName> <value>");
			return;
		}

		System.setProperty(propName, value);
		System.out.println("SystemProperty '" + propName + "' set to '" + value + "'");
	}

	public void _lsThreads(CommandInterpreter ci) {
		String filter = ci.nextArgument();

		int threadCount = Thread.activeCount();
		Thread[] tarray = new Thread[threadCount];
		Thread.enumerate(tarray);

		List<String> output = Lists.newLinkedList();
		for (Thread t : tarray) {
			String text = t.getName() + ": " + t.getState();
			if (Strings.isNullOrEmpty(filter) || text.contains(filter)) {
				output.add(text);
			}
		}

		System.out.println("Thread count: " + threadCount);
		sortAndPrintList(output);
	}

	public void _listThreads(CommandInterpreter ci) {
		_lsThreads(ci);
	}

	public void _listPeerAddresses(CommandInterpreter ci) {
		Collection<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();
		System.out.println("Remote peers known: " + remotePeerIDs.size());

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<String> optAddress = p2pDictionary.getRemotePeerAddress(remotePeerID);
			output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " : " + (optAddress.isPresent() ? optAddress.get() : "<unknown>"));
		}

		sortAndPrintList(output);
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

		sortAndPrintList(output);
	}

	public void _listAdvertisements(CommandInterpreter ci) {
		_lsAdvertisements(ci);
	}

	public void _refreshAdvertisements(CommandInterpreter ci) {
		jxtaServicesProvider.getRemoteAdvertisements();
		System.out.println("Refresh done.");
	}

	public void _loginPeer(CommandInterpreter ci) {
		String peername = ci.nextArgument();
		if (Strings.isNullOrEmpty(peername)) {
			System.out.println("usage: login <peername> <username> <password>");
			return;
		}

		String username = ci.nextArgument();
		if (Strings.isNullOrEmpty(username)) {
			System.out.println("usage: login <peername> <username> <password>");
			return;
		}

		String password = ci.nextArgument();
		if (Strings.isNullOrEmpty(password)) {
			System.out.println("usage: login <peername> <username> <password>");
			return;
		}

		LoginMessage loginMsg = new LoginMessage(username, password);

		Optional<PeerID> optPeerID = determinePeerID(peername);
		if (optPeerID.isPresent()) {
			try {
				peerCommunicator.send(optPeerID.get(), loginMsg);
				System.out.println("Send login to peer '" + peername + "'");
			} catch (PeerCommunicationException e) {
				System.out.println("Could not send login to peer '" + peername + "': " + e.getMessage());
			}
		} else {
			System.out.println("Peer '" + peername + "' not known.");
		}
	}

	public void _logoutPeer(CommandInterpreter ci) {
		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {
			System.out.println("usage: logout <peername>");
			return;
		}

		Optional<PeerID> optPid = determinePeerID(peerName);
		if (optPid.isPresent()) {
			PeerID pid = optPid.get();
			if (loggedToPeers.contains(pid)) {
				sendLogoutMessage(pid, peerName);
			} else {
				System.out.println("Not logged in peer '" + peerName + "'");
			}
		}
	}

	private static void sendLogoutMessage(PeerID pid, String peername) {
		LogoutMessage msg = new LogoutMessage();
		try {
			peerCommunicator.send(pid, msg);
		} catch (PeerCommunicationException e) {
			System.out.println("Could not send logout to peer '" + peername + "': " + e.getMessage());
		}
	}

	public void _executeCommand(CommandInterpreter ci) {
		String peerName = ci.nextArgument();
		if (Strings.isNullOrEmpty(peerName)) {
			System.out.println("usage: executeCommand <peername> <command>");
			return;
		}

		String command = ci.nextArgument();
		if (Strings.isNullOrEmpty(command)) {
			System.out.println("usage: executeCommand <peername> <command>");
			return;
		}

		Optional<PeerID> optPID = determinePeerID(peerName);
		if (optPID.isPresent()) {
			PeerID pid = optPID.get();
			if (loggedToPeers.contains(pid)) {
				sendCommandMessage(peerName, pid, command);
			} else {
				System.out.println("Not logged in peer '" + peerName + "'");
			}
		} else {
			System.out.println("Peername '" + peerName + "' not known.");
		}
	}

	private static void sendCommandMessage(String peerName, PeerID pid, String command) {
		CommandMessage cmd = new CommandMessage(command);
		try {
			peerCommunicator.send(pid, cmd);
			System.out.println("Command send to '" + peerName + "'");
		} catch (PeerCommunicationException e) {
			System.out.println("Could not send command to peer named '" + peerName + "': " + e.getMessage());
		}
	}

	private static Optional<PeerID> determinePeerID(String peerName) {
		for (PeerID pid : p2pDictionary.getRemotePeerIDs()) {
			if (p2pDictionary.getRemotePeerName(pid).equals(peerName)) {
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
				LOG.debug("Peer {} already logged in", p2pDictionary.getRemotePeerName(senderPeer));
				return;
			}

			processLoginMessage(senderPeer, message);
		} else if (message instanceof LoginOKMessage) {
			loggedToPeers.add(senderPeer);
			System.out.println("Login to peer '" + p2pDictionary.getRemotePeerName(senderPeer) + "' ok");
		} else if (message instanceof LogoutMessage) {
			loggedInPeers.remove(senderPeer);
			LOG.debug("Peer '{}' logged out", p2pDictionary.getRemotePeerName(senderPeer));

			sendLogoutOKMessage(senderPeer);
		} else if (message instanceof LogoutOKMessage) {
			loggedToPeers.remove(senderPeer);
			System.out.println("Logout from peer '" + p2pDictionary.getRemotePeerName(senderPeer) + "' ok");
		}
	}

	private static void sendLogoutOKMessage(PeerID senderPeer) {
		LogoutOKMessage msg = new LogoutOKMessage();
		try {
			peerCommunicator.send(senderPeer, msg);
		} catch (PeerCommunicationException e) {
			LOG.debug("Could not send logoutOK message to peer '{}'", p2pDictionary.getRemotePeerName(senderPeer), e);
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
			LOG.error("Could not send ok message to peer {}", p2pDictionary.getRemotePeerName(senderPeer), e);
		}
	}

	private void processCommandMessage(PeerID senderPeer, CommandMessage cmd) {
		String[] splitted = cmd.getCommandString().split("\\ ", 2);
		String command = splitted[0];
		String parameters = splitted.length > 1 ? splitted[1] : null;
		LOG.debug("Got command message: " + command);
		try {
			Method m = null;

			try {
				m = getClass().getMethod("_" + command, CommandInterpreter.class);
			} catch (NoSuchMethodException e) {
				try {
					m = OdysseusConsole.class.getMethod("_" + command, CommandInterpreter.class);
				} catch (NoSuchMethodException e1) {
					LOG.debug("Could not execute remote command", e);
					return;
				}
			}
			CommandInterpreter delegateCi = new DelegateCommandInterpreter(parameters != null ? parameters.split("\\ ") : new String[0]);

			PrintStream oldOut = System.out;
			ConsoleOutputStream cos = new ConsoleOutputStream(System.out);

			LOG.debug("Executing command");
			System.setOut(cos);
			m.invoke(this, delegateCi);
			System.setOut(oldOut);

			String text = cos.getOutput();

			CommandOutputMessage out = new CommandOutputMessage(text);
			try {
				LOG.debug("Command executed. Send results back");
				peerCommunicator.send(senderPeer, out);
			} catch (PeerCommunicationException e) {
				LOG.debug("Could not send console output", e);
			}

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			LOG.debug("Could not execute remote command", e);
		}
	}

	private void processCommandOutputMessage(IPeerCommunicator communicator, PeerID senderPeer, CommandOutputMessage cmd) {
		System.out.println("Output from '" + p2pDictionary.getRemotePeerName(senderPeer) + "':");
		System.out.println(cmd.getOutput());
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
			output.add(p2pDictionary.getRemotePeerName(loggedInPeer));
		}

		System.out.println("Following remote peers are logged in here:");
		sortAndPrintList(output);
	}

	public void _listLoggedInPeers(CommandInterpreter ci) {
		_lsLoggedInPeers(ci);
	}

	public void _lsLoggedToPeers(CommandInterpreter ci) {
		List<String> output = Lists.newArrayList();
		for (PeerID loggedInPeer : loggedToPeers) {
			output.add(p2pDictionary.getRemotePeerName(loggedInPeer));
		}

		System.out.println("Following remote peers we are logged in:");
		sortAndPrintList(output);
	}

	public void _listLoggedToPeers(CommandInterpreter ci) {
		_lsLoggedToPeers(ci);
	}

	public void _revokeLogin(CommandInterpreter ci) {
		String peername = ci.nextArgument();
		if (Strings.isNullOrEmpty(peername)) {
			System.out.println("usage: revokeLogin <peername>");
			return;
		}

		Optional<PeerID> optPid = determinePeerID(peername);
		if (optPid.isPresent()) {
			PeerID pid = optPid.get();
			if (loggedInPeers.contains(pid)) {
				loggedInPeers.remove(pid);
				sendLogoutOKMessage(pid);
				System.out.println("Login of peer '" + peername + "' revoked");
			} else {
				System.out.println("Peer '" + peername + "' is not logged in here");
			}
		} else {
			System.out.println("Peer '" + peername + "' not known");
		}
	}

	public void _dumpStream(CommandInterpreter ci) {
		String operatorHashString = ci.nextArgument();
		if (Strings.isNullOrEmpty(operatorHashString)) {
			System.out.println("usage: dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis>");
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
			System.out.println("usage: dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis>");
			return;
		}

		int timeMillis = 0;
		try {
			timeMillis = Integer.valueOf(timeMillisString);
		} catch (Throwable t) {
			System.out.println("usage: dumpStream <hashOfPhysicalOperator | nameOfOperator> <timeInMillis>");
			return;
		}

		Optional<IPhysicalOperator> optOperator = findOperatorByHash(operatorHash);
		if( !optOperator.isPresent() ) {
			optOperator = findOperatorByName(operatorName);
		}
		
		if (optOperator.isPresent()) {
			final IPhysicalOperator operator = optOperator.get();
			System.out.println("Connecting to physical operator " + operator);

			final DefaultStreamConnection<IStreamObject<?>> conn = new DefaultStreamConnection<IStreamObject<?>>(operator) {
				@Override
				public void process(IStreamObject<?> element, int port) {
					System.out.println(element);
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
					System.out.println("Disconnect from physical operator " + operator);
				}
			});
			t.setDaemon(true);
			t.setName("Operatorconnection disconnect waiting");
			t.start();
		} else {
			System.out.println("No physical operator with hash or name '" + operatorHash + "' found");
		}
	}

	private static Optional<IPhysicalOperator> findOperatorByName(String operatorName) {
		int queryID = -1;
		
		int splitPos = operatorName.indexOf(":");
		if( splitPos != -1 ){
			try {
				queryID = Integer.valueOf( operatorName.substring(0, splitPos));
				operatorName = operatorName.substring(splitPos + 1);
			} catch( Throwable t ) {
			}
		}
		
		Collection<IPhysicalQuery> physicalQueries = executor.getExecutionPlan().getQueries();
		Collection<IPhysicalOperator> foundOperators = Lists.newArrayList();
		for (IPhysicalQuery physicalQuery : physicalQueries) {
			if( queryID == -1 || physicalQuery.getID() == queryID ) {
				List<IPhysicalOperator> physicalOperators = physicalQuery.getPhysicalChilds();
				for (IPhysicalOperator physicalOperator : physicalOperators) {
					if (physicalOperator.getName().equals(operatorName)) {
						foundOperators.add(physicalOperator);
					}
				}
			}
		}
		
		if( foundOperators.size() == 1 ) {
			return Optional.of(foundOperators.iterator().next());
		} else if ( !foundOperators.isEmpty() ) { 
			System.out.println("Warning: Found multiple operators with name '" + operatorName + "': " + foundOperators);
			System.out.println("Selecting first one");
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
			System.out.println("usage: dumpPlan <queryid>");
			return;
		}

		int queryID = 0;
		try {
			queryID = Integer.valueOf(queryIDString);
		} catch (Throwable t) {
			System.out.println("usage: dumpPlan <queryid>");
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
				System.out.println("Physical plan of query: " + queryID);
				System.out.println(sb.toString());
			}
		} else {
			System.out.println("Query not found.");
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

		for (PhysicalSubscription<? extends ISource<?>> source : sink.getSubscribedToSource()) {
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
}
