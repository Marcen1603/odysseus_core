package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public class LogicalQueryViewDataProviderPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalQueryViewDataProviderPlugIn.class);
	private static IExecutor executor;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
	
	public void bindExecutor(IExecutor exec) {
		if( executor == null ) {
			executor = exec;
			LOG.debug("Executor " + exec + " bound ");
		} else {
			LOG.error("Multiple executors encountered.");
		}
	}

	public void unbindExecutor(IExecutor exec) {
		if( exec == executor ) {
			LOG.debug("Executor " + exec + " unbound");
			executor = null;
		}
	}
	
	public static IExecutor getClientExecutor() {
		return executor;
	}
}
