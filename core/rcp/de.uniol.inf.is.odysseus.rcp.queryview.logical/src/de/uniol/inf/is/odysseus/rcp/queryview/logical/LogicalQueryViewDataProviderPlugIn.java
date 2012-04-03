package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public class LogicalQueryViewDataProviderPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalQueryViewDataProviderPlugIn.class);
	private static IClientExecutor clientExecutor;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
	
	public void bindExecutor(IExecutor executor) {
		if( clientExecutor == null ) {
			Preconditions.checkArgument(executor instanceof IClientExecutor, "Executor must be an client");
			clientExecutor = (IClientExecutor)executor;
			LOG.debug("ClientExecutor " + executor + " bound ");
		} else {
			LOG.error("Multiple executors encountered.");
		}
	}

	public void unbindExecutor(IExecutor executor) {
		if( executor == clientExecutor ) {
			LOG.debug("ClientExecutor " + executor + " unbound");
			clientExecutor = null;
		}
	}
	
	public static IClientExecutor getClientExecutor() {
		return clientExecutor;
	}
}
