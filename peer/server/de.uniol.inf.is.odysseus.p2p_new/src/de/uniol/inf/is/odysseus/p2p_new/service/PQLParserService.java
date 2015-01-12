package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;

public class PQLParserService {

	private static final Logger LOG = LoggerFactory.getLogger(PQLParserService.class);

	private static IQueryParser pqlParser;

	// called by OSGi
	public static void bindParser(IQueryParser serv) {
		if (serv.getLanguage().equals("PQL")) {
			pqlParser = serv;
			LOG.debug("Bound {} as a pql parser.", serv.getClass().getSimpleName());
		}
	}

	// called by OSGi
	public static void unbindParser(IQueryParser serv) {
		if (serv == pqlParser ) {
			LOG.debug("Unbound {} as a pql parser.", serv.getClass().getSimpleName());
			
			pqlParser = null;
		}
	}
	
	public static boolean isBound() {
		return pqlParser != null;
	}
	
	public static IQueryParser getParser() {
		return pqlParser;
	}
}
