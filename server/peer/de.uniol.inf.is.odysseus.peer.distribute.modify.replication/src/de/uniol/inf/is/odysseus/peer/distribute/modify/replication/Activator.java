package de.uniol.inf.is.odysseus.peer.distribute.modify.replication;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

/**
 * The activator for the bundle 
 * "de.uniol.inf.is.odysseus.peer.distribute.modify.replication." <br />
 * It provides all bound services.
 * @author Michael Brand
 */
public class Activator implements BundleActivator {
	
	/**
	 * The bound {@link IP2PDictionary}.
	 */
	private static IP2PDictionary p2pDict;
	
	/**
	 * Returns the bound {@link IP2PDictionary}, if there is one bound.
	 */
	public static Optional<IP2PDictionary> getP2PDictionary() {
		
		return Optional.fromNullable(Activator.p2pDict);
		
	}
	
	/**
	 * Binds an {@link IP2PDictionary}.
	 * @param dict The {@link IP2PDictionary} to bind.
	 */
	public static void bindP2PDictionary(IP2PDictionary dict) {
		
		Activator.p2pDict = dict;
		
	}
	
	/**
	 * Unbinds an {@link IP2PDictionary}, if <code>dict</code> is the bound one.
	 * @param dict The {@link IP2PDictionary} to unbind.
	 */
	public static void unbindP2PDictionary(IP2PDictionary dict) {
		
		if(Activator.p2pDict != null || Activator.p2pDict.equals(dict))
			Activator.p2pDict = null;
		
	}

	@Override
	public void start(BundleContext context) throws Exception {
		
		// Nothing to do.

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		// Nothing to do.

	}

}