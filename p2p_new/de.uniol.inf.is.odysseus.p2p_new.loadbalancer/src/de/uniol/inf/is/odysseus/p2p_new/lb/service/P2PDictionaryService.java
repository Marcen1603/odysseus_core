package de.uniol.inf.is.odysseus.p2p_new.lb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class P2PDictionaryService {
	
	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionaryService.class);
	
	/**
	 * The <code>p2pDictionary</code> is used to get access to other peers.
	 */
	private static IP2PDictionary p2pDictionary;
	
	/**
	 * Binds the {@link IP2PDictionary} to get access to other peers. <br />
	 * Called by OSGi-DS
	 * @param dic The {@link IP2PDictionary} to be binded.
	 */
	public final void bindP2PDictionary(IP2PDictionary dic) {
		
		p2pDictionary = dic;
		LOG.debug("P2PDictionary bound {}", dic);
		
	}
	
	/**
	 * Undbinds the {@link IP2PDictionary}. <br />
	 * Called by OSGi-DS
	 * @param dic The {@link IP2PDictionary} to be unbinded.
	 */
	public final void unbindP2PDictionary(IP2PDictionary dic) {
		
		if(p2pDictionary == dic) {
			
			p2pDictionary = null;
			LOG.debug("P2PDictionary unbound {}", dic);
			
		}
		
	}
	
	public static IP2PDictionary get() {
		
		return p2pDictionary;
		
	}
	
	public static boolean isBound() {
		
		return get() != null;
		
	}

}