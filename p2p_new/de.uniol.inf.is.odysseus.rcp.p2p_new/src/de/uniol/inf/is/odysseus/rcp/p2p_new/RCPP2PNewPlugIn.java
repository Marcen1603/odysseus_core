package de.uniol.inf.is.odysseus.rcp.p2p_new;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;

public class RCPP2PNewPlugIn extends AbstractUIPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(RCPP2PNewPlugIn.class);

	private static IPeerManager peerManager;

	public void start(BundleContext context) throws Exception {
	}

	public void stop(BundleContext context) throws Exception {
	}

	public final void bindPeerManager(IPeerManager pm) {
		peerManager = pm;

		LOG.debug("Bound PeerManager {}", pm);
	}

	public final void unbindPeerManager(IPeerManager pm) {
		if( peerManager == pm ) {
			peerManager = null;
			
			LOG.debug("Unbound PeerManager {}", pm);
		}
	}
	
	public static IPeerManager getPeerManager() {
		return peerManager;
	}
}
