package de.uniol.inf.is.odysseus.p2p_new.lb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerAssignmentProvider;

/**
 * Singleton class for the single, static referenced {@link IPeerAssignmentProvider}. <br />
 * The referenced {@link IPeerAssignmentProvider} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class PeerAssignmentProviderService {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PeerAssignmentProviderService.class);
	
	/**
	 * The referenced {@link IPeerAssignmentProvider}.
	 * @see #bindPeerAssignmentProvider(IPeerAssignmentProvider)
	 * @see #unbindPeerAssignmentProvider(IPeerAssignmentProvider)
	 */
	private static IPeerAssignmentProvider provider;
	
	/**
	 * Binds the referenced {@link IPeerAssignmentProvider}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindPeerAssignmentProvider(IPeerAssignmentProvider)
	 * @param prov An instance of an {@link IPeerAssignmentProvider} implementation.
	 */
	public final void bindPeerAssignmentProvider(IPeerAssignmentProvider prov) {
		
		provider = prov;
		LOG.debug("PeerAssignmentProvider bound {}", prov);
		
	}
	
	/**
	 * Unbinds an referenced {@link IPeerAssignmentProvider}, if <code>prov</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindPeerAssignmentProvider(IPeerAssignmentProvider)
	 * @param prov An instance of an {@link IPeerAssignmentProvider} implementation.
	 */
	public final void unbindPeerAssignmentProvider(IPeerAssignmentProvider prov) {
		
		if(provider == prov) {
			
			provider = null;
			LOG.debug("PeerAssignmentProvider unbound {}", prov);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link IPeerAssignmentProvider}.
	 */
	public static IPeerAssignmentProvider get() {
		
		return provider;
		
	}
	
	/**
	 * Determines, if a referenced {@link IPeerAssignmentProvider} is bound.
	 * @see #bindPeerAssignmentProvider(IPeerAssignmentProvider)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}

}