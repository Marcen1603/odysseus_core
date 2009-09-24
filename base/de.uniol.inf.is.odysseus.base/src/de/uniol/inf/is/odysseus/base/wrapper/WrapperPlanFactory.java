/*
 * Created on 19.01.2005
 *
 */
package de.uniol.inf.is.odysseus.base.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

/**
 * @author Marco Grawunder
 * 
 */
public class WrapperPlanFactory {
	
	private static final Logger logger = LoggerFactory.getLogger( WrapperPlanFactory.class );
	
	@SuppressWarnings("unchecked")
	private static Map<String, ISource> sources = new HashMap<String, ISource>();

	public synchronized static void init() throws Exception {
		logger.debug("init wrapper");
		DataDictionary.getInstance();
	}

	@SuppressWarnings("unchecked")
	public synchronized static ISource getAccessPlan(String uri) {
		ISource po = sources.get(uri);
		return po;
	}

	public synchronized static void putAccessPlan(String uri, ISource s) {
		sources.put(uri, s);
	}
	
	//Fuer P2P
	public static Map<String, ISource> getSources() {
		return sources;
	}

}
