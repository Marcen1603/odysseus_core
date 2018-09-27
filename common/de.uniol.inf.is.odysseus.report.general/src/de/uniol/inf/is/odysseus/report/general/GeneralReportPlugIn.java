package de.uniol.inf.is.odysseus.report.general;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class GeneralReportPlugIn implements BundleActivator {

	private static BundleContext bundleContext;
	
	@Override
	public void start(BundleContext bc) throws Exception {
		bundleContext = bc;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		bundleContext = null;
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}
}
