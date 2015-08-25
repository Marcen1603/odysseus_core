package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.allocator.roundrobin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;

/**
 * The {@link BundleActivator} for the bundle <code>de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocator.roundrobin</code>.
 * @author Michael Brand
 */
public class Activator implements BundleActivator {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
	
	/**
	 * The {@link IPeerDictionary}.
	 */
	private static IPeerDictionary cPeerDictionary;
	
	/**
	 * Gets the peer dictionary.
	 * @return The {@link IPeerDictionary}, if one is bound.
	 */
	public static Optional<IPeerDictionary> getPeerDictionary() {
		
		return Optional.fromNullable(Activator.cPeerDictionary);
		
	}
	
	/**
	 * Binds a peer dictionary. <br />
	 * Called by OSGI-DS.
	 * @param dictionary The implementation of {@link IPeerDictionary} to be bound.
	 */
	public static void bindPeerDictionary(IPeerDictionary dictionary) {
		
		Preconditions.checkNotNull("The peer dictionary to be bound must be not null!");
		
		Activator.cPeerDictionary = dictionary;
		LOG.debug("Bound {} as an implementation of {}", dictionary.getClass().getSimpleName(), IP2PDictionary.class.getSimpleName());
		
	}
	
	/**
	 * Removes the binding for a peer dictionary. <br />
	 * Called by OSGI-DS.
	 * @param dictionary The implementation of {@link IPeerDictionary} to be unbound.
	 */
	public static void unbindPeerDictionary(IPeerDictionary dictionary) {
		
		if(dictionary != null && dictionary.equals(Activator.cPeerDictionary)) {
			
			Activator.cPeerDictionary = null;
			LOG.debug("Unbound {} as an implementation of {}", dictionary.getClass().getSimpleName(), IP2PDictionary.class.getSimpleName());
			
		}
		
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		
		// Nothing to do.
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		
		// Nothing to do.
		
	}

}