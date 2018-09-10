package de.uniol.inf.is.odysseus.rcp.profile;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.rcp.profile.views.AdmissionTimeSeriesManager;

public class ProfilePlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(ProfilePlugIn.class);
	
	private static IServerExecutor serverExecutor;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AdmissionTimeSeriesManager.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		AdmissionTimeSeriesManager.stop();
	}
	
	public void bindExecutor( IExecutor executor ) {
		if( executor instanceof IServerExecutor ) {
			serverExecutor = (IServerExecutor) executor;
			
			LOG.debug("ServerExecutor bound: {}", executor);
		}
	}
	
	public void unbindExecutor( IExecutor executor ) {
		if( executor == serverExecutor ) {
			serverExecutor = null;
			
			LOG.debug("ServerExecutor unbound: {}", executor);
		}
	}
	
	public static IServerExecutor getServerExecutor() {
		return serverExecutor;
	}
	
	public static boolean hasServerExecutor() {
		return getServerExecutor() != null;
	}
}
