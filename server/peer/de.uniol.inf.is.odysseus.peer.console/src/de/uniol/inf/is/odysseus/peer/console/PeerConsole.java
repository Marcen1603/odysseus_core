package de.uniol.inf.is.odysseus.peer.console;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import net.jxta.peer.PeerID;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

@SuppressWarnings("unused")
public class PeerConsole implements CommandProvider {

	private static final Logger LOG = LoggerFactory.getLogger(PeerConsole.class);
	
	private static IP2PDictionary p2pDictionary;
	private static IPeerResourceUsageManager peerResourceUsageManager;
	private static IPingMap pingMap;
	private static IP2PNetworkManager p2pNetworkManager;
	
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
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("---Peer commands---\n");
		sb.append("    listPeers               - Lists all known peers with their ids\n");
		sb.append("    resourceStatus          - Current status of local MEM, CPU, NET\n");
		sb.append("    ping                    - Lists the current latencies to known peers\n");
		sb.append("    peerStatus              - Summarizes the current peer status (peerName, ids, etc.)\n");
		sb.append("\n");
		sb.append("    log <level> <text>      - Creates a log statement\n");
		sb.append("    setLog <logger> <level> - Sets the logging level of a specific logger\n");
		sb.append("    listLoggers <filter>    - Lists all known loggers by name\n");
		return sb.toString();
	}

	public void _listPeers(CommandInterpreter ci) {
		ImmutableList<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();
		System.out.println("Remote peers known: " + remotePeerIDs.size());
		for( PeerID remotePeerID : remotePeerIDs ) {
			System.out.println("\t" + p2pDictionary.getRemotePeerName(remotePeerID).get() + " = " + remotePeerID) ;
		}
	}
	
	public void _resourceStatus(CommandInterpreter ci) {
		IResourceUsage u = peerResourceUsageManager.getLocalResourceUsage();
		
		System.out.println("Version " + toVersionString(u.getVersion()));
		System.out.println("MEM: " + u.getMemFreeBytes() + " of " + u.getMemMaxBytes() + " Bytes free ( " + (((double)u.getMemFreeBytes() / u.getMemMaxBytes()) * 100.0 ) + " %)");
		System.out.println("CPU: " + u.getCpuFree() + " of " + u.getCpuMax() + " free ( " + ((u.getCpuFree() / u.getCpuMax()) * 100.0 ) + " %)");
		System.out.println("NET: Max   = " + u.getNetBandwidthMax() );
		System.out.println("NET: Input = " + u.getNetInputRate() );
		System.out.println("NET: Output= " + u.getNetOutputRate() );
		System.out.println(u.getStoppedQueriesCount() + " queries stopped");
		System.out.println(u.getRunningQueriesCount() + " queries running");
	}
	
	private static String toVersionString(int[] version) {
		return version[0] + "." + version[1] + "." + version[2] + "." + version[3];
	}

	public void _ping(CommandInterpreter ci) {
		ImmutableCollection<PeerID> remotePeerIDs = pingMap.getRemotePeerIDs();
		System.out.println("Current known ping(s):");
		for( PeerID remotePeerID : remotePeerIDs ) {
			Optional<Double> optPing = pingMap.getPing(remotePeerID);
			if( optPing.isPresent() ) {
				System.out.println("\t" + p2pDictionary.getRemotePeerName(remotePeerID).get() + " : " + optPing.get());
			}
		}
	}
	
	public void _peerStatus(CommandInterpreter ci ) {
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
	
	public void _log(CommandInterpreter ci ) {
		String logLevel = ci.nextArgument();
		if( Strings.isNullOrEmpty(logLevel) ) {
			System.out.println("usage: log <logLevel> <message>");
			return;
		}
		
		String text = ci.nextArgument();
		if( Strings.isNullOrEmpty(text)) {
			System.out.println("usage: log <loglevel> <message>");
			return;
		}
		
		if( logLevel.equalsIgnoreCase("debug")) {
			LOG.debug(text);
		} else if( logLevel.equals("warn")) {
			LOG.warn(text);
		} else if( logLevel.equals("error")) {
			LOG.error(text);
		} else if( logLevel.equals("trace")) {
			LOG.trace(text);
		} else if( logLevel.equals("info")) {
			LOG.trace(text);
		} else {
			System.out.println("Unknown loglevel! Valid: trace, info, debug, warn, error");
		}
	}
	
	public void _setLogger(CommandInterpreter ci ) {
		String loggerName = ci.nextArgument();
		if( Strings.isNullOrEmpty(loggerName) ) {
			System.out.println("usage: setlog <loggerName> <logLevel>");
			return;
		}
		
		String logLevel = ci.nextArgument();
		if( Strings.isNullOrEmpty(logLevel) ) {
			System.out.println("usage: setlog <loggerName> <logLevel>");
			return;
		}
		org.apache.log4j.Level level = null;
		try {
			level = org.apache.log4j.Level.toLevel(logLevel.toUpperCase());
		} catch( Throwable t ) {
			System.out.println("Level '" + logLevel + "' is invalid.");
			return;
		}
		
		org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(loggerName);
		logger.setLevel(level);
		
		System.out.println("Set level of logger '" + loggerName + "' to '" + level.toString() + "'");
	}
	
	public void _listLoggers(CommandInterpreter ci ) {
		String filter = ci.nextArgument();
		
		Enumeration<?> loggerNames = org.apache.log4j.LogManager.getCurrentLoggers(); 
		while( loggerNames.hasMoreElements() ) {
			org.apache.log4j.Logger elem = (org.apache.log4j.Logger)loggerNames.nextElement();
			
			if( elem.getLevel() != null ) {
				if( Strings.isNullOrEmpty(filter) || (elem.getName().contains(filter))) {
					System.out.println("\t" + elem.getName() + " = " + elem.getLevel());
				}
			}
		}
	}
}
