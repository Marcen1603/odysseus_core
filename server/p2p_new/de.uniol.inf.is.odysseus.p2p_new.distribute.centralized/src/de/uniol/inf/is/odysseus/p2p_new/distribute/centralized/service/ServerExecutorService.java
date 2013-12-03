package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.IServiceStatusListener;

public class ServerExecutorService {
	private static List<IServiceStatusListener> listeners = new ArrayList<IServiceStatusListener>();
	private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	private static IServerExecutor executor;
	

	public void bindExecutor(IExecutor e ) {
		if(e instanceof IServerExecutor) {
			executor = (IServerExecutor)e;
			LOG.debug("Bound server executor {}", e);
			for(IServiceStatusListener l : listeners) {
				l.serviceBound(executor);
			}
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
	
	public static void addListener(IServiceStatusListener l) {
		listeners.add(l);
	}
	
}