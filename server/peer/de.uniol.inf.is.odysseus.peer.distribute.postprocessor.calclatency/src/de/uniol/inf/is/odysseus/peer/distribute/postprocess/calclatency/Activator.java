package de.uniol.inf.is.odysseus.peer.distribute.postprocess.calclatency;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;

/**
 * Th activator of the "CalcLatency Postprocessor" bundle.
 * @author Michael Brand
 */
public class Activator implements BundleActivator {

	/**
	 * The bound {@link IP2PNetworkManager}.
	 */
	private static IP2PNetworkManager p2pNetworkManager;
	
	/**
	 * Returns the bound {@link IP2PNetworkManager}, if there is one bound.
	 */
	public static Optional<IP2PNetworkManager> getP2PNetworkManager() {
		
		return Optional.fromNullable(Activator.p2pNetworkManager);
		
	}

	/**
	 * Binds an {@link IP2PNetworkManager}. <br />
	 * Called by OSGi-DS.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		Activator.p2pNetworkManager = serv;
		
	}

	/**
	 * Unbinds an {@link IP2PNetworkManager}, if <code>serv</code> is the bound one. <br />
	 * Called by OSGi-DS.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		if(Activator.p2pNetworkManager == serv) {
			
			Activator.p2pNetworkManager = null;
			
		}
		
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		
		// Nothing to do
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		// Nothing to do
		
	}

}