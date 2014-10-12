package de.uniol.inf.is.odysseus.peer.smarthome.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;


public class ServerExecutorService {
private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	
	private static IServerExecutor serverExecutor;
	//private static JxtaRemoveOperatorPlanListener listener = new JxtaRemoveOperatorPlanListener();
	
	// called by OSGi
	public void bindExecutor( IExecutor executor ) {
		if( executor instanceof IServerExecutor ) {
			serverExecutor = (IServerExecutor)executor;
			//serverExecutor.addPlanModificationListener(listener);
			
			LOG.debug("Bound server executor {} ", executor);
		}
	}
	
	// caleld by OSGi
	public void unbindExecutor( IExecutor executor ) {
		if( executor == serverExecutor ){
			//serverExecutor.removePlanModificationListener(listener);
			serverExecutor = null;
			
			LOG.debug("Unbound server executor {} ", executor);
		}
	}
	
	public static IServerExecutor getServerExecutor() {
		return serverExecutor;
	}
	
	public static IDataDictionary getDataDictionary(ITenant tenant) {
		return serverExecutor.getDataDictionary(tenant);
	}
	
	public static boolean isBound() {
		return serverExecutor != null;
	}
}
