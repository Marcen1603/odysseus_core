package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ServerExecutorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	
	private static IServerExecutor serverExecutor;
	private static IDataDictionary dataDictionary;
	
	// called by OSGi
	public void bindExecutor(IExecutor executor ) {
		
		if(executor instanceof IServerExecutor) {
			
			serverExecutor = (IServerExecutor)executor;
			dataDictionary = serverExecutor.getDataDictionary();
			
			LOG.debug("Bound server executor {} and DataDictionary {}", executor, dataDictionary);
			
		}
		
	}
	
	// called by OSGi
	public void unbindExecutor( IExecutor executor ) {
		
		if(executor == serverExecutor) {
			
			serverExecutor = null;
			dataDictionary = null;
			
			LOG.debug("Unbound server executor {} and data Dictionary", executor);
			
		}
		
	}
	
	public static IServerExecutor getServerExecutor() {
		
		return serverExecutor;
	}
	
	public static IDataDictionary getDataDictionary() {
		
		return dataDictionary;
		
	}
	
	public static boolean isBound() {
		
		return serverExecutor != null;
		
	}
	
}