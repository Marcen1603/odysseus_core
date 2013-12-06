package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class PQLGeneratorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PQLGeneratorService.class);
	
	private static IPQLGenerator generator;

	public final void bindPQLGenerator(IPQLGenerator gen) {
		generator = gen;
		LOG.debug("PQLGenerator bound {}", gen);
	}
	
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