package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;


/**
 * Class for binding the P2PDictionary-Service to be used within the package,
 * same as {@link de.uniol.inf.is.odysseus.p2p_new.distribute.user.service.P2PDictionaryService}
 * 
 */
public class P2PDictionaryService {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionaryService.class);
	private static IP2PDictionary dictionary;
	
	public void bindP2PDictionary( IP2PDictionary dict ) {
		dictionary = dict;
		
		LOG.debug("P2PDictionary bound");
	}
	
	public void unbindP2PDictionary( IP2PDictionary dict ) {
		if( dict == dictionary ) {
			dictionary = null;
			LOG.debug("P2PDictionary unbound");
		}
	}
	
	public static IP2PDictionary get() {
		return dictionary;
	}
	
	public static boolean isBound() {
		return dictionary != null;
	}
}
