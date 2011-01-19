package de.uniol.inf.is.odysseus.p2p.thinpeer.jxta;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p.thinpeer.AbstractThinPeer;

public class Activator implements BundleActivator {
	private AbstractThinPeer thinPeer = null;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		thinPeer = new ThinPeerJxtaImpl();
		thinPeer.startPeer();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
