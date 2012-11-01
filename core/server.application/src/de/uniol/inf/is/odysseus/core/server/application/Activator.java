package de.uniol.inf.is.odysseus.core.server.application;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		startBundles(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	private static void startBundles(final BundleContext context) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for (Bundle bundle : context.getBundles()) {
					boolean isFragment = bundle.getHeaders().get(
							Constants.FRAGMENT_HOST) != null;
					if (bundle != context.getBundle() && !isFragment
							&& bundle.getState() == Bundle.RESOLVED) {
						try {
							bundle.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		t.start();
	}
	
}
