package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;

public class RoundRobinActivator implements BundleActivator {

	private static IPeerDictionary peerDictionary;

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}
	@Override
	public void start(BundleContext context) throws Exception {
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static IPeerDictionary getPeerDictionary() {
		return peerDictionary;
	}
}
