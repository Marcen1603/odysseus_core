package de.uniol.inf.is.odysseus.p2p.thinpeer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.ThinPeerJxtaImpl;

public class Activator implements BundleActivator {
	private AbstractThinPeer thinPeer;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		thinPeer = ThinPeerJxtaImpl.getInstance();
		thinPeer.startPeer();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		thinPeer.stopPeer();
	}

}
