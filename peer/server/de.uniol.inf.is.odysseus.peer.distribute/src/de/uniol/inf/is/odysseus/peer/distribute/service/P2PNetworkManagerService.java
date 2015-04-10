package de.uniol.inf.is.odysseus.peer.distribute.service;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;

/**
 * The service class for a bound {@link IP2PNetworkManager}.
 * @author Michael Brand
 *
 */
public class P2PNetworkManagerService {

	/**
	 * The bound {@link IP2PNetworkManager}.
	 */
	private static IP2PNetworkManager p2pNetworkManager;
	
	/**
	 * Returns the bound {@link IP2PNetworkManager}.
	 */
	public static Optional<IP2PNetworkManager> getP2PNetworkManager() {
		
		return Optional.fromNullable(P2PNetworkManagerService.p2pNetworkManager);
		
	}

	/**
	 * Binds an {@link IP2PNetworkManager}. <br />
	 * Called by OSGi-DS.
	 */
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		
		P2PNetworkManagerService.p2pNetworkManager = serv;
		
	}

	/**
	 * Unbinds an {@link IP2PNetworkManager}, if <code>serv</code> is the bound one. <br />
	 * Called by OSGi-DS.
	 */
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		
		if(P2PNetworkManagerService.p2pNetworkManager == serv) {
			
			P2PNetworkManagerService.p2pNetworkManager = null;
			
		}
		
	}
	
}
