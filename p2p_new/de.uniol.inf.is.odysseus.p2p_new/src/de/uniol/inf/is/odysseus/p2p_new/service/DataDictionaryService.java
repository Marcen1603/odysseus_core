package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

public class DataDictionaryService {

	private static final Logger LOG = LoggerFactory.getLogger(DataDictionaryService.class);
	
	private static IDataDictionary dataDictionary;
	
	public void bindDataDictionary( IDataDictionary dd ) {
		dataDictionary = dd;
		
		LOG.debug("DataDictionary bound {}", dd);
	}
	
	public void unbindDataDictionary( IDataDictionary dd ) {
		if( dataDictionary == dd ) {
			dataDictionary = null;
			
			LOG.debug("DataDictionary unbound {}", dd);
		}
	}
	
	public static IDataDictionary get() {
		return dataDictionary;
	}
	
	public static boolean isBound() {
		return get() != null;
	}
}
