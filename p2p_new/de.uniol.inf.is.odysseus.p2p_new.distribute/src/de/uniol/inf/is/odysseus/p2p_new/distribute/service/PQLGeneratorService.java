package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

/**
 * Singleton class for the single, static referenced {@link IPQLGenerator}. <br />
 * The referenced {@link IPQLGenerator} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class PQLGeneratorService {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PQLGeneratorService.class);
	
	/**
	 * The referenced {@link IPQLGenerator}.
	 * @see #bindPQLGenerator(IPQLGenerator)
	 * @see #unbindPQLGenerator(IPQLGenerator)
	 */
	private static IPQLGenerator generator;
	
	/**
	 * Binds the referenced {@link IPQLGenerator}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindPQLGenerator(IPQLGenerator)
	 * @param prov An instance of an {@link IPQLGenerator} implementation.
	 */
	public final void bindPQLGenerator(IPQLGenerator gen) {
		
		generator = gen;
		LOG.debug("PQLGenerator bound {}", gen);
		
	}
	
	/**
	 * Unbinds an referenced {@link IPQLGenerator}, if <code>gen</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindPQLGenerator(IPQLGenerator)
	 * @param gen An instance of an {@link IPQLGenerator} implementation.
	 */
	public final void unbindPQLGenerator(IPQLGenerator gen) {
		
		if(generator == gen) {
			
			generator = null;
			LOG.debug("PQLGenerator unbound {}", gen);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link IPQLGenerator}.
	 */
	public static IPQLGenerator get() {
		
		return generator;
		
	}
	
	/**
	 * Determines, if a referenced {@link IPQLGenerator} is bound.
	 * @see #bindPQLGenerator(IPQLGenerator)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}

}