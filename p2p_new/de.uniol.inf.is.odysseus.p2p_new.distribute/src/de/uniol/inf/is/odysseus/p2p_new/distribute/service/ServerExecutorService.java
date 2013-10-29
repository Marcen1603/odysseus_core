package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

/**
 * Singleton class for the single, static referenced {@link IServerExecutor}. <br />
 * The referenced {@link IServerExecutor} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class ServerExecutorService {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ServerExecutorService.class);
	
	/**
	 * The referenced {@link IServerExecutor}.
	 * @see #bindExecutor(IServerExecutor)
	 * @see #unbindExecutor(IServerExecutor)
	 */
	private static IServerExecutor serverExecutor;
	
	/**
	 * Binds the referenced {@link IServerExecutor}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindExecutor(IServerExecutor)
	 * @param executor An instance of an {@link IServerExecutor} implementation.
	 */
	public void bindExecutor(IExecutor executor ) {
		
		if(executor instanceof IServerExecutor) {
			
			serverExecutor = (IServerExecutor)executor;
			
			LOG.debug("Bound server executor {}", executor);
			
		}
		
	}
	
	/**
	 * Unbinds an referenced {@link IServerExecutor}, if <code>executor</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindExecutor(IServerExecutor)
	 * @param executor An instance of an {@link IServerExecutor} implementation.
	 */
	public void unbindExecutor(IExecutor executor ) {
		
		if(executor == serverExecutor) {
			
			serverExecutor = null;
			
			LOG.debug("Unbound server executor {} and data Dictionary", executor);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link IServerExecutor}.
	 */
	public static IServerExecutor getServerExecutor() {
		
		return serverExecutor;
	}
	
	/**
	 * Returns {@link IServerExecutor#getDataDictionary(ITenant)}.
	 */
	public static IDataDictionary getDataDictionary(ITenant tenant) {
		
		return serverExecutor.getDataDictionary(tenant);
		
	}
	
	/**
	 * Determines, if a referenced {@link IServerExecutor} is bound.
	 * @see #bindExecutor(IServerExecutor)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return serverExecutor != null;
		
	}
	
}