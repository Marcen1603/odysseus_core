package de.uniol.inf.is.odysseus.report.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.report.registry.ReportProviderRegistry;

public class ReportPlugIn implements BundleActivator {

	private static final ReportProviderRegistry REPORT_PROVIDER_REGISTRY = new ReportProviderRegistry();
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	public static ReportProviderRegistry getReportProviderRegistry() {
		return REPORT_PROVIDER_REGISTRY;
	}
}
