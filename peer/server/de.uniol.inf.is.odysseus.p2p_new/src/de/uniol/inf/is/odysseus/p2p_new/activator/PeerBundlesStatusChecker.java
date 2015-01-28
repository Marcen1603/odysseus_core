package de.uniol.inf.is.odysseus.p2p_new.activator;

import java.util.Collection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class PeerBundlesStatusChecker extends Thread {

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
			if( !areAllBundlesActive(peerBundles) ) {
				waitOneSecond();
			} else {
				System.out.println("All " + peerBundles.size() + " peerBundles are active (services NOT included!).");
				return;
			}
		}
	}

	private static boolean areAllBundlesActive(Collection<Bundle> peerBundles) {
		for( Bundle peerBundle : peerBundles ) {
			if( peerBundle.getState() != Bundle.ACTIVE ) {
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
