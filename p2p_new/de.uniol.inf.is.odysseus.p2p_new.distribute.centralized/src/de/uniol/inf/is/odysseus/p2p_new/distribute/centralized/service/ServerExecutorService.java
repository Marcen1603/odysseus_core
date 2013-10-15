package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ServerExecutorService {
	private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	private static IServerExecutor executor;
	

	public void bindExecutor(IExecutor e ) {
		if(e instanceof IServerExecutor) {
			executor = (IServerExecutor)e;
			LOG.debug("Bound server executor {}", e);
		}
	}
	
	public void unbindExecutor( IExecutor e ) {
		if(e == executor) {
			executor = null;
			LOG.debug("Unbound server executor {} and data Dictionary", executor);
		}
	}
	
	public static IServerExecutor getServerExecutor() {
		return executor;
	}
	
	public static boolean isBound() {
		return executor != null;
	}
	
}