package de.uniol.inf.is.odysseus.test.runner;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class Activator implements BundleActivator {

	private static BundleContext context = null;

	@Override
	public void start(BundleContext ctx) throws Exception {
		startResolvedBundles(ctx);
		context = ctx;
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {

	}
	
	public static BundleContext getBundleContext() {
		return context;
	}

	private static void startResolvedBundles(final BundleContext context) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for (Bundle bundle : context.getBundles()) {
					if ( isValidBundle(context, bundle) ) {
						tryStartBundle(bundle);
					}
				}
			}
		});
		t.start();
	}
	
	private static boolean isValidBundle( BundleContext context, Bundle bundle ) {
		return bundle != context.getBundle() && bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null &&  bundle.getState() == Bundle.RESOLVED;
	}
	
	private static void tryStartBundle(Bundle bundle ) {
		try {
			bundle.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
