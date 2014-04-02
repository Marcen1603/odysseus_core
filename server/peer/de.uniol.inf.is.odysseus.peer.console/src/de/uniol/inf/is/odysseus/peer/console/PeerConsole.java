package de.uniol.inf.is.odysseus.peer.console;

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
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.InvalidP2PSource;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.logging.JXTALoggingPlugIn;
import de.uniol.inf.is.odysseus.peer.logging.JxtaLoggingDestinations;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.peer.update.PeerUpdatePlugIn;

@SuppressWarnings("unused")
public class PeerConsole implements CommandProvider {

	private static final Logger LOG = LoggerFactory.getLogger(PeerConsole.class);

	private static IP2PDictionary p2pDictionary;
	private static IPeerResourceUsageManager peerResourceUsageManager;
	private static IPingMap pingMap;
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerCommunicator peerCommunicator;
	private static IJxtaServicesProvider jxtaServicesProvider;

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
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
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
	public void activate() {
		LOG.debug("Peer console activated");
	}

	// called by OSGi-DS
	public void deactivate() {
		LOG.debug("Peer console deactivated");
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
		sb.append("    listAvailableSources <filter>  		- Lists known sources from the p2p network\n");
		sb.append("    remoteUpdateAll                		- Sends update signals to remote peers with matching filter\n");
		sb.append("\n");
		sb.append("    log <level> <text>             		- Creates a log statement\n");
		sb.append("    setLogger <logger> <level>     		- Sets the logging level of a specific logger\n");
		sb.append("    setLoggerOdysseus <logger> <level>	- Sets the logging level of a Odysseus-logger (de.uniol.inf.is.odysseus)\n");
		sb.append("    listLoggers/lsLoggers <filter> 		- Lists all known loggers by name\n");
		sb.append("    jxtaLogDestinations           		- Lists all peers to send log messages to\n");
		sb.append("    listSystemProperties/ls...     		- Lists all set system properties. Filter possible\n");
		sb.append("    setSystemProperty <name> <value>		- Stes system property.\n");
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
		ImmutableCollection<PeerID> remotePeerIDs = pingMap.getRemotePeerIDs();
		System.out.println("Current known ping(s):");

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<Double> optPing = pingMap.getPing(remotePeerID);
			if (optPing.isPresent()) {
				output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " : " + optPing.get());
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

		setLoggerImpl("de.uniol.inf.is.odysseus." + loggerName, logLevel, duration );
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
			SourceAdvertisement adv = p2pDictionary.exportSource(sourceName, "Standard");
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
	
	public void _listPeerAddresses( CommandInterpreter ci ) {
		Collection<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();
		System.out.println("Remote peers known: " + remotePeerIDs.size());

		List<String> output = Lists.newLinkedList();
		for (PeerID remotePeerID : remotePeerIDs) {
			Optional<String> optAddress = p2pDictionary.getRemotePeerAddress(remotePeerID);
			output.add(p2pDictionary.getRemotePeerName(remotePeerID) + " : " + ( optAddress.isPresent() ? optAddress.get() : "<unknown>"));
		}

		sortAndPrintList(output);
	}
	
	public void _lsPeerAddresses( CommandInterpreter ci ) {
		_listPeerAddresses(ci);
	}
	
	public void _lsAdvertisements( CommandInterpreter ci ) {
		String filter = ci.nextArgument();
		
		Collection<Advertisement> advs = jxtaServicesProvider.getLocalAdvertisements();
		advs.addAll(jxtaServicesProvider.getPeerAdvertisements());
		
		List<String> output = Lists.newLinkedList();
		for( Advertisement adv : advs ) {
			String txt = adv.getClass().getName();
			if( Strings.isNullOrEmpty(filter) || txt.contains(filter)) {
				output.add(txt);
			}
		}
		
		sortAndPrintList(output);
	}
	
	public void _listAdvertisement( CommandInterpreter ci ) {
		_lsAdvertisements(ci);
	}
	
	public void _refreshAdvertisements( CommandInterpreter ci ) {
		jxtaServicesProvider.getRemoteAdvertisements();
	}
}
