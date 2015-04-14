package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;

public class PeerDictionaryService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PeerDictionaryService.class);

	private static IPeerDictionary peerDictionary;

	// called by OSGi
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
		LOG.debug("Bound peer dictionary {}", serv.getClass().getSimpleName());
	}

	// called by OSGi
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (serv == peerDictionary ) {
			LOG.debug("Unbound peer dictionary {}", serv.getClass().getSimpleName());
			
			peerDictionary = null;
		}
	}
	
	public static boolean isBound() {
		return peerDictionary != null;
	}
	
	public static IPeerDictionary getInstance() {
		return peerDictionary;
	}

	public static void waitForStart() {
		while( !isBound() ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}
}
