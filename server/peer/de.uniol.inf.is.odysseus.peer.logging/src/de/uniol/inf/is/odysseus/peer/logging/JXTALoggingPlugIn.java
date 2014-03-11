package de.uniol.inf.is.odysseus.peer.logging;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.IJxtaServicesProvider;
import de.uniol.inf.is.odysseus.p2p_new.IPeerCommunicator;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;

public class JXTALoggingPlugIn implements BundleActivator {

	private static IPeerCommunicator peerCommunicator;
	private static IP2PDictionary p2pDictionary;
	private static IAdvertisementManager advertisementManager;
	private static IJxtaServicesProvider jxtaServicesProvider;

	private JXTAAppender jxtaAppender;

	// called by OSGi-DS
	public static void bindPeerCommunicator(IPeerCommunicator serv) {
		peerCommunicator = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerCommunicator(IPeerCommunicator serv) {
		if (peerCommunicator == serv) {
			peerCommunicator = null;
		}
	}

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

	// called by OSGi-DS
	public static void bindAdvertisementManager(IAdvertisementManager serv) {
		advertisementManager = serv;
	}

	// called by OSGi-DS
	public static void unbindAdvertisementManager(IAdvertisementManager serv) {
		if (advertisementManager == serv) {
			advertisementManager = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindJxtaServicesProvider(IJxtaServicesProvider serv) {
		jxtaServicesProvider = serv;
	}

	// called by OSGi-DS
	public static void unbindJxtaServicesProvider(IJxtaServicesProvider serv) {
		if (jxtaServicesProvider == serv) {
			jxtaServicesProvider = null;
		}
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		jxtaAppender = new JXTAAppender();
		
		Logger.getRootLogger().addAppender(jxtaAppender);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Logger.getRootLogger().removeAppender(jxtaAppender);
	}

	public static IPeerCommunicator getPeerCommunicator() {
		return peerCommunicator;
	}
	
	public static IP2PDictionary getP2PDictionary() {
		return p2pDictionary;
	}
	
	public static IAdvertisementManager getAdvertisementManager() {
		return advertisementManager;
	}
	
	public static IJxtaServicesProvider getJxtaServicesProvider() {
		return jxtaServicesProvider;
	}
}
