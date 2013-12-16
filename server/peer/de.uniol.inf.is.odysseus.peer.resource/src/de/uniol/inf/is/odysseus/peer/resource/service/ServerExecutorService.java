package de.uniol.inf.is.odysseus.peer.resource.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class ServerExecutorService {

	private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	
	private static IServerExecutor serverExecutor;
	
	public void bindExecutor( IExecutor executor ) {
		if( isServer(executor)) {
			serverExecutor = (IServerExecutor)executor;
			
			LOG.debug("Server Executor bound {}", executor);
		} else {
			throw new RuntimeException("Executor of type " + executor.getClass().toString() + " must implement the interface " + IServerExecutor.class.toString() + " to be used here");
		}
	}

	private static boolean isServer(IExecutor executor) {
		return executor instanceof IServerExecutor;
	}

	public void unbindExecutor( IExecutor executor ) {
		if( serverExecutor == executor ) {
			serverExecutor = null;
			
			LOG.debug("Unbound executor {}", executor);
		}
	}
	
	public static boolean isBound() {
		return serverExecutor != null;
	}
	
	public static IServerExecutor get() {
		return serverExecutor;
	}
}
