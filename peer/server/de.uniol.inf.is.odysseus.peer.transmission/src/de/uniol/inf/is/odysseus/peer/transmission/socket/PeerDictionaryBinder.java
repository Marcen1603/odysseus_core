package de.uniol.inf.is.odysseus.peer.transmission.socket;

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;

public class PeerDictionaryBinder {

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
	
	public static IPeerDictionary get() {
		return peerDictionary;
	}
}
