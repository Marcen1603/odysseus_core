package de.uniol.inf.is.odysseus.p2p.superpeer.jxta;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p.superpeer.AbstractSuperPeer;

public class Activator implements BundleActivator {
	private AbstractSuperPeer superPeer;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Starte Super-Peer");
		superPeer = new SuperPeerJxtaImpl();
		superPeer.startPeer();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
