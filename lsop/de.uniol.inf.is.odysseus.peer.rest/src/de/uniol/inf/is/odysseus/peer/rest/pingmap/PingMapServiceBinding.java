package de.uniol.inf.is.odysseus.peer.rest.pingmap;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;





public class PingMapServiceBinding {
	private static IPingMap pingMap;
	private static IPeerDictionary peerDictionary;

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
	
}
