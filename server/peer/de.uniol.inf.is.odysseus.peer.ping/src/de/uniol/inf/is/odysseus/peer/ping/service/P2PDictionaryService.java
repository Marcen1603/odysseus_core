package de.uniol.inf.is.odysseus.peer.ping.service;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class P2PDictionaryService {

	private static IP2PDictionary dictionary;

	// called by OSGi-DS
	public void bindP2PDictionary(IP2PDictionary serv) {
		dictionary = serv;
	}

	// called by OSGi-DS
	public void unbindP2PDictionary(IP2PDictionary serv) {
		if (dictionary == serv) {
			dictionary = null;
		}
	}

	public static boolean isBound() {
		return get() != null;
	}

	public static IP2PDictionary get() {
		return dictionary;
	}
	
	public static IP2PDictionary waitFor() {
		while( !isBound() ) {
			try {
				Thread.sleep(250);
			} catch( Throwable t ) {}
		}
		return get();
	}
}
