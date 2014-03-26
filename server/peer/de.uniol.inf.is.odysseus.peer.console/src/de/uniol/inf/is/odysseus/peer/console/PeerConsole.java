package de.uniol.inf.is.odysseus.peer.console;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.jxta.peer.PeerID;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

@SuppressWarnings("unused")
public class PeerConsole implements CommandProvider {

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
		return "---Peer commands---";
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
}
