package de.uniol.inf.is.odysseus.p2p_new.lb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

/**
 * Singleton class for the single, static referenced {@link IDataDictionary}. <br />
 * The referenced {@link IDataDictionary} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class DataDictionaryService {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DataDictionaryService.class);
	
	/**
	 * The referenced {@link IDataDictionary}.
	 * @see #bindDataDictionary(IDataDictionary)
	 * @see #unbindDataDictionary(IDataDictionary)
	 */
	private static IDataDictionary dictionary;
	
	/**
	 * Binds the referenced {@link IDataDictionary}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindDataDictionary(IDataDictionary)
	 * @param dic An instance of an {@link IDataDictionary} implementation.
	 */
	public void bindDataDictionary(IDataDictionary dic) {
		
		dictionary = dic;		
		LOG.debug("DataDictionary bound {}", dic);
		
	}
	
	/**
	 * Unbinds an referenced {@link IDataDictionary}, if <code>dic</code> is the binded on. <br />
	 * Called by OSGI-DS.
	 * @see #bindDataDictionary(IDataDictionary)
	 * @param dic An instance of an {@link IDataDictionary} implementation.
	 */
	public void unbindDataDictionary(IDataDictionary dic) {
		
		if(dictionary == dic) {
			
			dictionary = null;			
			LOG.debug("DataDictionary unbound {}", dic);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link IDataDictionary}.
	 */
	public static IDataDictionary get() {
		
		return dictionary;
		
	}
	
	/**
	 * Determines, if a referenced {@link IDataDictionary} is bound.
	 * @see #bindDataDictionary(IDataDictionary)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}
	
}