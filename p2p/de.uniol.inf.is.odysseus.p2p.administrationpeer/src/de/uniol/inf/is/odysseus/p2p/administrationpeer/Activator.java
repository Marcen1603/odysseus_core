package de.uniol.inf.is.odysseus.p2p.administrationpeer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
//import de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.AdministrationPeerJxtaImpl;

public class Activator implements BundleActivator {
	private AbstractAdministrationPeer administrationPeer;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
//		System.out.println("Starte Verwaltungs-Peer");
//		administrationPeer = new AdministrationPeerJxtaImpl();
//		System.out.println("Starte Verwaltungs-Peer2");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		administrationPeer.stopPeer();
	}


}
