package de.uniol.inf.is.odysseus.test.runner;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class Activator implements BundleActivator {

	static public BundleContext context = null;

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
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

	@Override
	public void stop(BundleContext ctx) throws Exception {

	}

	
}
