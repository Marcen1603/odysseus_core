package de.uniol.inf.is.odysseus.report.executor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public class ExecutorBugReportPlugIn implements BundleActivator {

	private static IExecutor serverExecutor;

	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		serverExecutor = serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (serverExecutor == serv) {
			serverExecutor = null;
		}
	}
	
	public static boolean isServerExecutorBound() {
		return serverExecutor != null;
	}
	
	public static IExecutor getServerExecutor() {
		return serverExecutor;
	}
		
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
