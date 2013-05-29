package de.uniol.inf.is.odysseus.p2p_new.lb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class PQLGeneratorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PQLGeneratorService.class);
	
	/**
	 * The <code>generator</code> generates PQL statements.
	 */
	private static IPQLGenerator generator;
	
	/**
	 * Binds the {@link IPQLGenerator} to generate PQL statements. <br />
	 * Called by OSGi-DS
	 * @param gen The {@link IPQLGenerator} to be binded.
	 */
	public final void bindPQLGenerator(IPQLGenerator gen) {
		
		generator = gen;
		LOG.debug("PQLGenerator bound {}", gen);
		
	}
	
	/**
	 * Unbinds the {@link IPQLGenerator}. <br />
	 * Called by OSGi-DS
	 * @see #bindPQLGenerator(IPQLGenerator)
	 * @param gen The {@link IPQLGenerator} to be unbinded.
	 */
	public final void unbindPQLGenerator(IPQLGenerator gen) {
		
		if(generator == gen) {
			
			generator = null;
			LOG.debug("PQLGenerator unbound {}", gen);
			
		}
		
	}
	
	public static IPQLGenerator get() {
		
		return generator;
		
	}
	
	public static boolean isBound() {
		
		return get() != null;
		
	}

}