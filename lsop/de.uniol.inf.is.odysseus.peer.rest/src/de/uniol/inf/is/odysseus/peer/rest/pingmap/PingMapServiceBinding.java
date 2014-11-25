package de.uniol.inf.is.odysseus.peer.rest.pingmap;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class PingMapServiceBinding {
	private static IPingMap pingMap;
	private static IPeerDictionary peerDictionary;
	private static IP2PNetworkManager p2pNetworkManager;

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
	public static void bindPingMap(IPingMap serv) {
		pingMap = serv;
	}

	// called by OSGi-DS
	public static void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
		}
	}

	public static IPingMap getPingMap() {
		return pingMap;
	}

	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}
	
	public static IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}

}
