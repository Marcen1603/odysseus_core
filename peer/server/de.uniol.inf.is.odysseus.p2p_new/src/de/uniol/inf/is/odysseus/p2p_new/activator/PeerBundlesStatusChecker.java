package de.uniol.inf.is.odysseus.p2p_new.activator;

import java.util.Collection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class PeerBundlesStatusChecker extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(PeerBundlesStatusChecker.class);
	
	private final BundleContext context;
	
	public PeerBundlesStatusChecker(BundleContext context) {
		Preconditions.checkNotNull(context, "Bundlecontext must not be null!");
		
		setName("Peer bundles checker");
		setDaemon(true);
		
		this.context = context;
	}
	
	@Override
	public void run() {
		Collection<Bundle> peerBundles = determinePeerBundles(context);
		
		while( true ) {
			LOG.debug("Check if all {} peer-bundles are active...", peerBundles.size());
			if( !areAllBundlesActive(peerBundles) ) {
				LOG.debug("No. Waiting...");
				waitOneSecond();
			} else {
				LOG.info("All {} peerBundles are active. OdysseusP2P is ready.", peerBundles.size());
				return;
			}
		}
	}

	private static boolean areAllBundlesActive(Collection<Bundle> peerBundles) {
		for( Bundle peerBundle : peerBundles ) {
			if( peerBundle.getState() != Bundle.ACTIVE ) {
				LOG.debug("PeerBundle {} is not active at the moment", peerBundle.getSymbolicName());
				return false;
			}
		}
		
		return true;
	}

	private static void waitOneSecond() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	private static Collection<Bundle> determinePeerBundles(BundleContext context) {
		Bundle[] bundles = context.getBundles();
		Collection<Bundle> peerBundles = Lists.newArrayList();
		for( Bundle bundle : bundles ) {
			String name = bundle.getSymbolicName();
			if( name.startsWith("de.uniol.inf.is.odysseus.peer") || name.startsWith("de.uniol.inf.is.odysseus.p2p_new")) {
				peerBundles.add(bundle);
			}
		}
		
		return peerBundles;
	}
}
