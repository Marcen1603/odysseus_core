package de.uniol.inf.is.odysseus.rcp.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class ServerExecutorService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	
	private static IServerExecutor serverExecutor;
	
	// called by OSGi
	public void bindExecutor( IExecutor executor ) {
		if( executor instanceof IServerExecutor ) {
			serverExecutor = (IServerExecutor)executor;
			
			LOG.debug("Bound server executor {} ", executor);
		}
	}
	
	// caleld by OSGi
	public void unbindExecutor( IExecutor executor ) {
		if( executor == serverExecutor ){
			serverExecutor = null;
			
			LOG.debug("Unbound server executor {} and data Dictionary", executor);
		}
	}
	
	public static IServerExecutor getServerExecutor() {
		return serverExecutor;
	}
	
	public static IDataDictionary getDataDictionary(ITenant tenant) {
		return getDataDictionary(tenant);
	}
	
	public static boolean isBound() {
		return serverExecutor != null;
	}
}
