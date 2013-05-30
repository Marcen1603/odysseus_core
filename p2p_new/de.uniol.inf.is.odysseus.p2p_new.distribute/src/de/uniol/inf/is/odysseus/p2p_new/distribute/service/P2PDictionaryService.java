package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

/**
 * Singleton class for the single, static referenced {@link IP2PDictionary}. <br />
 * The referenced {@link IP2PDictionary} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class P2PDictionaryService {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(P2PDictionaryService.class);
	
	/**
	 * The referenced {@link IP2PDictionary}.
	 * @see #bindP2PDictionary(IP2PDictionary)
	 * @see #unbindP2PDictionary(IP2PDictionary)
	 */
	private static IP2PDictionary dictionary;
	
	/**
	 * Binds the referenced {@link IP2PDictionary}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindP2PDictionary(IP2PDictionary)
	 * @param dic An instance of an {@link IP2PDictionary} implementation.
	 */
	public final void bindP2PDictionary(IP2PDictionary dic) {
		
		dictionary = dic;
		LOG.debug("P2PDictionary bound {}", dic);
		
	}
	
	/**
	 * Unbinds an referenced {@link IP2PDictionary}, if <code>dic</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindP2PDictionary(IP2PDictionary)
	 * @param dic An instance of an {@link IP2PDictionary} implementation.
	 */
	public final void unbindP2PDictionary(IP2PDictionary dic) {
		
		if(dictionary == dic) {
			
			dictionary = null;
			LOG.debug("P2PDictionary unbound {}", dic);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link IP2PDictionary}.
	 */
	public static IP2PDictionary get() {
		
		return dictionary;
		
	}
	
	/**
	 * Determines, if a referenced {@link IP2PDictionary} is bound.
	 * @see #bindP2PDictionary(IP2PDictionary)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}

}