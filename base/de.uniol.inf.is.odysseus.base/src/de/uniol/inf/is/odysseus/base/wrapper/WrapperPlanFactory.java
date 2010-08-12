/*
 * Created on 19.01.2005
 *
 */
package de.uniol.inf.is.odysseus.base.wrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;

/**
 * @author Marco Grawunder
 */
public class WrapperPlanFactory {
	
	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(WrapperPlanFactory.class);
		}
		return _logger;
	}
	
	private static Map<String, ISource<?>> sources = new HashMap<String, ISource<?>>();

	public synchronized static void init() throws Exception {
		//logger.debug("init wrapper");
		DataDictionary.getInstance();
	}

	public synchronized static ISource<?> getAccessPlan(String uri) {
		ISource<?> po = sources.get(uri);
		return po;
	}

	public synchronized static void putAccessPlan(String uri, ISource<?> s) {
		sources.put(uri, s);
	}
	
	//Fuer P2P
	public synchronized static Map<String, ISource<?>> getSources() {
		return sources;
	}

	public synchronized static void clearSources() {
		sources.clear();
	}

	public static void removeClosedSources() {
		Iterator<Entry<String, ISource<?>>> it = sources.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, ISource<?>> curEntry = it.next();
			if (!curEntry.getValue().hasOwner()) {
				it.remove();
			}
		}
	}

}
