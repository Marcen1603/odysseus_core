package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class RoundRobinActivator implements BundleActivator {

	private static IP2PDictionary p2pDictionary;

	// called by OSGi-DS
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
		}
	}
	@Override
	public void start(BundleContext context) throws Exception {
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static IP2PDictionary getP2PDictionary() {
		return p2pDictionary;
	}
}
