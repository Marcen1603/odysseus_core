package de.uniol.inf.is.odysseus.rcp.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

public final class DataDictionaryService {

	private static final Logger LOG = LoggerFactory.getLogger(DataDictionaryService.class);
	private static IDataDictionary instance;
	
	// called by OSGi-Service
	public void bindDataDictionary( IDataDictionary dd ) {
		instance = dd;
		
		LOG.debug("DataDictionary bound {}", dd);
	}
	
	// called by OSGi-Service
	public void unbindDataDictionary( IDataDictionary dd ) {
		if( instance == dd ) {
			instance = null;
			
			LOG.debug("DataDictionary unbound {}", dd );
		}
	}
	
	public static boolean isBound() {
		return instance != null;
	}
	
	public static IDataDictionary get() {
		return instance;
	}
}
