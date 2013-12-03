package de.uniol.inf.is.odysseus.peer.distribute.allocate.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class P2PDictionaryService {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionaryService.class);

	private static IP2PDictionary dictionary;

	public final void bindP2PDictionary(IP2PDictionary dic) {
		dictionary = dic;
		LOG.debug("P2PDictionary bound {}", dic);
	}

	public final void unbindP2PDictionary(IP2PDictionary dic) {
		if (dictionary == dic) {
			dictionary = null;
			LOG.debug("P2PDictionary unbound {}", dic);
		}
	}

	public static IP2PDictionary get() {
		return dictionary;
	}

	public static boolean isBound() {
		return get() != null;
	}

}